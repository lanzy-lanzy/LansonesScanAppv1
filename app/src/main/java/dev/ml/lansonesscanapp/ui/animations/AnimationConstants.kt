package dev.ml.lansonesscanapp.ui.animations

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * Centralized animation constants for consistent timing and motion across the app.
 * All durations are in milliseconds.
 */
object AnimationConstants {
    
    // Duration constants
    const val DURATION_INSTANT = 1
    const val DURATION_VERY_SHORT = 100
    const val DURATION_SHORT = 150
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 400
    const val DURATION_VERY_LONG = 1000
    
    // Specific animation durations
    const val PRESS_ANIMATION_DURATION = 100
    const val CARD_PRESS_DURATION = 100
    const val DIALOG_ENTER_DURATION = 250
    const val DIALOG_EXIT_DURATION = 200
    const val BOTTOM_SHEET_ENTER_DURATION = 300
    const val BOTTOM_SHEET_EXIT_DURATION = 250
    const val MODE_SELECTION_DURATION = 300
    const val CHIP_COLOR_DURATION = 200
    const val IMAGE_FADE_DURATION = 400
    const val BUTTON_SLIDE_DURATION = 300
    const val RESULT_CARD_DURATION = 400
    const val LOADING_PULSE_DURATION = 1000
    const val ERROR_SLIDE_DURATION = 300
    const val ERROR_SHAKE_DURATION = 400
    const val ERROR_DISMISS_DURATION = 250
    const val LIST_ITEM_ENTER_DURATION = 300
    const val LIST_ITEM_EXIT_DURATION = 250
    const val EXPAND_DURATION = 300
    const val COLLAPSE_DURATION = 250
    const val CONTENT_FADE_IN_DURATION = 250
    const val CONTENT_FADE_OUT_DURATION = 150
    const val FAB_SCALE_IN_DURATION = 300
    const val FAB_SCALE_OUT_DURATION = 200
    const val EMPTY_STATE_FADE_DURATION = 400
    const val NAVIGATION_DURATION = 300
    
    // Delay constants
    const val STAGGER_DELAY_SHORT = 50
    const val STAGGER_DELAY_MEDIUM = 100
    const val STAGGER_DELAY_LONG = 200
    const val FAB_ENTRANCE_DELAY = 200
    
    // Scale values
    const val SCALE_PRESSED = 0.95f
    const val SCALE_NORMAL = 1.0f
    const val SCALE_CHIP_SELECTED = 1.05f
    const val SCALE_FAB_PRESSED = 0.9f
    const val SCALE_IMAGE_START = 0.95f
    const val SCALE_DIALOG_START = 0.8f
    const val SCALE_LOADING_MIN = 0.95f
    const val SCALE_LOADING_MAX = 1.05f
    
    // Offset values (in dp)
    const val SLIDE_OFFSET_SMALL = 50
    const val SHAKE_OFFSET = 10f
    
    // Opacity values
    const val SCRIM_OPACITY = 0.4f
    
    // Easing curves - Material Design 3 motion
    val FastOutSlowInEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    val EmphasizedDecelerateEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
    val EmphasizedAccelerateEasing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
    val StandardEasing = CubicBezierEasing(0.4f, 0.0f, 0.6f, 1.0f)
    
    // Spring specifications
    val SpringSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    val SpringSpecLowBounce = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    val SpringSpecNoBounce = spring<Float>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    )
    
    // Shake animation parameters
    const val SHAKE_OSCILLATIONS = 3
    const val SHAKE_SINGLE_DURATION = 50
}
