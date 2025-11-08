package dev.ml.lansonesscanapp.model

import kotlinx.serialization.Serializable

/**
 * Data class for parsing Gemini fruit analysis JSON response
 */
@Serializable
data class FruitAnalysisResult(
    val variety: String,
    val confidence: Double,
    val ripeness: String,
    val defects: List<String> = emptyList(),
    val notes: String = ""
)
