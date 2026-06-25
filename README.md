# Score Calculator & Answer Sheet App

A fast, lightweight answer sheet and score calculator app built for school use. Quickly generate answer sheets, mark correct options, and calculate final scores with ease. 

## Features
- **Customizable Sheets:** Generate answer sheets with any number of questions (e.g., 20, 40) and options per question (2-6).
- **Multiple Label Styles:** Choose between standard ABCD, 1234, or True/False options.
- **Fast Correction Mode:** Quickly mark the correct answers (key) and the user's selected answers, and the app will instantly compare them.
- **Instant Result Calculation:** Get instant feedback on the number of correct, wrong, and unanswered questions, along with the final percentage score.
- **Local Persistence:** Data is securely saved locally using Room Database, allowing you to resume grading at any time.

## Architecture
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room Database for local storage
- **Navigation:** Navigation Compose

## Build & CI
This project includes a GitHub Actions workflow that automatically builds the app whenever code is pushed.
It generates:
- Debug APKs
- Release APKs (unsigned)
- Split APKs for various ABIs (`armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`)
- Universal APK
