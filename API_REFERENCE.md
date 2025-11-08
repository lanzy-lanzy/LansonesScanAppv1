# API Reference - LansonesScanApp

## Gemini API Integration

This document provides detailed information about the Gemini API integration in LansonesScanApp.

## GeminiClient

**Location**: `dev.ml.lansonesscanapp.network.GeminiClient`

The main client for interacting with Google's Gemini API.

### Constructor

```kotlin
GeminiClient(context: Context)
```

**Parameters**:
- `context`: Android application context for accessing resources and content resolver

### Methods

#### analyzeImage

```kotlin
suspend fun analyzeImage(imageUri: Uri, mode: ScanMode): Result<String>
```

Analyzes an image using the Gemini API based on the specified scan mode.

**Parameters**:
- `imageUri`: URI of the image to analyze
- `mode`: Either `ScanMode.FRUIT` or `ScanMode.LEAF`

**Returns**: `Result<String>` containing the JSON response or error

**Example**:
```kotlin
val geminiClient = GeminiClient(context)
val result = geminiClient.analyzeImage(imageUri, ScanMode.FRUIT)

result.fold(
    onSuccess = { jsonResponse -> 
        // Handle successful response
    },
    onFailure = { exception -> 
        // Handle error
    }
)
```

#### parseFruitResult

```kotlin
fun parseFruitResult(jsonString: String): FruitAnalysisResult?
```

Parses the Gemini API JSON response for fruit analysis.

**Parameters**:
- `jsonString`: JSON string from Gemini API

**Returns**: `FruitAnalysisResult` object or `null` if parsing fails

#### parseLeafResult

```kotlin
fun parseLeafResult(jsonString: String): LeafAnalysisResult?
```

Parses the Gemini API JSON response for leaf analysis.

**Parameters**:
- `jsonString`: JSON string from Gemini API

**Returns**: `LeafAnalysisResult` object or `null` if parsing fails

---

## Gemini Prompts

### Fruit Identification Prompt

**Purpose**: Identify lansones fruit variety and assess quality

**Prompt Template**:
```
You are a tropical fruit expert specializing in lansones (lanzones) fruit identification.

Analyze the uploaded image of a lansones fruit and respond ONLY in valid JSON format with the following structure:
{
  "variety": "string",
  "confidence": number,
  "ripeness": "string",
  "defects": ["string"],
  "notes": "string"
}

Guidelines:
- variety: Identify the variety (e.g., "Lonkong", "Duco", "Paete", "Unknown")
- confidence: Provide a confidence score from 0 to 100
- ripeness: Classify as "unripe", "ripe", "overripe", or "damaged"
- defects: List any visible defects such as bruising, insect holes, discoloration
- notes: Provide additional remarks or suggestions

If you cannot identify the variety with certainty, set variety to "Unknown" and explain why in the notes field.

Respond ONLY with valid JSON, no additional text.
```

**Expected Response Format**:
```json
{
  "variety": "Lonkong",
  "confidence": 85,
  "ripeness": "ripe",
  "defects": ["minor bruising on skin"],
  "notes": "High-quality fruit with characteristic translucent flesh"
}
```

**Common Varieties**:
- **Lonkong**: Most popular variety, sweet with minimal seeds
- **Duco**: Larger fruits, slightly sour
- **Paete**: Medium-sized, balanced sweetness
- **Unknown**: When variety cannot be determined

**Ripeness Levels**:
- `unripe`: Green or yellowish, firm
- `ripe`: Golden yellow, slightly soft
- `overripe`: Dark yellow/brown, very soft
- `damaged`: Visible damage or decay

---

### Leaf Disease Detection Prompt

**Purpose**: Diagnose lansones leaf diseases and provide treatment recommendations

**Prompt Template**:
```
You are a plant pathologist specializing in tropical crops, particularly lansones (lanzones) trees.

Analyze the uploaded image of a lansones leaf and respond ONLY in valid JSON format with the following structure:
{
  "diagnosis": [
    {
      "name": "string",
      "confidence": number
    }
  ],
  "primary_diagnosis": "string",
  "severity": "string",
  "recommended_treatment": ["string"],
  "recommended_products": [
    {
      "type": "string",
      "example": "string"
    }
  ],
  "cultural_recommendations": ["string"],
  "additional_info_needed": "string"
}

Guidelines:
- diagnosis: List all possible diseases/conditions with confidence scores (0-100)
- primary_diagnosis: The most likely diagnosis
- severity: Classify as "low", "moderate", or "high"
- recommended_treatment: Provide actionable, short treatment steps
- recommended_products: Suggest product types and examples (e.g., fungicides, pesticides)
- cultural_recommendations: Provide farming best practices to prevent recurrence
- additional_info_needed: If uncertain, specify what additional information would help

Common lansones leaf diseases include:
- Leaf spot
- Powdery mildew
- Anthracnose
- Bacterial blight
- Nutrient deficiency

If you cannot make a confident diagnosis, indicate "uncertain" as primary_diagnosis and list what additional information is needed.

Respond ONLY with valid JSON, no additional text.
```

**Expected Response Format**:
```json
{
  "diagnosis": [
    {
      "name": "Leaf spot disease",
      "confidence": 75
    },
    {
      "name": "Fungal infection",
      "confidence": 60
    }
  ],
  "primary_diagnosis": "Leaf spot disease",
  "severity": "moderate",
  "recommended_treatment": [
    "Remove affected leaves",
    "Apply fungicide spray",
    "Improve air circulation"
  ],
  "recommended_products": [
    {
      "type": "Fungicide",
      "example": "Copper-based fungicide"
    },
    {
      "type": "Organic treatment",
      "example": "Neem oil spray"
    }
  ],
  "cultural_recommendations": [
    "Avoid overhead watering",
    "Maintain proper spacing between trees",
    "Remove fallen leaves regularly"
  ],
  "additional_info_needed": ""
}
```

