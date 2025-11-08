# Setup Guide - LansonesScanApp

This guide will help you set up and run the LansonesScanApp on your development machine.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: Version 11 or higher
- **Android SDK**: API Level 24 (Android 7.0) or higher
- **Git**: For cloning the repository

## Step-by-Step Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd LansonesScanAppv1
```

### 2. Obtain a Gemini API Key

The app requires a Google Gemini API key to function. Follow these steps:

1. **Visit Google AI Studio**
   - Go to [https://makersuite.google.com/app/apikey](https://makersuite.google.com/app/apikey)
   - Sign in with your Google account

2. **Create a New API Key**
   - Click "Create API Key"
   - Select a Google Cloud project (or create a new one)
   - Copy the generated API key

3. **Enable the Generative AI API**
   - Go to [Google Cloud Console](https://console.cloud.google.com/)
   - Navigate to "APIs & Services" > "Library"
   - Search for "Generative Language API"
   - Click "Enable"

### 3. Configure the API Key

**Option A: Using gradle.properties (Recommended)**

1. Open or create `gradle.properties` in the project root directory
2. Add the following line:
   ```properties
   GEMINI_API_KEY=your_actual_api_key_here
   ```
3. Save the file

**Option B: Using local.properties**

1. Open `local.properties` in the project root
2. Add the following line:
   ```properties
   GEMINI_API_KEY=your_actual_api_key_here
   ```
3. Update `build.gradle.kts` to read from `local.properties` instead

**Important Security Notes:**
- ✅ Both `gradle.properties` and `local.properties` are in `.gitignore`
- ❌ Never commit your API key to version control
- ❌ Never hardcode the API key in source files
- ✅ Use environment variables in CI/CD pipelines

### 4. Sync Gradle Files

1. Open the project in Android Studio
2. Click "Sync Project with Gradle Files" or press `Ctrl+Shift+O` (Windows/Linux) / `Cmd+Shift+O` (Mac)
3. Wait for the sync to complete

### 5. Build the Project

**Using Android Studio:**
1. Click "Build" > "Make Project" or press `Ctrl+F9` (Windows/Linux) / `Cmd+F9` (Mac)
2. Wait for the build to complete

**Using Command Line:**
```bash
# Windows
.\gradlew build

# Mac/Linux
./gradlew build
```

### 6. Run the App

**On a Physical Device:**
1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect your device via USB
4. Click "Run" in Android Studio or press `Shift+F10`
5. Select your device from the list

**On an Emulator:**
1. Open AVD Manager in Android Studio
2. Create a new virtual device (API 24 or higher recommended)
3. Start the emulator
4. Click "Run" in Android Studio
5. Select the emulator from the list

**Using Command Line:**
```bash
# Install on connected device
.\gradlew installDebug

# Run on device
adb shell am start -n dev.ml.lansonesscanapp/.MainActivity
```

## Verifying the Setup

### 1. Check API Key Configuration

After building, verify the API key is properly configured:

1. Open `app/build/generated/source/buildConfig/debug/dev/ml/lansonesscanapp/BuildConfig.java`
2. Look for the `GEMINI_API_KEY` field
3. Ensure it contains your API key (not an empty string)

### 2. Test the App

1. Launch the app
2. Tap "Scan Fruit or Leaf"
3. Select "Fruit" mode
4. Take a photo or select an image from gallery
5. Tap "Analyze Image"
6. Verify that the analysis completes successfully

If you see an error like "API key not valid", double-check your API key configuration.

## Common Issues and Solutions

### Issue 1: Build Fails with "GEMINI_API_KEY not found"

**Solution:**
- Ensure `gradle.properties` exists in the project root
- Verify the API key line is correctly formatted: `GEMINI_API_KEY=your_key`
- Try invalidating caches: File > Invalidate Caches > Invalidate and Restart

### Issue 2: "API key not valid" Error

**Solution:**
- Verify the API key is correct (no extra spaces or quotes)
- Check that the Generative Language API is enabled in Google Cloud Console
- Ensure your Google Cloud project has billing enabled (if required)
- Try generating a new API key

### Issue 3: Camera Permission Denied

**Solution:**
- Go to device Settings > Apps > LansonesScanApp > Permissions
- Enable Camera permission
- Restart the app

### Issue 4: Image Analysis Fails

**Solution:**
- Check internet connection
- Verify API key is valid
- Ensure image is not corrupted
- Try with a different image
- Check Logcat for detailed error messages

### Issue 5: Gradle Sync Fails

**Solution:**
```bash
# Clean the project
.\gradlew clean

