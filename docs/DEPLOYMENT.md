# Deployment Guide

This guide will walk you through the process of deploying your customized PinoySeoul Radio app to the Google Play Store.

## 1. Versioning

Before you build your release, it's important to update the version code and version name in `app/build.gradle`.

*   `versionCode`: An integer that you increment for each release.
*   `versionName`: A string that is displayed to users.

```gradle
android {
    // ...
    defaultConfig {
        // ...
        versionCode 2
        versionName "1.1"
    }
}
```

## 2. Generate a Signed App Bundle

1.  In Android Studio, go to "Build" > "Generate Signed Bundle / APK".
2.  Select "Android App Bundle" and click "Next".
3.  Select your keystore and enter your credentials.
4.  Choose a destination for your signed app bundle and click "Finish".

This will generate an `.aab` file, which you will upload to the Google Play Store.

## 3. Google Play Console

1.  Go to the [Google Play Console](https://play.google.com/console).
2.  Select your app.
3.  Go to "Release" > "Production".
4.  Click on "Create new release".

### Prepare the Release

1.  Upload your signed app bundle.
2.  **Release name:** A name for your release, such as "1.1".
3.  **Release notes:** A description of the changes in this release.

### Store Listing

Before you can publish your app, you'll need to complete the store listing.

*   **App details:** Title, short description, full description.
*   **Graphic assets:** App icon, feature graphic, screenshots.
*   **Contact details:** Website, email, phone.
*   **Privacy policy:** A URL to your privacy policy.

## 4. Rollout

Once you've prepared your release and completed the store listing, you can roll out your release.

1.  Click on "Save".
2.  Click on "Review release".
3.  If everything looks good, click on "Start rollout to production".

Your app will then be reviewed by Google, and if it's approved, it will be published on the Google Play Store.

## Best Practices

*   **Test your release thoroughly** before you publish it.
*   **Use staged rollouts** to release your app to a small percentage of users at first.
*   **Monitor your app's performance** in the Google Play Console.
*   **Respond to user reviews** to show that you're engaged with your users.
