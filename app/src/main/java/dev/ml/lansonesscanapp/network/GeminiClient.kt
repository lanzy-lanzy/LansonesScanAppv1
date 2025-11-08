package dev.ml.lansonesscanapp.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dev.ml.lansonesscanapp.BuildConfig
import dev.ml.lansonesscanapp.model.FruitAnalysisResult
import dev.ml.lansonesscanapp.model.LeafAnalysisResult
import dev.ml.lansonesscanapp.model.ScanMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

/**
 * Client for interacting with Google Gemini API
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
    
    /**
     * Analyzes an image based on the scan mode
     */
    suspend fun analyzeImage(imageUri: Uri, mode: ScanMode): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Load bitmap from URI
            val bitmap = loadBitmapFromUri(imageUri)
                ?: return@withContext Result.failure(Exception("Failed to load image"))
            
            // Get appropriate prompt
            val prompt = when (mode) {
                ScanMode.FRUIT -> getFruitPrompt()
                ScanMode.LEAF -> getLeafPrompt()
            }
            
            // Create content with image and prompt
            val inputContent = content {
                image(bitmap)
                text(prompt)
            }
            
            // Generate response
            val response = model.generateContent(inputContent)
            val responseText = response.text ?: ""
            
            Log.d(TAG, "Raw Gemini response: $responseText")
            
            // Extract JSON from response (remove markdown code blocks if present)
            val cleanedResponse = cleanJsonResponse(responseText)
            
            Log.d(TAG, "Cleaned JSON response: $cleanedResponse")
            
            Result.success(cleanedResponse)
        } catch (e: Exception) {
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
            You are a tropical fruit expert specializing in lansones (lanzones) fruit identification.
            
            Analyze the uploaded image of a lansones fruit and provide a detailed, formal analysis.
            
            Please structure your response in the following format without using asterisks or special formatting:
            
            VARIETY IDENTIFICATION
            Identify the variety (Lonkong, Duco, Paete, or Unknown) and provide your confidence level as a percentage.
            
            RIPENESS ASSESSMENT
            Classify the ripeness as unripe, ripe, overripe, or damaged. Explain the visual indicators.
            
            QUALITY ASSESSMENT
            List any visible defects such as bruising, insect holes, discoloration, or other issues.
            
            RECOMMENDATIONS
            Provide suggestions for handling, storage, or consumption based on the fruit's condition.
            
            Write in clear, formal language suitable for farmers and agricultural professionals. Avoid using asterisks, bullet points, or markdown formatting. Use numbered lists or paragraph format instead.
        """.trimIndent()
    }
    
    private fun getLeafPrompt(): String {
        return """
            You are a plant pathologist specializing in tropical crops, particularly lansones (lanzones) trees.
            
            Analyze the uploaded image of a lansones leaf and provide a comprehensive diagnostic report.
            
            Please structure your response in the following format without using asterisks or special formatting:
            
            DISEASE DIAGNOSIS
            Identify the primary disease or condition affecting this leaf. List all possible diagnoses with confidence levels. Common lansones diseases include leaf spot, powdery mildew, anthracnose, bacterial blight, and nutrient deficiencies.
            
            SEVERITY ASSESSMENT
            Classify the severity as low, moderate, or high. Describe the extent of damage and potential impact on the tree.
            
            TREATMENT RECOMMENDATIONS
            Provide step-by-step treatment instructions. Include both chemical and organic options when applicable.
            
            RECOMMENDED PRODUCTS
            Suggest specific types of products (fungicides, pesticides, fertilizers) with examples that farmers can use.
            
            PREVENTION MEASURES
            Describe cultural practices and farming techniques to prevent recurrence of this condition.
            
            ADDITIONAL OBSERVATIONS
            Note any other concerns or information that would be helpful for the farmer.
            
            Write in clear, formal language suitable for farmers and agricultural professionals. Avoid using asterisks, bullet points, or markdown formatting. Use numbered lists or paragraph format instead.
        """.trimIndent()
    }
}
