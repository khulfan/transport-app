# ✅ Project Setup Verification Checklist

Use this checklist to verify your setup is complete and working.

---

## 📋 Phase 1: Initial Setup

### Firebase Project
- [ ] Firebase project created
- [ ] Android app added to Firebase
- [ ] Package name: `com.institute.transport`
- [ ] `google-services.json` downloaded
- [ ] `google-services.json` placed in `app/` folder
- [ ] Firebase Authentication enabled
- [ ] Email/Password auth method enabled
- [ ] Cloud Firestore database created
- [ ] Firestore set to production mode (or test mode for development)

### Google Maps
- [ ] Maps SDK for Android enabled in Google Cloud Console
- [ ] API key created
- [ ] API key restricted to Android apps
- [ ] Package name `com.institute.transport` added to restrictions
- [ ] API key added to `local.properties`
- [ ] Format: `MAPS_API_KEY=your_key_here`

### WhatsApp Business API
- [ ] Meta Business account created
- [ ] WhatsApp Business app created
- [ ] WhatsApp product added to app
- [ ] Phone Number ID obtained
- [ ] Permanent access token generated
- [ ] Test phone numbers added
- [ ] Your own number verified

---

## 📋 Phase 2: Backend Deployment

### Firebase CLI
- [ ] Firebase CLI installed (`npm install -g firebase-tools`)
- [ ] Logged in to Firebase (`firebase login`)
- [ ] Project initialized in `backend/` directory

### Cloud Functions
- [ ] Node.js 18+ installed
- [ ] Dependencies installed (`cd backend/functions && npm install`)
- [ ] WhatsApp token configured: `firebase functions:config:set whatsapp.token="..."`
- [ ] Phone ID configured: `firebase functions:config:set whatsapp.phone="..."`
- [ ] Configuration verified: `firebase functions:config:get`
- [ ] Functions deployed: `firebase deploy --only functions`
- [ ] Deployment successful (no errors in console)

### Firestore
- [ ] Security rules deployed: `firebase deploy --only firestore:rules`
- [ ] Indexes deployed: `firebase deploy --only firestore:indexes`
- [ ] Rules verified in Firebase Console
- [ ] Collections visible in Firestore Database

---

## 📋 Phase 3: Android App

### Project Build
- [ ] Android Studio installed (Hedgehog 2023.1.1+)
- [ ] JDK 17+ installed
- [ ] Project opened in Android Studio
- [ ] Gradle sync successful
- [ ] No build errors
- [ ] `google-services.json` detected by build system

### Dependencies
- [ ] All Firebase dependencies resolved
- [ ] Google Play Services dependencies resolved
- [ ] Material Design dependencies resolved
- [ ] Retrofit dependencies resolved
- [ ] Build completes successfully

### Configuration
- [ ] Package name matches: `com.institute.transport`
- [ ] Min SDK: 24 (Android 7.0)
- [ ] Target SDK: 34 (Android 14)
- [ ] Maps API key properly configured
- [ ] No build warnings (or acceptable warnings only)

---

## 📋 Phase 4: Testing

