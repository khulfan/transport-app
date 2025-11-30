# 🚀 Quick Start Guide

## Get Running in 30 Minutes!

### Step 1: Firebase Setup (10 min)
1. Go to https://console.firebase.google.com/
2. Create new project: "institute-transport"
3. Add Android app (package: `com.institute.transport`)
4. Download `google-services.json` → place in `app/` folder
5. Enable Authentication (Email/Password)
6. Enable Firestore Database

### Step 2: Google Maps (5 min)
1. Go to https://console.cloud.google.com/
2. APIs & Services → Library
3. Enable "Maps SDK for Android"
4. Create API Key (restrict to Android apps)
5. Add to `local.properties`:
   ```
   MAPS_API_KEY=your_key_here
   ```

### Step 3: WhatsApp Setup (10 min)
1. Go to https://developers.facebook.com/
2. Create Business app
3. Add WhatsApp product
4. Copy Phone Number ID and Access Token
5. Add test phone numbers

### Step 4: Deploy Backend (5 min)
```powershell
cd backend
npm install -g firebase-tools
firebase login
cd functions
npm install
firebase functions:config:set whatsapp.token="YOUR_TOKEN"
firebase functions:config:set whatsapp.phone="YOUR_PHONE_ID"
firebase deploy
```

### Step 5: Run App
1. Open project in Android Studio
2. Wait for Gradle sync
3. Connect Android phone (USB debugging on)
4. Click Run ▶️

---

## ✅ Verification Checklist

- [ ] `google-services.json` in `app/` folder
- [ ] Google Maps API key in `local.properties`
- [ ] Firebase Authentication enabled
- [ ] Firestore database created
- [ ] WhatsApp test numbers added
- [ ] Cloud Functions deployed
- [ ] App running on physical device

---

## 🎯 Test Workflow

### As Driver:
1. Register with driver role
2. Create a route "Test Route"
3. Add 2-3 stops nearby
4. Start trip
5. Walk to first stop
6. Watch geofence trigger

### As Passenger:
1. Register with passenger role
2. Select "Test Route"
3. See live bus location
4. Receive WhatsApp when bus arrives

---

## 🐛 Quick Fixes

**App won't build?**
- Check `google-services.json` exists
- Sync Gradle files
- Clean & Rebuild

**Map not showing?**
- Verify API key is correct
- Check API is enabled
- Restart app

**No WhatsApp messages?**
- Check Firebase Functions logs
- Verify phone numbers added as test numbers
- Check WhatsApp token validity

---

## 📖 Need More Help?

- **Detailed Setup**: See `SETUP_GUIDE.md`
- **Project Info**: See `PROJECT_SUMMARY.md`
- **Backend Help**: See `backend/README.md`

---

**You're all set! Start tracking! 🚌**
