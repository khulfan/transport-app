# 📱 Institute Transportation Tracking System - Complete Setup Guide

## 🎯 Project Overview
This is a complete Android application for real-time bus tracking with automated WhatsApp notifications using geofencing technology.

---

## 📋 Prerequisites

### Required Software
- **Android Studio** (Hedgehog 2023.1.1 or later)
- **Java Development Kit (JDK)** 17 or later
- **Node.js** 18 or later (for Firebase Functions)
- **Firebase CLI** (`npm install -g firebase-tools`)
- **Git** (optional, for version control)

### Required Accounts
1. **Google Account** (for Firebase)
2. **Meta Business Account** (for WhatsApp Cloud API)

---

## 🚀 Step-by-Step Setup

### Phase 1: Firebase Project Setup

#### 1.1 Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add Project"
3. Enter project name: `institute-transport` (or your choice)
4. Enable Google Analytics (optional)
5. Create project

#### 1.2 Add Android App to Firebase
1. In Firebase Console, click Android icon
2. Enter package name: `com.institute.transport`
3. Enter app nickname: "Institute Transport"
4. Download `google-services.json`
5. Place file in `app/` directory

#### 1.3 Enable Firebase Services
In Firebase Console, enable:

**Authentication:**
- Go to Authentication → Sign-in method
- Enable "Email/Password"

**Cloud Firestore:**
- Go to Firestore Database
- Create database (start in test mode)
- Choose location closest to your users

**Cloud Functions:**
- Go to Functions
- Upgrade to Blaze plan (pay-as-you-go, has free tier)

---

### Phase 2: WhatsApp Cloud API Setup

