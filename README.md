# 💬 ChatApp - Real-Time Android Messaging Application

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android%20(API%2024%2B)-green.svg?style=flat&logo=android)](https://developer.android.com)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg?style=flat&logo=firebase)](https://firebase.google.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A modern native Android real-time chat application built with **Kotlin** and powered by **Google Firebase**. The application supports user authentication, real-time 1-on-1 instant messaging, contact search, and profile picture management with cloud storage.

---

## 📱 Features

- 🔐 **User Authentication**
  - Secure registration and login powered by **Firebase Authentication**.
  - Form validation with Material Design Text Input components.
- ⚡ **Real-Time 1-to-1 Messaging**
  - Instant message delivery and live message listeners using **Cloud Firestore**.
  - Distinct chat bubble UI (incoming vs. outgoing messages) with timestamps.
- 🔍 **User Discovery & Contacts**
  - Real-time search engine to find registered users by name or email.
  - Add users to contacts and initiate direct conversations.
- 🖼️ **Media & Profile Customization**
  - Profile image selection from device gallery.
  - Image storage and hosting via **Firebase Storage**.
  - High-performance image caching and rounded rendering powered by **Glide**.
- 🎨 **Modern Android UI**
  - Material Design 3 guidelines.
  - Clean animated Splash Screen and intuitive navigation.

---

## 🏗️ Architecture & Tech Stack

The application follows Android architecture best practices with modular separation of concerns:

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: Android XML with Material Components & View Binding
- **Backend as a Service (BaaS)**:
  - **Firebase Authentication**: User identity and credential management.
  - **Cloud Firestore**: NoSQL real-time document database for messages, friends, and user metadata.
  - **Firebase Storage**: Scalable cloud object storage for user avatars.
- **Image Loading & Caching**: [Glide](https://github.com/bumptech/glide)
- **Asynchronous Operations**: Firebase Task API & Android SDK Callbacks
- **Build System**: Gradle with Kotlin DSL (`build.gradle.kts`)

### Project Structure

```
app/src/main/java/com/zinebbouakkiz/chatapp/
│
├── ChatApp.kt                      # Application class
│
├── activities/                     # Activity controllers & UI screens
│   ├── SplashScreenActivity.kt     # App launch & auth check
│   ├── AuthentificationActivity.kt # User sign-in
│   ├── RegisterActivity.kt         # New account registration
│   ├── HomeActivity.kt             # Recent chats & friends list
│   ├── ChatActivity.kt             # Real-time chat screen
│   ├── UsersSearchActivity.kt      # Search and add contacts
│   └── SettingsActivity.kt         # User profile & avatar update
│
├── adapters/                       # Custom RecyclerView Adapters
│   ├── ChatRecyclerAdapter.kt      # Message stream adapter (sender/receiver)
│   ├── FriendsRecyclerAdapter.kt   # Friend list adapter
│   └── UsersRecyclerAdapter.kt     # Search results adapter
│
└── models/                         # Data classes
    ├── User.kt                     # User account representation
    ├── Message.kt                  # Message payload model
    ├── Friend.kt                   # Friendship relation model
    └── Data.kt                     # Utility & constant mappings
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Iguana | 2023.2.1 or newer
- **JDK**: Version 17 or higher
- **Android Device / Emulator**: Running Android API 24 (Nougat) or higher

### Installation & Firebase Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ZinebBouakkiz/ChatApp.git
   cd ChatApp
   ```

2. **Configure Firebase**:
   - Create a new project in the [Firebase Console](https://console.firebase.google.com/).
   - Add an **Android App** with package name `com.zinebbouakkiz.chatapp`.
   - Enable **Firebase Authentication** (Email/Password provider).
   - Enable **Cloud Firestore** and set appropriate read/write rules.
   - Enable **Firebase Cloud Storage** for profile photos.
   - Download your `google-services.json` file from Firebase Console and copy it to:
     ```
     app/google-services.json
     ```
     *(A template reference is provided in `app/google-services.json.example`)*.

3. **Build and Run**:
   - Open the project in Android Studio.
   - Let Gradle sync all dependencies.
   - Select your emulator or connected device and click **Run (Shift + F10)**.

---

## 🔒 Security & Privacy Notice

Credentials, private tokens, and local build files are strictly excluded via `.gitignore`. When setting up your Firestore database in production, ensure you deploy strict security rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read, write: if request.auth != null;
    }
    match /chats/{chatId}/messages/{messageId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 👩‍💻 Author

**Zineb Bouakkiz**  
*Software Engineering Graduate (Master's Degree)*  
- GitHub: [@ZinebBouakkiz](https://github.com/ZinebBouakkiz)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
