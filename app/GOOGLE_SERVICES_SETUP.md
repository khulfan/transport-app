# 🔥 **IMPORTANT: GOOGLE-SERVICES.JSON SETUP** 🔥

## You MUST add your `google-services.json` file here!

### Steps to get the file:

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project (or create a new one)
3. Click on "Project Settings" (gear icon)
4. Under "Your apps", click on Android icon to add an Android app
5. Enter package name: `com.institute.transport`
6. Download `google-services.json`
7. Place it in this directory: `app/google-services.json`

### Without this file:
- ❌ The project will NOT compile
- ❌ Firebase features will not work
- ❌ Authentication will fail
- ❌ Firestore database access will fail

### Verify file is correct:
The file should contain:
```json
{
  "project_info": {
    "project_number": "...",
    "project_id": "your-project-id",
    ...
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "...",
        "android_client_info": {
          "package_name": "com.institute.transport"
        }
      }
      ...
    }
  ]
}
```

## DO NOT commit this file to version control!
Add to `.gitignore`:
```
google-services.json
```
