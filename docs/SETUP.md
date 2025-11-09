# Setup Guide

This guide will walk you through the process of setting up the PinoySeoul Radio project for development.

## 1. Prerequisites

*   **Android Studio:** Make sure you have the latest version of Android Studio installed.
*   **Git:** You'll need Git to clone the repository.
*   **Firebase Account:** A Firebase account is required for push notifications and analytics.

## 2. Clone the Repository

```bash
git clone https://github.com/pinoyseoul/pinoyseoulradio.git
cd pinoyseoulradio
```

## 3. Open in Android Studio

1.  Open Android Studio.
2.  Click on "Open an existing Android Studio project".
3.  Navigate to the `pinoyseoulradio` directory and click "OK".

## 4. Firebase Setup

1.  Go to the [Firebase Console](https://console.firebase.google.com/).
2.  Click on "Add project" and follow the on-screen instructions.
3.  Once your project is created, click on the Android icon to add an Android app to your project.
4.  Enter the package name `com.pinoyseoul.radio` and click "Register app".
5.  Download the `google-services.json` file and place it in the `app` directory of the project.
6.  Follow the on-screen instructions to add the Firebase SDK to your project (this should already be done).

## 5. Configure Keystore

To build a release version of the app, you'll need to create a keystore.

1.  In Android Studio, go to "Build" > "Generate Signed Bundle / APK".
2.  Select "Android App Bundle" and click "Next".
3.  Click on "Create new..." and fill in the required information.
4.  Once you've created your keystore, create a file named `key.properties` in the root of the project with the following content:

```
storePassword=YOUR_STORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=YOUR_KEY_ALIAS
storeFile=/path/to/your/keystore.jks
```

## 6. Build and Run

You should now be able to build and run the app on an emulator or a physical device.

1.  In Android Studio, select your desired run configuration.
2.  Click on the "Run" button.

If you encounter any issues, please open an issue on the [GitHub repository](https://github.com/pinoyseoul/pinoyseoulradio/issues).