# Rebuild
.\gradlew build --refresh-dependencies
```

### Issue 6: KSP Annotation Processing Errors

**Solution:**
- Ensure KSP plugin version matches Kotlin version
- Update to latest stable versions
- Clean and rebuild project

## Development Tips

### Enable Logging

Add logging to track API calls:

```kotlin
// In GeminiClient.kt
private val TAG = "GeminiClient"

suspend fun analyzeImage(imageUri: Uri, mode: ScanMode): Result<String> {
    Log.d(TAG, "Starting analysis for mode: $mode")
    // ... rest of the code
}
```

### Debug Mode

To enable verbose logging in debug builds:

```kotlin
// In build.gradle.kts
buildTypes {
    debug {
        buildConfigField("boolean", "DEBUG_MODE", "true")
    }
    release {
        buildConfigField("boolean", "DEBUG_MODE", "false")
    }
}
```

### Testing with Mock Data

For testing without API calls, create a mock GeminiClient:

```kotlin
class MockGeminiClient : GeminiClient {
    override suspend fun analyzeImage(imageUri: Uri, mode: ScanMode): Result<String> {
        return Result.success("""
            {
                "variety": "Lonkong",
                "confidence": 85.0,
                "ripeness": "ripe",
                "defects": [],
                "notes": "Mock data for testing"
            }
        """)
    }
}
```

## Environment Variables for CI/CD

### GitHub Actions Example

```yaml
name: Android CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Create gradle.properties
      run: |
        echo "GEMINI_API_KEY=${{ secrets.GEMINI_API_KEY }}" > gradle.properties
    
    - name: Build with Gradle
      run: ./gradlew build
```

### GitLab CI Example

```yaml
build:
  image: openjdk:11-jdk
  before_script:
    - echo "GEMINI_API_KEY=${GEMINI_API_KEY}" > gradle.properties
  script:
    - ./gradlew build
  variables:
    GEMINI_API_KEY: $GEMINI_API_KEY
```

## Project Configuration Files

### gradle.properties Template

```properties
# Project-wide Gradle settings
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true

# AndroidX package structure
android.useAndroidX=true
android.enableJetifier=true

# Kotlin code style
kotlin.code.style=official

# API Keys (DO NOT COMMIT)
GEMINI_API_KEY=your_api_key_here
```

### local.properties Template

```properties
# Location of the Android SDK
sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk

# API Keys (DO NOT COMMIT)
GEMINI_API_KEY=your_api_key_here
```

## Next Steps

After successful setup:

1. **Explore the Code**: Review the architecture and code structure
2. **Run Tests**: Execute unit and integration tests
3. **Customize UI**: Modify themes and colors in `ui/theme/`
4. **Add Features**: Extend functionality based on your needs
5. **Deploy**: Build a release APK for distribution

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Gemini API Documentation](https://ai.google.dev/docs)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)

## Support

If you encounter issues not covered in this guide:

1. Check the [README.md](README.md) for general information
2. Review the [API_REFERENCE.md](API_REFERENCE.md) for API details
3. Search existing GitHub issues
4. Create a new issue with:
   - Android Studio version
   - Device/emulator details
   - Error logs from Logcat
   - Steps to reproduce

---

**Happy Coding!** 🚀
