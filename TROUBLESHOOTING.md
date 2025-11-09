# Troubleshooting Guide

## API Error: "Invalid JSON payload received"

### Error Details
```
ApiError[code=400, message=API request failed with code 400: {
  "error": {
    "code": 400,
    "message": "Invalid JSON payload received. Expected , or } after key:value pair.\ning structure:\n{\n  \"name\"\n    ^",
    "status": "INVALID_ARGUMENT"
  }
}
```

### Possible Causes & Solutions

#### 1. Model Name Issue
**Problem:** The model name might not be correct or available.

**Solution:**
- Changed from `gemini-2.5-pro` to `gemini-1.5-flash`
- `gemini-1.5-flash` is the stable, fast model for production use

**Verify in:** `app/src/main/java/dev/ml/lansonesscanapp/network/GeminiClient.kt`
```kotlin
private val model = GenerativeModel(
    modelName = "gemini-1.5-flash",  // ✅ Correct
    apiKey = BuildConfig.GEMINI_API_KEY
)
```

#### 2. API Key Issues
**Problem:** Invalid or missing API key.

**Check:**
1. Open `gradle.properties` file
2. Verify `GEMINI_API_KEY` is set correctly
3. Format should be: `GEMINI_API_KEY=your_actual_api_key_here`

**Get a new API key:**
1. Go to https://makersuite.google.com/app/apikey
2. Create a new API key
3. Copy and paste into `gradle.properties`

**Test API key:**
```bash
# Clean and rebuild
./gradlew clean
./gradlew build
```

#### 3. SDK Version Compatibility
**Problem:** Outdated Gemini SDK version.

**Current version:** 0.9.0

**Check for updates:**
- Visit: https://github.com/google/generative-ai-android
- Update in `app/build.gradle.kts` if needed

#### 4. Network/Firewall Issues
**Problem:** Network blocking API requests.

**Solutions:**
- Check internet connection
- Try on different network (WiFi vs mobile data)
- Check if firewall is blocking Google AI APIs
- Verify VPN isn't interfering

#### 5. API Quota Exceeded
**Problem:** Too many requests in short time.

**Solutions:**
- Wait a few minutes and try again
- Check quota at: https://console.cloud.google.com/
- Upgrade to paid tier if needed

### Quick Fix Steps

#### Step 1: Verify Model Name
```kotlin
// In GeminiClient.kt
private val model = GenerativeModel(
    modelName = "gemini-1.5-flash",  // Use this
    apiKey = BuildConfig.GEMINI_API_KEY
)
```

#### Step 2: Check API Key
```properties
# In gradle.properties
GEMINI_API_KEY=AIzaSy...your_key_here
```

#### Step 3: Clean and Rebuild
```bash
./gradlew clean
./gradlew build
./gradlew installDebug
```

#### Step 4: Test with Simple Image
- Use a clear, well-lit image
- Try both camera and gallery
- Check logcat for detailed errors

### Debugging

#### Enable Detailed Logging
Check Android Studio Logcat for:
```
D/GeminiClient: Image hash: ...
D/GeminiClient: Cache miss. Calling API...
E/GeminiClient: Gemini API error: ...
```

#### Common Log Messages

**Success:**
```
D/GeminiClient: Raw Gemini response: DIAGNOSIS: ...
D/GeminiClient: Result cached successfully
```

**API Key Error:**
```
E/GeminiClient: Gemini API error: API key not valid
```

**Quota Error:**
```
E/GeminiClient: Gemini API error: quota exceeded
```

**Model Error:**
```
E/GeminiClient: Gemini API error: model not found
```

### Verification Checklist

- [ ] Model name is `gemini-1.5-flash`
- [ ] API key is set in `gradle.properties`
- [ ] API key is valid (test at https://makersuite.google.com/)
- [ ] Internet connection is working
- [ ] App has internet permission in AndroidManifest.xml
- [ ] Clean build completed successfully
- [ ] No firewall/VPN blocking requests

### Testing API Key

#### Method 1: Test in Browser
1. Go to https://makersuite.google.com/
2. Try generating content with your API key
3. If it works there, it should work in app

#### Method 2: Test with cURL
```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"contents":[{"parts":[{"text":"Hello"}]}]}' \
  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=YOUR_API_KEY"
```

### Alternative Models

If `gemini-1.5-flash` doesn't work, try:

```kotlin
// Option 1: Flash (fastest)
modelName = "gemini-1.5-flash"

// Option 2: Pro (more capable, slower)
modelName = "gemini-1.5-pro"

// Option 3: Pro Vision (for images)
modelName = "gemini-pro-vision"
```

### Error Handling Improvements

The app now provides better error messages:

```kotlin
catch (e: GoogleGenerativeAIException) {
    val errorMessage = when {
        e.message?.contains("API key") == true -> 
            "Invalid API key. Please check your Gemini API key."
        e.message?.contains("quota") == true -> 
            "API quota exceeded. Please try again later."
        e.message?.contains("400") == true -> 
            "Invalid request. The model may not support this operation."
        else -> "API error: ${e.message}"
    }
    Result.failure(Exception(errorMessage, e))
}
```

### Still Having Issues?

#### 1. Check Gemini API Status
- Visit: https://status.cloud.google.com/
- Check if Gemini AI services are operational

#### 2. Verify Permissions
In `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### 3. Check ProGuard Rules
If using ProGuard/R8, add:
```
-keep class com.google.ai.client.generativeai.** { *; }
```

#### 4. Update Dependencies
```bash
./gradlew app:dependencies
```
Check for conflicts or outdated versions.

#### 5. Create New API Key
Sometimes API keys get corrupted or restricted:
1. Go to https://makersuite.google.com/app/apikey
2. Delete old key
3. Create new key
4. Update `gradle.properties`
5. Clean and rebuild

### Contact Support

If none of these solutions work:

1. **Google AI Support:**
   - https://developers.google.com/generative-ai/support

2. **GitHub Issues:**
   - https://github.com/google/generative-ai-android/issues

3. **Stack Overflow:**
   - Tag: `google-gemini-api`

### Success Indicators

When everything is working correctly, you should see:

1. **First Analysis (3-5 seconds):**
   ```
   D/GeminiClient: Image hash: abc123...
   D/GeminiClient: Cache miss. Calling API...
   D/GeminiClient: Raw Gemini response: DIAGNOSIS: ...
   D/GeminiClient: Result cached successfully
   ```

2. **Cached Analysis (<0.1 seconds):**
   ```
   D/GeminiClient: Image hash: abc123...
   D/GeminiClient: Cache hit! Returning cached result
   ```

3. **UI Shows:**
   - Analysis result card with diagnosis
   - No error messages
   - Result saved to history

---

**Last Updated:** Based on current implementation  
**Model Used:** gemini-1.5-flash  
**SDK Version:** 0.9.0
