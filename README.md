# Kids Fun Quiz

Kids Fun Quiz is an offline Android learning app designed for young children. It presents spoken instructions, large picture and letter choices, encouraging feedback, and simple progress tracking.

## Features

- 15 activity sections with 79 questions
- Missing-letter, picture-choice, vocabulary, patterns, and maths activities
- Offline Android text-to-speech using Indian English
- Two-attempt learning flow: a gentle retry followed by a visual answer reveal
- Responsive phone and tablet layouts
- Animated first-use hint for the Next action
- Local best-score tracking with no account, advertising, or internet requirement

## Requirements

- Android 7.0 (API 24) or newer
- Android Studio with Android SDK 37
- Android Studio's bundled JDK

## Build

Open the repository in Android Studio and use **Build > Build APK(s)**, or run:

```shell
./gradlew assembleDebug
```

The APK will be generated under `app/build/outputs/apk/debug/`.

## Tests

```shell
./gradlew testDebugUnitTest lintDebug
```