#### 2.1 Create Meta Business App
1. Go to [Meta for Developers](https://developers.facebook.com/)
2. Click "My Apps" → "Create App"
3. Select "Business" as app type
4. Fill in app details

#### 2.2 Add WhatsApp Product
1. In your app dashboard, click "Add Product"
2. Find "WhatsApp" and click "Set Up"
3. Follow the setup wizard

#### 2.3 Get Credentials
1. Go to WhatsApp → API Setup
2. Copy **Temporary Access Token** (we'll make it permanent later)
3. Copy **Phone Number ID**
4. Copy **WhatsApp Business Account ID**

#### 2.4 Generate Permanent Token
1. Go to App Settings → Basic
2. Note your App ID and App Secret
3. Use Graph API Explorer to generate permanent token:
   - Go to [Graph API Explorer](https://developers.facebook.com/tools/explorer/)
   - Select your app
   - Request permissions: `whatsapp_business_messaging`, `whatsapp_business_management`
   - Generate Access Token
   - Extend token expiration using Exchange Tool

#### 2.5 Add Test Phone Numbers
1. In WhatsApp → API Setup
2. Add phone numbers for testing (including your own)
3. Verify numbers via SMS/WhatsApp

---

### Phase 3: Google Maps Setup

#### 3.1 Enable Maps API
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project
3. Go to APIs & Services → Library
4. Search and enable:
   - Maps SDK for Android
   - Places API (optional, for search)

#### 3.2 Create API Key
1. Go to APIs & Services → Credentials
2. Click "Create Credentials" → API Key
3. Restrict the key:
   - Application restrictions: Android apps
   - Add package name: `com.institute.transport`
   - Add SHA-1 fingerprint (from Android Studio)
4. API restrictions: Maps SDK for Android

#### 3.3 Add API Key to Project
1. Open `local.properties` in project root
2. Add line:
   ```
   MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
   ```

---

### Phase 4: Android Project Setup

#### 4.1 Open Project in Android Studio
1. Launch Android Studio
2. File → Open
3. Navigate to project folder
4. Wait for Gradle sync

#### 4.2 Update Dependencies
If prompted, update:
- Gradle plugin
- Android SDK
- Build tools

#### 4.3 Build Project
1. Build → Clean Project
2. Build → Rebuild Project
3. Fix any errors (usually related to missing google-services.json)

---

### Phase 5: Firebase Backend Deployment

#### 5.1 Install Firebase CLI
```powershell
npm install -g firebase-tools
```

#### 5.2 Login to Firebase
```powershell
cd backend
firebase login
```

#### 5.3 Initialize Firebase (if needed)
```powershell
firebase init
```
Select:
- Firestore
- Functions
- Choose your existing project

#### 5.4 Install Function Dependencies
```powershell
cd functions
npm install
```

#### 5.5 Configure WhatsApp Credentials
```powershell
firebase functions:config:set whatsapp.token="YOUR_PERMANENT_ACCESS_TOKEN"
firebase functions:config:set whatsapp.phone="YOUR_PHONE_NUMBER_ID"
```

Verify:
```powershell
firebase functions:config:get
```

#### 5.6 Deploy Everything
```powershell
# Deploy Firestore rules and indexes
firebase deploy --only firestore

# Deploy Cloud Functions
firebase deploy --only functions
```

---

### Phase 6: Testing the Application

#### 6.1 Run on Physical Device (Required)
**Note:** Location services require a physical Android device, not an emulator.

1. Enable Developer Options on your Android phone
2. Enable USB Debugging
3. Connect phone to computer
4. In Android Studio, select your device
5. Click Run ▶️

#### 6.2 Test Authentication
1. Open app
2. Register as a Driver
3. Register as a Passenger (use different email)

#### 6.3 Test Driver Flow
1. Login as driver
2. Create a new route
3. Add stops (use map to select locations)
4. Start trip
5. Grant location permissions when prompted

#### 6.4 Test Passenger Flow
1. Login as passenger
2. Select the route
3. View live map
4. Should see bus location updating

#### 6.5 Test Geofencing & WhatsApp
1. As driver, start trip
2. Physically move to a stop location (or use location spoofing)
3. When within 150m of stop, geofence triggers
4. Check Firestore - stop status should change to "ARRIVED"
5. Passenger should receive WhatsApp message

#### 6.6 Monitor Cloud Functions
```powershell
firebase functions:log
```

---

## 🔧 Configuration Files Reference

### `local.properties`
```properties
MAPS_API_KEY=AIzaSy...your_key_here
```

### Firebase Config (set via CLI)
```bash
whatsapp.token = "EAAx...your_token"
whatsapp.phone = "1234567890"
```

---

## 🐛 Troubleshooting

### Issue: "google-services.json not found"
**Solution:** Download from Firebase Console and place in `app/` folder

### Issue: "Location permission denied"
**Solution:** 
- Go to Settings → Apps → Institute Transport → Permissions
- Allow Location (All the time for background tracking)

### Issue: "WhatsApp messages not sending"
**Solution:**
- Check Firebase Functions logs: `firebase functions:log`
- Verify WhatsApp token is valid
- Check phone numbers are added to WhatsApp Business test numbers
- Ensure phone format is correct (digits only, no +)

### Issue: "Geofence not triggering"
**Solution:**
- Ensure location permission granted
- Check geofence radius (default 150m)
- Wait a few minutes - geofences have delays
- Use physical device, not emulator

### Issue: "Map not showing"
**Solution:**
- Check Maps API key is correct
- Verify API is enabled in Google Cloud Console
- Check package name matches in restrictions

---

## 📊 Database Structure

### Firestore Collections

**users/**
```javascript
{
  uid: "user123",
  name: "John Doe",
  email: "john@example.com",
  phone: "1234567890",
  role: "DRIVER" | "PASSENGER",
  routeId: "route_abc" // for passengers
}
```

**routes/**
```javascript
{
  routeId: "route_abc",
  routeName: "Main Campus Route",
  driverId: "driver_uid",
  driverName: "Driver Name",
  activeTrip: true,
  stops: [
    {
      stopIndex: 0,
      stopName: "Main Gate",
      latitude: 40.7128,
      longitude: -74.0060,
      status: "PENDING" | "ARRIVED",
      geofenceRadius: 150
    }
  ]
}
```

**busLocations/**
```javascript
{
  locationId: "route_abc",
  routeId: "route_abc",
  latitude: 40.7128,
  longitude: -74.0060,
  speed: 15.5,
  bearing: 180,
  timestamp: "2024-01-01T12:00:00Z"
}
```

---

## 🔐 Security Best Practices

### 1. Firestore Security Rules
Already configured in `backend/firestore.rules`:
- Users can only read/write their own data
- Drivers can only modify their own routes
- Passengers can read but not modify routes

### 2. API Keys
- Never commit `google-services.json` to Git
- Never commit `local.properties` to Git
- Restrict Maps API key to your package name

### 3. WhatsApp Token
- Store in Firebase Functions config (server-side)
- Never expose in client code
- Use permanent tokens, not temporary

---

## 💰 Cost Estimation

### Free Tier Limits (Monthly)
- **Firebase Authentication:** Unlimited
- **Cloud Firestore:** 50,000 reads, 20,000 writes, 20,000 deletes
- **Cloud Functions:** 2M invocations, 400,000 GB-seconds
- **WhatsApp Cloud API:** 1,000 free conversations
- **Google Maps:** $200 credit (~28,000 map loads)

### For Small Institute (50 buses, 500 users)
- **Expected Cost:** $0-5/month (within free tier)

### For Large Institute (200 buses, 5000 users)
- **Expected Cost:** $10-30/month

---

## 🚀 Next Steps

### Recommended Enhancements
1. **Admin Dashboard** - Web interface for management
2. **Push Notifications** - Firebase Cloud Messaging backup
3. **Route Optimization** - AI-powered route suggestions
4. **QR Attendance** - Scan QR to mark attendance
5. **Analytics Dashboard** - Usage statistics and reports
6. **Multi-language Support** - Localization
7. **Emergency SOS** - Panic button feature
8. **Offline Mode** - Cache routes for offline viewing

### Production Checklist
- [ ] Remove test accounts
- [ ] Enable Crashlytics for error reporting
- [ ] Add ProGuard/R8 obfuscation
- [ ] Implement proper error handling
- [ ] Add analytics tracking
- [ ] Create privacy policy
- [ ] Submit to Google Play Store
- [ ] Set up monitoring and alerts

---

## 📞 Support & Resources

### Documentation Links
- [Firebase Documentation](https://firebase.google.com/docs)
- [WhatsApp Cloud API](https://developers.facebook.com/docs/whatsapp/cloud-api)
- [Google Maps Android SDK](https://developers.google.com/maps/documentation/android-sdk)
- [Android Geofencing](https://developer.android.com/training/location/geofencing)

### Project Repository
- GitHub: [Your Repository URL]
- Issues: [Your Issues URL]

---

## 📄 License

This project is released under the MIT License. See LICENSE file for details.

---

**Last Updated:** November 27, 2025
**Version:** 1.0.0
**Author:** Institute Transportation Project Team
