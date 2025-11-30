# 📊 System Architecture & Data Flow

## 🏗️ High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     ANDROID APPLICATION                      │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Driver     │  │  Passenger   │  │    Auth      │      │
│  │  Activity    │  │   Activity   │  │   Activity   │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                  │              │
│  ┌──────▼──────────────────▼──────────────────▼───────┐    │
│  │              ViewModels (MVVM)                      │    │
│  └──────┬──────────────────┬──────────────────┬───────┘    │
│         │                  │                  │             │
│  ┌──────▼───────┐  ┌───────▼──────┐  ┌───────▼──────┐     │
│  │   Route      │  │   Location   │  │     Auth     │     │
│  │  Repository  │  │  Repository  │  │  Repository  │     │
│  └──────┬───────┘  └───────┬──────┘  └───────┬──────┘     │
└─────────┼──────────────────┼──────────────────┼────────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────────┐
│                    FIREBASE SERVICES                         │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Firestore  │  │     Auth     │  │   Functions  │      │
│  │   Database   │  │              │  │   (Node.js)  │      │
│  └──────┬───────┘  └──────────────┘  └──────┬───────┘      │
└─────────┼────────────────────────────────────┼──────────────┘
          │                                    │
          ▼                                    ▼
┌─────────────────────┐            ┌─────────────────────────┐
│  ANDROID SERVICES   │            │   WHATSAPP CLOUD API    │
│                     │            │                         │
│  • Location Tracker │            │  • Send Messages        │
│  • Geofence Manager │            │  • Message Templates    │
│  • FCM Messaging    │            │  • Delivery Status      │
└─────────────────────┘            └─────────────────────────┘
```

---

## 🔄 Data Flow Diagrams

### 1️⃣ Driver Starts Trip Flow

```
Driver App                  Geofence Manager              Firestore
    │                            │                           │
    │  Start Trip Button         │                           │
    ├────────────────────────────►                           │
    │                            │                           │
    │                       Create Geofences                 │
    │                       (one per stop)                   │
    │                            │                           │
    │                            │  Update Route             │
    │                            │  (activeTrip = true)      │
    │                            ├──────────────────────────►│
    │                            │                           │
    │  Start Location Service    │                           │
    ├────────────────────────────►                           │
    │                            │                           │
    │          Every 5 seconds   │                           │
    │  Upload GPS Location       │                           │
    ├───────────────────────────────────────────────────────►│
    │                            │                           │
```

### 2️⃣ Automatic Stop Arrival & WhatsApp Notification Flow

```
Bus Location        Geofence API      Firestore       Cloud Function    WhatsApp API    Passengers
     │                   │                │                 │                │              │
     │ GPS Update        │                │                 │                │              │
     │ (within 150m)     │                │                 │                │              │
     ├──────────────────►│                │                 │                │              │
     │                   │                │                 │                │              │
     │              Geofence ENTER        │                 │                │              │
     │              Triggered             │                 │                │              │
     │                   │                │                 │                │              │
     │                   │  Update Stop   │                 │                │              │
     │                   │  status=ARRIVED│                 │                │              │
     │                   ├───────────────►│                 │                │              │
     │                   │                │                 │                │              │
     │                   │                │   onUpdate()    │                │              │
     │                   │                │   Trigger       │                │              │
     │                   │                ├────────────────►│                │              │
     │                   │                │                 │                │              │
     │                   │                │            Fetch Passengers      │              │
     │                   │                │            (where routeId=...)   │              │
     │                   │                │◄────────────────┤                │              │
     │                   │                │                 │                │              │
     │                   │                │            For each passenger:   │              │
     │                   │                │            Send WhatsApp Message │              │
     │                   │                │                 ├───────────────►│              │
     │                   │                │                 │                │              │
     │                   │                │                 │           Deliver Message     │
     │                   │                │                 │                ├─────────────►│
     │                   │                │                 │                │              │
     │                   │                │                 │                │  🚌 Bus Arrived!
     │                   │                │                 │                │              │
```

### 3️⃣ Passenger Live Tracking Flow

```
Passenger App              Firestore                  Driver App
     │                        │                           │
     │  Subscribe to          │                           │
     │  Route Updates         │                           │
     ├───────────────────────►│                           │
     │                        │                           │
     │  Real-time Listener    │                           │
     │  Active                │      GPS Update           │
     │                        │◄──────────────────────────┤
     │                        │                           │
     │  Live Updates          │                           │
     │◄───────────────────────┤                           │
     │                        │                           │
     │  Update Map            │                           │
     │  • Bus marker          │                           │
     │  • Route line          │                           │
     │  • Stop markers        │                           │
     │                        │                           │