### Device Setup
- [ ] Physical Android device available (emulator won't work for location)
- [ ] USB debugging enabled on device
- [ ] Device connected to computer
- [ ] Device recognized by Android Studio
- [ ] Android version 7.0+ (API 24+)

### App Installation
- [ ] App installed on device successfully
- [ ] App opens without crashing
- [ ] Splash screen displays
- [ ] Auth screen loads

### Permissions
- [ ] Location permission requested
- [ ] Fine location permission granted
- [ ] Background location permission granted (Android 10+)
- [ ] Notification permission granted (Android 13+)

---

## 📋 Phase 5: Feature Testing

### Authentication
- [ ] Can register as Driver
- [ ] Can register as Passenger
- [ ] Can login with created accounts
- [ ] Routes to correct activity based on role
- [ ] User data saved in Firestore
- [ ] Can view user document in Firebase Console

### Driver Features
- [ ] Driver dashboard loads
- [ ] Can access route creation (if implemented)
- [ ] Location permission dialog appears
- [ ] Can start location tracking

### Passenger Features
- [ ] Passenger view loads
- [ ] Can see route selection (if implemented)
- [ ] Can view map (if implemented)

### Location Tracking
- [ ] Foreground service starts
- [ ] Notification appears in status bar
- [ ] GPS location updates in Firestore
- [ ] `busLocations` collection updates every 5 seconds
- [ ] Location visible in Firebase Console

### Geofencing
- [ ] Geofences created when trip starts
- [ ] Can see geofences in Android Settings → Location
- [ ] Geofence triggers when entering stop radius
- [ ] Stop status changes to "ARRIVED" in Firestore
- [ ] Can verify in Firebase Console

### WhatsApp Notifications
- [ ] Cloud Function triggers on stop arrival
- [ ] Function logs visible: `firebase functions:log`
- [ ] WhatsApp message sent to test number
- [ ] Message received on WhatsApp
- [ ] Message format is correct

---

## 📋 Phase 6: Production Readiness

### Security
- [ ] `google-services.json` NOT committed to Git
- [ ] `local.properties` NOT committed to Git
- [ ] `.gitignore` properly configured
- [ ] Firestore security rules deployed
- [ ] API keys restricted properly
- [ ] WhatsApp token kept secret

### Performance
- [ ] App doesn't crash on background/foreground
- [ ] Location updates don't drain battery excessively
- [ ] Firestore queries are efficient
- [ ] No memory leaks observed
- [ ] App responsive on low-end devices

### Error Handling
- [ ] Network errors handled gracefully
- [ ] Permission denials handled
- [ ] Firebase errors caught and logged
- [ ] User-friendly error messages shown

### Documentation
- [ ] All documentation read and understood
- [ ] Setup guide followed successfully
- [ ] Architecture diagram reviewed
- [ ] Code comments adequate

---

## 🎯 Final Verification

### Complete Workflow Test

#### As Driver:
1. [ ] Register with driver role
2. [ ] Create a route with 2-3 stops
3. [ ] Add stops using map
4. [ ] Start trip
5. [ ] Location tracking starts
6. [ ] Move to first stop
7. [ ] Geofence triggers
8. [ ] Stop status updates in Firestore

#### As Passenger:
1. [ ] Register with passenger role
2. [ ] Select the created route
3. [ ] See bus location on map
4. [ ] Receive WhatsApp message when bus arrives at stop
5. [ ] Message format is professional
6. [ ] Can track bus in real-time

### Backend Verification
- [ ] Cloud Function logs show no errors
- [ ] WhatsApp API requests successful
- [ ] Firestore writes recorded
- [ ] No permission denied errors

---

## 🐛 Common Issues Checklist

If something doesn't work, verify:

### Build Issues
- [ ] `google-services.json` exists in `app/` folder
- [ ] Package name matches in all places
- [ ] Gradle sync completed
- [ ] Internet connection available

### Location Issues
- [ ] Using physical device (not emulator)
- [ ] Location services enabled on device
- [ ] GPS/high accuracy mode enabled
- [ ] App has all location permissions
- [ ] Not testing indoors (poor GPS signal)

### Geofence Issues
- [ ] Location permissions granted
- [ ] Background location allowed
- [ ] Trip actually started
- [ ] Within 150m of stop
- [ ] Waited a few minutes (geofences have delay)

### WhatsApp Issues
- [ ] Cloud Functions deployed
- [ ] Config values set correctly
- [ ] Phone number in test numbers list
- [ ] Phone number format correct (no + or spaces)
- [ ] WhatsApp token still valid
- [ ] Function logs checked

### Firebase Issues
- [ ] Project ID correct
- [ ] Security rules deployed
- [ ] User authenticated
- [ ] Network connection available

---

## ✅ Success Criteria

Your setup is complete when:

- ✅ App builds without errors
- ✅ App runs on physical device
- ✅ Can register and login
- ✅ Location tracking works
- ✅ Geofences trigger
- ✅ Firestore updates in real-time
- ✅ WhatsApp messages delivered
- ✅ No crashes or major bugs

---

## 📊 Scoring

Count your checkmarks:

- **50+ checks**: 🎉 **Fully Configured!** Ready for production testing
- **40-49 checks**: 🚀 **Almost There!** Fix remaining issues
- **30-39 checks**: 💪 **Good Progress!** Continue setup
- **< 30 checks**: 📚 **Keep Going!** Follow setup guide

---

## 🆘 Need Help?

If stuck:
1. Check [SETUP_GUIDE.md](SETUP_GUIDE.md) troubleshooting section
2. Review [DOCUMENTATION_INDEX.md](DOCUMENTATION_INDEX.md)
3. Check Firebase Console for errors
4. Review `firebase functions:log`
5. Check Android Logcat for app errors

---

**Last Updated**: November 27, 2025

Print this checklist and check off items as you complete them! 📝
