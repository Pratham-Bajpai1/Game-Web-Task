# Balloon Escape and Three Dots Game Application

This Android application includes two HTML5 games: **Balloon Escape** and **Three Dots**. The app uses a WebView to load and play these games dynamically.

---

## 📱 Features

- **Game 1: Balloon Escape**
  - Launches directly in a WebView from the app's assets.
- **Game 2: Three Dots**
  - Downloads game files dynamically from a server, extracts them, and launches in a WebView.
- Clean and intuitive UI with game images dynamically set as button backgrounds.

---

## 🚀 How to Install

1. Download the **APK file** from the repository:
   - [Download game-web.apk](https://github.com/Pratham-Bajpai1/Game-Web-Task/blob/main/game-web.apk)
2. Transfer the APK to your Android device.
3. Enable **installation from unknown sources** on your device:
   - Go to `Settings > Security > Unknown Sources` and enable it.
4. Open the APK file and follow the installation steps.

---

## 🕹️ How to Use

1. Open the application on your device.
2. Tap on:
   - **Game 1: Balloon Escape** to start playing directly.
   - **Game 2: Three Dots** to download and play the game.
3. Enjoy the games!

---

## 📂 Project Structure


Here’s the corrected project structure with appropriate file extensions and organization:
```
.
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/
│   │   │   │   └── balloonescape/  # Assets for Game 1
│   │   │   ├── java/
│   │   │   │   └── com/
│   │   │   │       └── example/
│   │   │   │           └── gameweb/  # Application source code
│   │   │   └── res/  # Application resources (e.g., layouts, drawables)
│   ├── build/
│   │   ├── outputs/
│   │   │   ├── apk/
│   │   │   │   └── release/
│   │   │   │       └── app-release.apk  # Release APK file
├── README.md  # Project readme
└── game-web.apk  # APK file for installation

```

---

## 💡 Technologies Used

- **Android Java**
- **WebView**
- **NanoHTTPD** for local server functionality
- **Zip extraction** for dynamic game loading

---
