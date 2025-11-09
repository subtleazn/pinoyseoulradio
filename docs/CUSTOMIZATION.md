# Customization Checklist

This guide provides a step-by-step checklist for customizing the PinoySeoul Radio app.

## 1. Brand Identity

### App Name

*   Open `app/src/main/res/values/strings.xml`.
*   Change the `app_name` string to your desired app name.

```xml
<string name="app_name">Your App Name</string>
```

### App Icon

*   Replace the `ic_launcher.png` and `ic_launcher_round.png` files in the `app/src/main/res/mipmap-*` directories with your own app icon.

### Splash Screen

*   Replace the `splash_screen.png` file in the `app/src/main/res/drawable` directory with your own splash screen image.

### Color Scheme

*   Open `app/src/main/res/values/colors.xml`.
*   Modify the color values to match your brand's color scheme.

## 2. Radio Streams

*   Open `app/src/main/java/com/pinoyseoul/radio/settings/Configs.java`.
*   Update the `RADIO_URL` constant with your radio stream URL.

```java
public interface Configs {
    String RADIO_URL = "YOUR_RADIO_STREAM_URL";
    // ...
}
```

## 3. Firebase Services

### OneSignal

*   Open `app/build.gradle`.
*   Replace `"YOUR_ONESIGNAL_APP_ID"` with your OneSignal App ID.

```gradle
android {
    // ...
    defaultConfig {
        // ...
        manifestPlaceholders = [
                onesignal_app_id: "YOUR_ONESIGNAL_APP_ID",
                // ...
        ]
    }
}
```

### AdMob

*   Open `app/src/main/res/values/strings.xml`.
*   Replace the `admob_app_id`, `admob_banner_ad_unit_id`, and `admob_interstitial_ad_unit_id` strings with your AdMob credentials.

## 4. Package Name

1.  In Android Studio, right-click on the package `com.pinoyseoul.radio` in the "Project" view.
2.  Select "Refactor" > "Rename".
3.  Enter your desired package name and click "Refactor".
4.  Open `app/build.gradle` and update the `applicationId` to your new package name.

```gradle
android {
    // ...
    defaultConfig {
        applicationId "com.your.new.package.name"
        // ...
    }
}
```

## 5. Testing

After making your customizations, it's important to test the app thoroughly.

*   Build and run the app on an emulator or a physical device.
*   Verify that the app name, icon, and splash screen are correct.
*   Test the radio stream to ensure that it plays correctly.
*   Test push notifications to ensure that they are received.
*   Verify that AdMob ads are displayed correctly.
