package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAdaptiveAnimationDuration
import kotlinx.coroutines.delay

/**
 * A Material 3 Card with press scale animation and optional entrance animation.
 * 
 * Features:
 * - Scales down to 0.95x when pressed
 * - Optional slide-in entrance animation from bottom
 * - Configurable delay for staggered animations
 * - Maintains all Material 3 Card styling options
 * 
 * @param onClick Callback invoked when the card is clicked
 * @param modifier Modifier to be applied to the card
 * @param enterAnimation Whether to apply entrance animation (slide up + fade in)
 * @param enterDelay Delay in milliseconds before entrance animation starts (for staggering)
 * @param enabled Whether the card is clickable
 * @param shape The shape of the card
 * @param colors The colors to use for the card
 * @param elevation The elevation values to use for the card
 * @param content The content to display inside the card
 */
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enterAnimation: Boolean = false,
    enterDelay: Int = 0,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Press animation with adaptive duration
    val pressDuration = rememberAdaptiveAnimationDuration(AnimationConstants.CARD_PRESS_DURATION)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) AnimationConstants.SCALE_PRESSED else AnimationConstants.SCALE_NORMAL,
        animationSpec = tween(
            durationMillis = pressDuration,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "cardPressScale"
    )
    
    // Use derivedStateOf to reduce recompositions
    val scaleValue by remember { derivedStateOf { scale } }
    
    // Entrance animation
    var visible by remember { mutableStateOf(!enterAnimation) }
    val enterDuration = rememberAdaptiveAnimationDuration(AnimationConstants.DURATION_MEDIUM)
    
    LaunchedEffect(enterAnimation) {
        if (enterAnimation) {
            delay(enterDelay.toLong())
            visible = true
        }
    }
    
    // Cleanup on disposal
    DisposableEffect(Unit) {
        onDispose {
            // Animation cleanup is handled automatically by Compose
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = if (enterAnimation) {
            fadeIn(
                animationSpec = tween(
                    durationMillis = enterDuration,
                    easing = AnimationConstants.FastOutSlowInEasing
                )
            ) + slideInVertically(
                initialOffsetY = { AnimationConstants.SLIDE_OFFSET_SMALL },
                animationSpec = tween(
                    durationMillis = enterDuration,
                    easing = AnimationConstants.EmphasizedDecelerateEasing
                )
            )
        } else {
            fadeIn(animationSpec = tween(0))
        }
    ) {
        Card(
            onClick = onClick,
            // Use graphicsLayer for hardware-accelerated scale animation
            modifier = modifier.graphicsLayer(
                scaleX = scaleValue,
                scaleY = scaleValue
            ),
            enabled = enabled,
            shape = shape,
            colors = colors,
            elevation = elevation,
            interactionSource = interactionSource
        ) {
            content()
        }
    }
}
