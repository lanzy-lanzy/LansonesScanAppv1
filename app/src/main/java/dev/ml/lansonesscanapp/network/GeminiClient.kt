package dev.ml.lansonesscanapp.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dev.ml.lansonesscanapp.BuildConfig
import dev.ml.lansonesscanapp.cache.AnalysisCache
import dev.ml.lansonesscanapp.model.FruitAnalysisResult
import dev.ml.lansonesscanapp.model.LeafAnalysisResult
import dev.ml.lansonesscanapp.model.ScanMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

/**
 * Client for interacting with Google Gemini API with caching support
 */
class GeminiClient(private val context: Context) {
    
    companion object {
        private const val TAG = "GeminiClient"
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
    
    private val model = GenerativeModel(
        modelName = "gemini-2.5-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )
    
    private val cache = AnalysisCache(context)
    
    /**
     * Analyzes an image based on the scan mode with caching support
     */
    suspend fun analyzeImage(imageUri: Uri, mode: ScanMode): Result<String> = withContext(Dispatchers.IO) {
        var originalBitmap: Bitmap? = null
        var resizedBitmap: Bitmap? = null
        
        try {
            // Load bitmap from URI
            originalBitmap = loadBitmapFromUri(imageUri)
                ?: return@withContext Result.failure(Exception("Failed to load image"))
            
            // Resize image FIRST to avoid memory issues
            resizedBitmap = resizeBitmap(originalBitmap, 1024)
            
            // Recycle original if different from resized
            if (originalBitmap != resizedBitmap) {
                originalBitmap.recycle()
                originalBitmap = null
            }
            
            // Generate hash from resized bitmap (more memory efficient)
            val imageHash = cache.generateImageHash(resizedBitmap)
            Log.d(TAG, "Image hash: $imageHash")
            
            // Check cache first
            val cachedResult = cache.getCachedResult(imageHash, mode)
            if (cachedResult != null) {
                Log.d(TAG, "Cache hit! Returning cached result")
                resizedBitmap?.recycle()
                return@withContext Result.success(cachedResult.result)
            }
            
            Log.d(TAG, "Cache miss. Calling API...")
            
            // Get appropriate prompt
            val prompt = when (mode) {
                ScanMode.FRUIT -> getFruitPrompt()
                ScanMode.LEAF -> getLeafPrompt()
                ScanMode.UNKNOWN -> return@withContext Result.failure(
                    Exception("Cannot analyze with UNKNOWN mode")
                )
            }
            
            // Create content with image and prompt
            val inputContent = content {
                image(resizedBitmap)
                text(prompt)
            }
            
            // Generate response
            val response = model.generateContent(inputContent)
            val responseText = response.text ?: ""
            
            Log.d(TAG, "Raw Gemini response: $responseText")
            
            // Extract JSON from response (remove markdown code blocks if present)
            val cleanedResponse = cleanJsonResponse(responseText)
            
            Log.d(TAG, "Cleaned JSON response: $cleanedResponse")
            
            // Cache the result for future use
            cache.cacheResult(imageHash, mode, cleanedResponse)
            Log.d(TAG, "Result cached successfully")
            
            // Clean up bitmap
            resizedBitmap?.recycle()
            
            Result.success(cleanedResponse)
        } catch (e: OutOfMemoryError) {
            Log.e(TAG, "Out of memory error", e)
            // Clean up bitmaps
            originalBitmap?.recycle()
            resizedBitmap?.recycle()
            Result.failure(Exception("Image too large. Please try a smaller image or use gallery instead of camera."))
        } catch (e: com.google.ai.client.generativeai.type.GoogleGenerativeAIException) {
            Log.e(TAG, "Gemini API error: ${e.message}", e)
            // Clean up bitmaps
            originalBitmap?.recycle()
            resizedBitmap?.recycle()
            val errorMessage = when {
                e.message?.contains("API key") == true -> "Invalid API key. Please check your Gemini API key."
                e.message?.contains("quota") == true -> "API quota exceeded. Please try again later."
                e.message?.contains("400") == true -> "Invalid request. The model may not support this operation."
                else -> "API error: ${e.message}"
            }
            Result.failure(Exception(errorMessage, e))
        } catch (e: Exception) {
            Log.e(TAG, "Analysis failed: ${e.message}", e)
            // Clean up bitmaps
            originalBitmap?.recycle()
            resizedBitmap?.recycle()
            Result.failure(e)
        }
    }
    
    /**
     * Parse fruit analysis result from JSON
     */
    fun parseFruitResult(jsonString: String): FruitAnalysisResult? {
        return try {
            json.decodeFromString<FruitAnalysisResult>(jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse fruit result: ${e.message}", e)
            Log.e(TAG, "JSON was: $jsonString")
            null
        }
    }
    
    /**
     * Parse leaf analysis result from JSON
     */
    fun parseLeafResult(jsonString: String): LeafAnalysisResult? {
        return try {
            json.decodeFromString<LeafAnalysisResult>(jsonString)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse leaf result: ${e.message}", e)
            Log.e(TAG, "JSON was: $jsonString")
            null
        }
    }
    
    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Resize bitmap to fit within maxSize while maintaining aspect ratio
     * Smaller images = faster API response
     */
    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // If image is already small enough, return as-is
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }
        
        // Calculate new dimensions maintaining aspect ratio
        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int
        
        if (width > height) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
    
    private fun cleanJsonResponse(response: String): String {
        // Remove markdown code blocks if present
        var cleaned = response.trim()
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.removePrefix("```json").removeSuffix("```").trim()
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.removePrefix("```").removeSuffix("```").trim()
        }
        return cleaned
    }
    
    private fun getFruitPrompt(): String {
        return """
            You are a lansones fruit expert. Analyze this image quickly and concisely.
            
            CRITICAL: If this is NOT a lansones fruit, respond ONLY with:
            "INVALID_IMAGE: Please select image that is fruit of a lansones"
            
            If it IS a lansones fruit, provide a brief analysis:
            
            VARIETY: Identify variety (Lonkong, Duco, Paete, or Unknown) with confidence %.
            
            RIPENESS: State if unripe, ripe, overripe, or damaged with key visual indicators.
            
            QUALITY: Note any defects (bruising, insect damage, discoloration).
            
            RECOMMENDATIONS: Brief handling, storage, or consumption advice.
            
            Keep response concise and clear. No asterisks or markdown formatting.
        """.trimIndent()
    }
    
    private fun getLeafPrompt(): String {
        return """
            You are a lansones plant pathologist. Analyze this leaf quickly and concisely.
            
            CRITICAL: If this is NOT a lansones leaf, respond ONLY with:
            "INVALID_IMAGE: Please select image that is leaf of a lansones"
            
            If it IS a lansones leaf, provide a brief diagnostic:
            
            DIAGNOSIS: Identify primary disease/condition with confidence %. Common issues: leaf spot, powdery mildew, anthracnose, bacterial blight, nutrient deficiency, pest infestation.
            
            SEVERITY: State low, moderate, or high with brief impact description.
            
            TREATMENT: Provide 2-3 key treatment steps (chemical and/or organic options).
            
            PRODUCTS: Suggest 1-2 specific product types (fungicides, pesticides, fertilizers).
            
            PREVENTION: List 2-3 key prevention practices.
            
            Keep response concise and actionable. No asterisks or markdown formatting.
        """.trimIndent()
    }
}