**Common Diseases**:
- **Leaf Spot**: Circular brown/black spots on leaves
- **Powdery Mildew**: White powdery coating on leaves
- **Anthracnose**: Dark lesions, leaf curling
- **Bacterial Blight**: Water-soaked lesions, yellowing
- **Nutrient Deficiency**: Yellowing, stunted growth

**Severity Levels**:
- `low`: Minor symptoms, limited spread
- `moderate`: Noticeable symptoms, spreading
- `high`: Severe symptoms, tree health at risk

---

## Data Models

### FruitAnalysisResult

```kotlin
@Serializable
data class FruitAnalysisResult(
    val variety: String,
    val confidence: Double,
    val ripeness: String,
    val defects: List<String> = emptyList(),
    val notes: String = ""
)
```

### LeafAnalysisResult

```kotlin
@Serializable
data class LeafAnalysisResult(
    val diagnosis: List<Diagnosis> = emptyList(),
    val primary_diagnosis: String,
    val severity: String,
    val recommended_treatment: List<String> = emptyList(),
    val recommended_products: List<Product> = emptyList(),
    val cultural_recommendations: List<String> = emptyList(),
    val additional_info_needed: String = ""
)

@Serializable
data class Diagnosis(
    val name: String,
    val confidence: Double
)

@Serializable
data class Product(
    val type: String,
    val example: String
)
```

### ScanResult

```kotlin
@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey
    val id: String,
    val mode: ScanMode,
    val imageUri: String,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
```

---

## Error Handling

### Common Errors

#### API Key Issues
```kotlin
// Error: Invalid API key
Exception: API key not valid. Please pass a valid API key.

// Solution: Check gradle.properties has correct GEMINI_API_KEY
```

#### Network Errors
```kotlin
// Error: Network timeout
Exception: Failed to connect to Gemini API

// Solution: Check internet connection, retry request
```

#### Image Loading Errors
```kotlin
// Error: Failed to load image
Exception: Failed to load image from URI

// Solution: Verify URI is valid and accessible
```

#### JSON Parsing Errors
```kotlin
// Error: Invalid JSON response
Exception: kotlinx.serialization.SerializationException

// Solution: Check Gemini response format, may need to clean response
```

### Error Handling Example

```kotlin
viewModelScope.launch {
    val result = geminiClient.analyzeImage(imageUri, mode)
    
    result.fold(
        onSuccess = { jsonResponse ->
            val scanResult = when (mode) {
                ScanMode.FRUIT -> {
                    geminiClient.parseFruitResult(jsonResponse)?.let {
                        // Process fruit result
                    }
                }
                ScanMode.LEAF -> {
                    geminiClient.parseLeafResult(jsonResponse)?.let {
                        // Process leaf result
                    }
                }
            }
            
            if (scanResult == null) {
                // Handle parsing failure
                _uiState.value = _uiState.value.copy(
                    error = "Failed to parse analysis result"
                )
            }
        },
        onFailure = { exception ->
            // Handle API failure
            _uiState.value = _uiState.value.copy(
                error = exception.message ?: "Analysis failed"
            )
        }
    )
}
```

---

## Rate Limits and Quotas

### Gemini API Limits

- **Free Tier**: 60 requests per minute
- **Image Size**: Max 20MB per image
- **Response Time**: Typically 2-5 seconds

### Best Practices

1. **Image Optimization**: Resize images before upload to reduce processing time
2. **Error Retry**: Implement exponential backoff for failed requests
3. **Caching**: Store results locally to avoid redundant API calls
4. **User Feedback**: Show loading indicators during API calls

---

## Testing

### Unit Testing GeminiClient

```kotlin
@Test
fun `parseFruitResult returns valid object for correct JSON`() {
    val json = """
        {
            "variety": "Lonkong",
            "confidence": 85.0,
            "ripeness": "ripe",
            "defects": [],
            "notes": "High quality"
        }
    """.trimIndent()
    
    val result = geminiClient.parseFruitResult(json)
    
    assertNotNull(result)
    assertEquals("Lonkong", result?.variety)
    assertEquals(85.0, result?.confidence)
}
```

### Integration Testing

```kotlin
@Test
fun `analyzeImage returns success for valid image`() = runTest {
    val mockUri = Uri.parse("content://test/image.jpg")
    
    val result = geminiClient.analyzeImage(mockUri, ScanMode.FRUIT)
    
    assertTrue(result.isSuccess)
}
```

---

## Security Considerations

### API Key Management

1. **Never hardcode API keys** in source code
2. **Use BuildConfig** to inject keys at build time
3. **Add gradle.properties** to .gitignore
4. **Rotate keys** periodically

### Data Privacy

1. **Images are not stored** on Gemini servers permanently
2. **No personal data** is sent to the API
3. **Local processing** before upload when possible
4. **User consent** for camera and storage permissions

### Production Recommendations

1. **Backend Proxy**: Route API calls through your backend
2. **Rate Limiting**: Implement client-side rate limiting
3. **Request Validation**: Validate images before sending
4. **Error Logging**: Log errors for monitoring (without sensitive data)

---

## Additional Resources

- [Gemini API Documentation](https://ai.google.dev/docs)
- [Google AI Studio](https://makersuite.google.com/)
- [Kotlin Serialization](https://kotlinlang.org/docs/serialization.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
