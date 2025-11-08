# Quick Start Guide - LansonesScanApp

Get the app running in 5 minutes!

## 🚀 Fast Track Setup

### Step 1: Get API Key (2 minutes)

1. Visit: https://makersuite.google.com/app/apikey
2. Click "Create API Key"
3. Copy the key

### Step 2: Configure Project (1 minute)

```bash
# Copy the template
cp gradle.properties.example gradle.properties

# Edit gradle.properties and replace with your key
# GEMINI_API_KEY=your_actual_key_here
```

### Step 3: Build & Run (2 minutes)

**Windows:**
```bash
.\gradlew build
.\gradlew installDebug
```

**Mac/Linux:**
```bash
./gradlew build
./gradlew installDebug
```

## ✅ Verify It Works

1. Open the app on your device/emulator
2. Tap "Scan Fruit or Leaf"
3. Select "Fruit" mode
4. Take a photo or choose from gallery
5. Tap "Analyze Image"
6. See results in 2-3 seconds!

## 🐛 Quick Troubleshooting

### "API key not valid"
- Check `gradle.properties` has correct key
- No quotes around the key
- No extra spaces

### "Build failed"
- Run: `.\gradlew clean build`
- Check internet connection
- Ensure JDK 11+ installed

### "Camera not working"
- Grant camera permission in app settings
- Test on physical device (emulator cameras can be unreliable)

### "Analysis fails"
- Check internet connection
- Verify API key is active
- Try with a different image

## 📱 First Scan Tutorial

### Scanning a Fruit

1. **Launch App** → Tap "Scan Fruit or Leaf"
2. **Select Mode** → Tap "Fruit" chip
3. **Get Image**:
   - Tap "Camera" → Take photo of lansones fruit
   - OR Tap "Gallery" → Select existing photo
4. **Analyze** → Tap "Analyze Image" button
5. **View Results**:
   - Variety (e.g., Lonkong)
   - Confidence score
   - Ripeness level
   - Any defects
   - Expert notes

### Scanning a Leaf

1. **Launch App** → Tap "Scan Fruit or Leaf"
2. **Select Mode** → Tap "Leaf" chip
3. **Get Image**:
   - Tap "Camera" → Take photo of lansones leaf
   - OR Tap "Gallery" → Select existing photo
4. **Analyze** → Tap "Analyze Image" button
5. **View Results**:
   - Disease diagnosis
   - Severity level
   - Treatment recommendations
   - Product suggestions
   - Prevention tips

### Viewing History

1. **Launch App** → Tap "View History"
2. **Browse Scans** → Scroll through past results
3. **Expand Details** → Tap any scan to see full info
4. **Delete** → Tap trash icon to remove a scan
5. **Clear All** → Tap top-right icon to clear history

## 💡 Tips for Best Results

### For Fruit Scanning
- ✅ Use good lighting (natural light is best)
- ✅ Focus on the fruit (not too far away)
- ✅ Show the whole fruit if possible
- ✅ Clean lens before taking photo
- ❌ Avoid blurry images
- ❌ Avoid very dark/bright images

### For Leaf Scanning
- ✅ Show the entire leaf
- ✅ Capture both healthy and affected areas
- ✅ Use natural lighting
- ✅ Get close-ups of spots/discoloration
- ❌ Avoid shadows on the leaf
- ❌ Avoid wet/shiny leaves (glare)

## 🎯 Common Use Cases

### Use Case 1: Market Vendor
**Goal**: Identify fruit variety before buying

```
1. At market → See lansones
2. Open app → Scan Fruit
3. Take photo → Analyze
4. Check variety & quality
5. Make informed purchase
```

### Use Case 2: Farmer
**Goal**: Diagnose leaf disease

```
1. Notice sick leaves → Open app
2. Scan Leaf → Take photo
3. Get diagnosis → Read treatment
4. Apply recommended solution
5. Monitor improvement
```

### Use Case 3: Student
**Goal**: Learn about lansones varieties

```
1. Collect samples → Scan each
2. Compare results → Note differences
3. Review history → Study patterns
4. Build knowledge → Share findings
```

## 📊 What to Expect

### Fruit Analysis Results

**Example Output:**
```
Variety: Lonkong (85% confidence)
Ripeness: ripe
Defects: minor bruising on skin
Notes: High-quality fruit with characteristic 
       translucent flesh. Good for immediate 
       consumption or short-term storage.
```

### Leaf Analysis Results

**Example Output:**
```
Diagnosis: Leaf spot disease
Severity: moderate
Treatment:
• Remove affected leaves
• Apply fungicide spray
• Improve air circulation

Products:
• Fungicide: Copper-based fungicide
• Organic: Neem oil spray

Prevention:
• Avoid overhead watering
• Maintain proper spacing
• Remove fallen leaves regularly
```

## 🔧 Advanced Features

### Switching Modes
- Tap the mode chips at top of Analysis screen
- Switch between Fruit and Leaf anytime
- Each mode uses different AI prompts

### Managing History
- Tap any history item to expand
- Long-press for additional options (future)
- Delete individual items or clear all
- History persists after app restart

### Retaking Photos
- Select new image to replace current
- Previous image is discarded
- No limit on retakes

## 📚 Learn More

- **Full Documentation**: See [README.md](README.md)
- **Detailed Setup**: See [SETUP_GUIDE.md](SETUP_GUIDE.md)
- **API Details**: See [API_REFERENCE.md](API_REFERENCE.md)
- **AI Prompts**: See [PROMPTS.md](PROMPTS.md)

## 🆘 Need Help?

### Quick Fixes

**App won't build?**
```bash
.\gradlew clean
.\gradlew build --refresh-dependencies
```

**API not working?**
1. Check internet connection
2. Verify API key in gradle.properties
3. Restart Android Studio
4. Rebuild project

**Camera issues?**
1. Check app permissions in device settings
2. Grant camera permission
3. Restart app

### Get Support

1. Check [SETUP_GUIDE.md](SETUP_GUIDE.md) troubleshooting section
2. Review [DEPLOYMENT_CHECKLIST.md](DEPLOYMENT_CHECKLIST.md)
3. Search GitHub issues
4. Create new issue with details

## 🎓 Next Steps

Once you're comfortable with the basics:

1. **Explore Code**: Review the architecture
2. **Customize UI**: Change colors and themes
3. **Add Features**: Extend functionality
4. **Contribute**: Submit improvements
5. **Share**: Help others get started

## 📝 Checklist

Before you start using the app:

- [ ] API key configured
- [ ] App builds successfully
- [ ] App installed on device
- [ ] Camera permission granted
- [ ] Internet connection active
- [ ] Test scan completed

## 🌟 Pro Tips

1. **Save Good Examples**: Keep scans of perfect specimens
2. **Compare Results**: Use history to track changes
3. **Share Knowledge**: Show results to other farmers
4. **Regular Updates**: Check for app updates
5. **Provide Feedback**: Report issues and suggestions

---

## 🎉 You're Ready!

The app is now set up and ready to use. Start scanning lansones fruits and leaves to get instant AI-powered analysis!

**Happy Scanning!** 🍇🌿

---

**Quick Start Version**: 1.0  
**Last Updated**: November 2024  
**Estimated Setup Time**: 5 minutes
