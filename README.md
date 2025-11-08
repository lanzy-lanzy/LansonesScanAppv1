# LansonesScanApp

A Kotlin Android application using Jetpack Compose and the Gemini API to scan **lansones fruits and leaves** for analysis.

## Features

### 🍇 Fruit Scanning
- Identify lansones fruit varieties (Lonkong, Duco, Paete, etc.)
- Assess ripeness level (unripe, ripe, overripe, damaged)
- Detect visible defects (bruising, insect holes, discoloration)
- Get confidence scores and expert notes

### 🌿 Leaf Disease Detection
- Diagnose common lansones leaf diseases
- Assess severity levels (low, moderate, high)
- Receive treatment recommendations
- Get product suggestions and cultural recommendations
- Identify nutrient deficiencies

### 📜 Scan History
- View all past scans with thumbnails
- Access detailed analysis results
- Delete individual scans or clear all history
- Organized by date and time

## Technology Stack

- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room for local persistence
- **AI/ML**: Google Gemini API for image analysis
- **Image Handling**: Coil for image loading, CameraX for camera capture
- **Navigation**: Jetpack Navigation Compose
- **Async Operations**: Kotlin Coroutines & Flow

## Project Structure

```
app/
├── data/
│   ├── AppDatabase.kt          # Room database
│   ├── ScanResultDao.kt        # Data access object
│   └── Converters.kt           # Type converters
├── model/
│   ├── ScanMode.kt             # Enum for scan types
│   ├── ScanResult.kt           # Main data model
│   ├── FruitAnalysisResult.kt  # Fruit analysis response
│   └── LeafAnalysisResult.kt   # Leaf analysis response
├── network/
│   └── GeminiClient.kt         # Gemini API integration
├── viewmodel/
│   ├── AnalysisViewModel.kt    # Analysis screen logic
│   └── HistoryViewModel.kt     # History screen logic
├── ui/
│   └── screens/
│       ├── DashboardScreen.kt  # Home screen
│       ├── AnalysisScreen.kt   # Scanning interface
│       └── HistoryScreen.kt    # History display
├── navigation/
│   └── NavGraph.kt             # Navigation setup
└── MainActivity.kt             # Entry point
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 24 or higher
- Google Gemini API key

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd LansonesScanAppv1
   ```

2. **Get a Gemini API Key**
   - Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Create a new API key
   - Copy the key

3. **Configure API Key**
   
   Create or edit `gradle.properties` in the project root:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
   
   **Important**: Never commit your API key to version control. Add `gradle.properties` to `.gitignore`.

4. **Build the project**
   ```bash
   ./gradlew build
   ```

5. **Run on device or emulator**
   - Connect an Android device or start an emulator
   - Click Run in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## Usage

### Scanning a Fruit
1. Open the app and tap "Scan Fruit or Leaf"
2. Select "Fruit" mode
3. Take a photo or choose from gallery
4. Tap "Analyze Image"
5. View the variety, ripeness, and defect analysis

### Scanning a Leaf
1. Open the app and tap "Scan Fruit or Leaf"
2. Select "Leaf" mode
3. Take a photo or choose from gallery
4. Tap "Analyze Image"
5. View diagnosis, severity, and treatment recommendations

### Viewing History
1. From the dashboard, tap "View History"
2. Tap any scan to expand details
3. Delete individual scans or clear all history

## Gemini Prompts

### Fruit Identification Prompt
The app uses a specialized prompt to identify lansones varieties:
- Analyzes variety (Lonkong, Duco, Paete, Unknown)
- Provides confidence score (0-100)
- Assesses ripeness level
- Detects defects
- Includes expert notes

### Leaf Disease Detection Prompt
The app uses a comprehensive prompt for disease diagnosis:
- Lists possible diagnoses with confidence scores
- Identifies primary diagnosis
- Assesses severity level
- Recommends treatments and products
- Provides cultural recommendations
- Identifies additional information needed

## Permissions

The app requires the following permissions:
- **CAMERA**: To capture photos of fruits and leaves
- **READ_MEDIA_IMAGES**: To select images from gallery (Android 13+)
- **READ_EXTERNAL_STORAGE**: To select images from gallery (Android 12 and below)
- **INTERNET**: To communicate with Gemini API

## Security Considerations

- ✅ API key stored in `gradle.properties` (not in source code)
- ✅ API key loaded via BuildConfig
- ✅ Images processed locally before upload
- ✅ No personal data collected
- ⚠️ Consider implementing backend proxy for production

## Future Enhancements

- [ ] Multi-fruit support (other tropical fruits)
- [ ] Offline mode with cached results
- [ ] Export scan history as PDF/CSV
- [ ] Share results via social media
- [ ] Soil condition analysis
- [ ] Weather integration for farming advice
- [ ] Multi-language support
- [ ] Cloud sync across devices

## Dependencies

Key dependencies used in this project:

```kotlin
// Gemini AI
implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.4")

// CameraX
implementation("androidx.camera:camera-camera2:1.4.0")
implementation("androidx.camera:camera-lifecycle:1.4.0")
implementation("androidx.camera:camera-view:1.4.0")

// Image Loading
implementation("io.coil-kt:coil-compose:2.7.0")

// Permissions
implementation("com.google.accompanist:accompanist-permissions:0.36.0")
```

## Troubleshooting

### Build Errors
- Ensure you have the latest Android Studio version
- Sync Gradle files
- Clean and rebuild: `./gradlew clean build`

### API Key Issues
- Verify the API key is correctly set in `gradle.properties`
- Check that BuildConfig is enabled in `build.gradle.kts`
- Ensure the API key has proper permissions in Google Cloud Console

### Camera Not Working
- Check that camera permissions are granted
- Verify device has a camera
- Test on a physical device (emulator cameras can be unreliable)

### Analysis Fails
- Check internet connection
- Verify API key is valid
- Ensure image is clear and well-lit
- Check Gemini API quota limits

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Google Gemini API for AI-powered image analysis
- Jetpack Compose for modern Android UI
- The lansones farming community for domain expertise

## Contact

For questions or support, please open an issue on GitHub.

---

**Note**: This app is designed for educational and agricultural assistance purposes. Always consult with agricultural experts for critical farming decisions.
