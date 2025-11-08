package dev.ml.lansonesscanapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush

/**
 * A reusable gradient background component that provides a subtle vertical gradient
 * from primary container (10% alpha) to surface color.
 * 
 * Features:
 * - Vertical gradient from top to bottom
 * - Uses Material 3 theme colors for consistency
 * - Adapts to light and dark themes
 * - Doesn't interfere with content readability
 * 
 * @param modifier Modifier to be applied to the background container
 * @param content The content to display on top of the gradient background
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surface = MaterialTheme.colorScheme.surface
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryContainer.copy(alpha = 0.1f),
                        surface
                    )
                )
            )
    ) {
        content()
    }
}