```

---

## 📦 Database Schema

### Collection: `users`
```javascript
users/{userId} {
  uid: string                    // Firebase Auth UID
  name: string                   // "John Doe"
  email: string                  // "john@example.com"
  phone: string                  // "1234567890"
  role: enum                     // DRIVER | PASSENGER | ADMIN
  routeId: string?               // For passengers only
  assignedRouteIds: string[]     // For drivers only
  createdAt: timestamp
  updatedAt: timestamp
}
```

### Collection: `routes`
```javascript
routes/{routeId} {
  routeId: string                // Auto-generated
  routeName: string              // "Main Campus Route"
  driverId: string               // Reference to user
  driverName: string             // "John Driver"
  activeTrip: boolean            // true/false
  tripStartTime: timestamp?
  tripEndTime: timestamp?
  stops: [                       // Array of stops
    {
      stopIndex: number          // 0, 1, 2...
      stopName: string           // "Main Gate"
      latitude: number           // 40.7128
      longitude: number          // -74.0060
      status: enum               // PENDING | ARRIVED | DEPARTED
      arrivalTime: timestamp?
      geofenceRadius: number     // 150 (meters)
    }
  ]
  createdAt: timestamp
  updatedAt: timestamp
}
```

### Collection: `busLocations`
```javascript
busLocations/{routeId} {         // Document ID = routeId
  locationId: string             // Same as routeId
  routeId: string
  latitude: number               // Current position
  longitude: number
  speed: number                  // m/s
  bearing: number                // Degrees (0-360)
  timestamp: timestamp           // Server timestamp
}
```

---

## 🔐 Security Rules Summary

```
Rules:
  ✅ Users can read/write their own data only
  ✅ Drivers can create/edit/delete their own routes
  ✅ Passengers can read routes (but not modify)
  ✅ All authenticated users can read bus locations
  ✅ Only drivers can update bus locations
  ✅ Cloud Functions have admin access for WhatsApp
```

---

## 🌐 API Integrations

### Firebase Cloud Functions
- **Base URL**: `https://REGION-PROJECT_ID.cloudfunctions.net/`
- **Functions**:
  - `sendStopArrivalNotification` (Firestore trigger)
  - `testWhatsApp` (HTTP endpoint)

### WhatsApp Cloud API
- **Base URL**: `https://graph.facebook.com/v18.0/`
- **Endpoint**: `/{PHONE_NUMBER_ID}/messages`
- **Method**: POST
- **Auth**: Bearer token

### Google Maps SDK
- **Maps Display**: Real-time map rendering
- **Location Services**: FusedLocationProviderClient
- **Geofencing**: GeofencingClient (150m radius)

---

## 📱 App Permissions

```
Required Permissions:
  📍 ACCESS_FINE_LOCATION        (GPS tracking)
  📍 ACCESS_COARSE_LOCATION      (Network location)
  📍 ACCESS_BACKGROUND_LOCATION  (Track when app closed)
  🔔 POST_NOTIFICATIONS          (Android 13+)
  🌐 INTERNET                    (Firebase sync)
  🔋 FOREGROUND_SERVICE          (Continuous tracking)
  🔋 WAKE_LOCK                   (Keep service alive)
```

---

## ⚡ Performance Optimizations

### Location Updates
- **Interval**: 5 seconds (configurable)
- **Priority**: HIGH_ACCURACY
- **Battery Impact**: ~5-8% per hour
- **Optimization**: Stops when trip ends

### Geofencing
- **Radius**: 150 meters (optimal for bus stops)
- **Trigger**: ENTER only
- **Expiration**: Never (until trip ends)
- **Max Geofences**: 100 (Android limit)

### Firestore
- **Indexes**: Optimized for common queries
- **Real-time**: Only active routes
- **Offline**: Automatic caching
- **Batching**: Location updates every 5s

### Cloud Functions
- **Cold Start**: ~2-3 seconds
- **Memory**: 256 MB
- **Timeout**: 60 seconds
- **Retries**: Automatic

---

## 🔄 State Management

### App States
```
Authentication State:
  • Not Logged In → Auth Screen
  • Logged In (Driver) → Driver Dashboard
  • Logged In (Passenger) → Passenger View

Trip State (Driver):
  • No Active Trip → Can start trip
  • Active Trip → Location tracking on
  • Trip Ended → Location tracking off

Tracking State (Passenger):
  • Route Selected → Subscribe to updates
  • Bus Moving → Show live location
  • Bus Arrived → Show notification
```

---

This architecture ensures:
- ✅ Real-time updates
- ✅ Automatic notifications
- ✅ Minimal battery drain
- ✅ Scalable to 100+ buses
- ✅ Secure data access
- ✅ Offline-capable
