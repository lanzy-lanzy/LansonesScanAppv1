package dev.ml.lansonesscanapp.ui.animations

import android.content.Context
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

/**
 * Applies a press animation that scales down the composable when pressed.
 * Uses hardware-accelerated graphics layer for optimal performance.
 * 
 * @param scalePressed The scale factor when pressed (default: 0.95)
 * @param duration The animation duration in milliseconds
 * @return Modified Modifier with press animation
 */
fun Modifier.pressAnimation(
    scalePressed: Float = AnimationConstants.SCALE_PRESSED,
    duration: Int = AnimationConstants.PRESS_ANIMATION_DURATION
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) scalePressed else AnimationConstants.SCALE_NORMAL,
        animationSpec = tween(
            durationMillis = duration,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "pressAnimation"
    )
    
    // Use graphicsLayer for hardware acceleration instead of scale()
    this.graphicsLayer(
        scaleX = scale,
        scaleY = scale
    )
}

/**
 * Applies a shake animation that oscillates the composable horizontally.
 * Typically used for error states to draw attention.
 * 
 * @param trigger Boolean that triggers the shake animation when changed to true
 * @param oscillations Number of back-and-forth movements (default: 3)
 * @param offsetAmount Maximum horizontal offset in pixels (default: 10f)
 * @return Modified Modifier with shake animation
 */
fun Modifier.shakeAnimation(
    trigger: Boolean,
    oscillations: Int = AnimationConstants.SHAKE_OSCILLATIONS,
    offsetAmount: Float = AnimationConstants.SHAKE_OFFSET
): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    
    LaunchedEffect(trigger) {
        if (trigger) {
            repeat(oscillations) {
                offsetX.animateTo(
                    targetValue = offsetAmount,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.SHAKE_SINGLE_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
                offsetX.animateTo(
                    targetValue = -offsetAmount,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.SHAKE_SINGLE_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            }
            offsetX.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = AnimationConstants.SHAKE_SINGLE_DURATION,
                    easing = AnimationConstants.FastOutSlowInEasing
                )
            )
        }
    }
    
    this.offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

/**
 * Applies a slide-in animation from the bottom.
 * 
 * @param trigger Boolean that triggers the animation when true
 * @param offsetAmount Initial offset in pixels from which to slide (default: 50)
 * @param duration Animation duration in milliseconds
 * @return Modified Modifier with slide-in animation
 */
fun Modifier.slideInFromBottom(
    trigger: Boolean,
    offsetAmount: Float = AnimationConstants.SLIDE_OFFSET_SMALL.toFloat(),
    duration: Int = AnimationConstants.DURATION_MEDIUM
): Modifier = composed {
    val offsetY = remember { Animatable(if (trigger) 0f else offsetAmount) }
    
    LaunchedEffect(trigger) {
        if (trigger) {
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = duration,
                    easing = AnimationConstants.EmphasizedDecelerateEasing
                )
            )
        }
    }
    
    this.offset { IntOffset(0, offsetY.value.roundToInt()) }
}

/**
 * Returns the appropriate animation duration based on system accessibility settings.
 * If animations are disabled in system settings, returns 1ms for instant transitions.
 * 
 * @param baseDuration The normal animation duration in milliseconds
 * @return Adjusted duration based on accessibility settings
 */
@Composable
fun rememberAnimationDuration(baseDuration: Int): Int {
    val context = LocalContext.current
    val animationScale = remember {
        getAnimationScale(context)
    }
    
    return if (animationScale == 0f) {
        AnimationConstants.DURATION_INSTANT
    } else {
        (baseDuration * animationScale).toInt()
    }
}

/**
 * Checks if animations are enabled in system settings.
 * 
 * @return true if animations are enabled, false otherwise
 */
@Composable
fun rememberAnimationsEnabled(): Boolean {
    val context = LocalContext.current
    return remember {
        getAnimationScale(context) > 0f
    }
}

/**
 * Gets the system animation scale setting.
 * 
 * @param context Android context
 * @return Animation scale factor (0.0 = disabled, 1.0 = normal speed)
 */
private fun getAnimationScale(context: Context): Float {
    return try {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        )
    } catch (e: Exception) {
        1f // Default to enabled if we can't read the setting
    }
}

/**
 * Checks if accessibility features are enabled that might affect animations.
 * 
 * @param context Android context
 * @return true if accessibility features are enabled
 */
fun isAccessibilityEnabled(context: Context): Boolean {
    val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
    return accessibilityManager?.isEnabled == true
}
