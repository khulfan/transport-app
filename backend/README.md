# Firebase Backend for Institute Transport System

## Setup Instructions

### 1. Install Firebase CLI
```bash
npm install -g firebase-tools
```

### 2. Login to Firebase
```bash
firebase login
```

### 3. Initialize Firebase Project
```bash
firebase init
```
Select:
- Firestore
- Functions
- Choose existing project or create new one

### 4. Configure WhatsApp Cloud API

#### Get WhatsApp Business API Credentials:
1. Go to [Meta for Developers](https://developers.facebook.com/)
2. Create an app and select "Business" type
3. Add WhatsApp product
4. Get your:
   - Access Token (permanent)
   - Phone Number ID

#### Set environment variables:
```bash
firebase functions:config:set whatsapp.token="YOUR_PERMANENT_ACCESS_TOKEN"
firebase functions:config:set whatsapp.phone="YOUR_PHONE_NUMBER_ID"
```

#### Verify configuration:
```bash
firebase functions:config:get
```

### 5. Install Dependencies
```bash
cd functions
npm install
```

### 6. Deploy Functions
```bash
firebase deploy --only functions
```

### 7. Deploy Firestore Rules
```bash
firebase deploy --only firestore:rules
firebase deploy --only firestore:indexes
```

## Cloud Functions

### `sendStopArrivalNotification`
- **Trigger:** Firestore document update on `routes/{routeId}`
- **Purpose:** Automatically detect when a stop status changes to "ARRIVED"
- **Action:** Send WhatsApp messages to all passengers on that route

### `testWhatsApp`
- **Trigger:** HTTPS request
- **Purpose:** Test WhatsApp message sending manually
- **Usage:**
```bash
curl -X POST https://YOUR_REGION-YOUR_PROJECT.cloudfunctions.net/testWhatsApp \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "1234567890",
    "routeName": "Route A",
    "stopName": "Main Gate"
  }'
```

## WhatsApp Message Format

```
🚌 *Bus Alert*

The bus on route *[Route Name]* has arrived at *[Stop Name]*.

Please be ready!
```

## Firestore Security Rules

- Users can only read/write their own data
- Drivers can create/update/delete their own routes
- All authenticated users can read routes and bus locations
- Only drivers can update bus locations

## Testing Locally

Run Firebase emulators:
```bash
firebase emulators:start
```

This will start:
- Firestore emulator
- Functions emulator
- Emulator UI (http://localhost:4000)

## Monitoring

View function logs:
```bash
firebase functions:log
```

View specific function:
```bash
firebase functions:log --only sendStopArrivalNotification
```

## Troubleshooting

### WhatsApp messages not sending?
1. Check if config is set: `firebase functions:config:get`
2. Verify WhatsApp token is still valid
3. Check phone numbers are in correct format (no + or spaces)
4. View function logs: `firebase functions:log`

### Firestore permission denied?
1. Check security rules are deployed
2. Verify user authentication
3. Check user role in Firestore

## Cost Estimation

- Cloud Functions: First 2 million invocations/month FREE
- Firestore: 50,000 reads/day FREE
- WhatsApp Cloud API: First 1,000 conversations/month FREE

For a small-medium institute, this should stay within free tier limits.
