package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import dev.ml.lansonesscanapp.model.ScanMode
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants

/**
 * Enhanced mode selection chip with icon and text layout.
 * 
 * Features:
 * - Icon + text layout with proper spacing
 * - Fruit icon (Apple) for FRUIT mode
 * - Leaf icon (Eco) for LEAF mode
 * - Enhanced styling with 16dp horizontal padding, 12dp vertical padding
 * - 16dp corner radius for modern appearance
 * - Smooth color and scale animations
 * 
 * @param mode The scan mode this chip represents
 * @param selected Whether this chip is currently selected
 * @param onClick Callback invoked when the chip is clicked
 * @param modifier Modifier to be applied to the chip
 */
@Composable
fun EnhancedModeChip(
    mode: ScanMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animate scale for selected chip (1.05x when selected)
    val scale by animateFloatAsState(
        targetValue = if (selected) AnimationConstants.SCALE_CHIP_SELECTED else AnimationConstants.SCALE_NORMAL,
        animationSpec = tween(
            durationMillis = AnimationConstants.MODE_SELECTION_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipScale"
    )
    
    // Use derivedStateOf to reduce recompositions
    val scaleValue by remember { derivedStateOf { scale } }
    
    // Animate colors for smooth transitions
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipContainerColor"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipContentColor"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipBorderColor"
    )
    
    // Determine icon and label based on mode
    val icon = when (mode) {
        ScanMode.FRUIT -> Icons.Default.Restaurant
        ScanMode.LEAF -> Icons.Default.Eco
    }
    
    val label = when (mode) {
        ScanMode.FRUIT -> "Fruit"
        ScanMode.LEAF -> "Leaf"
    }
    
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { 
            Text(
                text = label,
                color = contentColor
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "$label mode icon",
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        },
        enabled = true,
        // Use graphicsLayer for hardware-accelerated scale animation
        modifier = modifier
            .heightIn(min = 48.dp) // MD3: Minimum touch target
            .graphicsLayer(
                scaleX = scaleValue,
                scaleY = scaleValue
            )
            .semantics(mergeDescendants = true) {
                contentDescription = if (selected) {
                    "$label mode selected"
                } else {
                    "$label mode, tap to select"
                }
            },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = containerColor,
            labelColor = contentColor,
            selectedLabelColor = contentColor,
            iconColor = contentColor,
            selectedLeadingIconColor = contentColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = borderColor,
            selectedBorderColor = borderColor,
            borderWidth = if (selected) 2.dp else 1.dp
        ),
        shape = RoundedCornerShape(12.dp) // MD3: 12dp corner radius for chips
    )
}
