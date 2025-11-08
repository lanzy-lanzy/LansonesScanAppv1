package dev.ml.lansonesscanapp.model

import kotlinx.serialization.Serializable

/**
 * Data class for parsing Gemini leaf analysis JSON response
 */
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
