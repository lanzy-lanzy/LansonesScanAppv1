package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import dev.ml.lansonesscanapp.model.ScanMode

/**
 * Displays an animated description for the selected scan mode.
 * 
 * Features:
 * - AnimatedContent with 200ms fade transition
 * - "Analyze fruit ripeness and quality" for FRUIT mode
 * - "Detect leaf diseases and health" for LEAF mode
 * - Smooth transitions when mode changes
 * 
 * @param mode The currently selected scan mode
 * @param modifier Modifier to be applied to the component
 */
@Composable
fun ModeDescription(
    mode: ScanMode,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = mode,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(durationMillis = 200)
            ) togetherWith fadeOut(
                animationSpec = tween(durationMillis = 200)
            )
        },
        label = "modeDescription",
        modifier = modifier
    ) { targetMode ->
        val description = when (targetMode) {
            ScanMode.FRUIT -> "Analyze fruit ripeness and quality"
            ScanMode.LEAF -> "Detect leaf diseases and health"
            ScanMode.UNKNOWN -> "General analysis"
        }
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
