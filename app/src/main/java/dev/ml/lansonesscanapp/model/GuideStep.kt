package dev.ml.lansonesscanapp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single step in the "How it Works" guide
 * 
 * @property icon The icon to display for this step
 * @property title The title of the step
 * @property description The detailed description of what this step involves
 * @property containerColor The background color for this step's container
 */
data class GuideStep(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val containerColor: Color
)

/**
 * Predefined guide steps for the "How it Works" guide
 * These steps explain the core functionality of the Lansones Scan App
 */
object GuideSteps {
    
    /**
     * Creates the list of guide steps with appropriate icons, titles, and descriptions
     * 
     * @param primaryContainer Color for the first step container
     * @param secondaryContainer Color for the second step container
     * @param tertiaryContainer Color for the third step container
     * @return List of three GuideStep objects
     */
    fun getSteps(
        primaryContainer: Color,
        secondaryContainer: Color,
        tertiaryContainer: Color
    ): List<GuideStep> {
        return listOf(
            GuideStep(
                icon = Icons.Default.Tune,
                title = "Choose Your Scan Mode",
                description = "Select 'Fruit' to analyze lansones fruit ripeness and quality, or 'Leaf' to detect diseases and health issues.",
                containerColor = primaryContainer
            ),
            GuideStep(
                icon = Icons.Default.CameraAlt,
                title = "Capture or Select Image",
                description = "Take a photo with your camera or choose an existing image from your gallery. Make sure the image is clear and well-lit.",
                containerColor = secondaryContainer
            ),
            GuideStep(
                icon = Icons.Default.Search,
                title = "Get Instant Analysis",
                description = "Tap 'Analyze Image' and our AI will process your image and provide detailed insights about your lansones.",
                containerColor = tertiaryContainer
            )
        )
    }
}
