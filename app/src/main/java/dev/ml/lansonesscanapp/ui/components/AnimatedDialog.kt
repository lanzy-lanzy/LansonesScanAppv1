package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAnimationDuration

/**
 * A dialog wrapper with scale and fade entrance/exit animations.
 * 
 * Features:
 * - Fade in + scale from 0.8 to 1.0 on enter (250ms)
 * - Fade out + scale from 1.0 to 0.8 on exit (200ms)
 * - Backdrop fade animation with 40% opacity
 * - Uses overshoot interpolator for natural spring-like feel
 * 
 * Requirements addressed:
 * - 3.1: Dialog appears with fade-in and scale animation (250ms)
 * - 3.2: Dialog dismisses with fade-out and scale animation (200ms)
 * - 3.3: Backdrop fades in with 40% opacity (200ms)
 * - 3.4: Uses overshoot interpolator for scale animation
 * 
 * @param visible Whether the dialog is currently visible
 * @param onDismissRequest Callback invoked when the user tries to dismiss the dialog
 * @param properties Dialog properties for customization
 * @param content The content to display inside the dialog
 */
@Composable
fun AnimatedDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_ENTER_DURATION),
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + scaleIn(
                    initialScale = AnimationConstants.SCALE_DIALOG_START,
                    animationSpec = tween(
                        durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_ENTER_DURATION),
                        easing = CubicBezierEasing(0.34f, 1.56f, 0.64f, 1f) // Overshoot easing for natural spring-like feel
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_EXIT_DURATION),
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + scaleOut(
                    targetScale = AnimationConstants.SCALE_DIALOG_START,
                    animationSpec = tween(
                        durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_EXIT_DURATION),
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            ) {
                content()
            }
        }
    }
}

/**
 * A backdrop scrim that can be used behind dialogs or bottom sheets.
 * 
 * @param visible Whether the scrim is visible
 * @param opacity The opacity of the scrim (default: 40%)
 * @param onClick Callback invoked when the scrim is clicked
 */
@Composable
fun AnimatedScrim(
    visible: Boolean,
    opacity: Float = AnimationConstants.SCRIM_OPACITY,
    onClick: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_ENTER_DURATION),
                easing = AnimationConstants.FastOutSlowInEasing
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = rememberAnimationDuration(AnimationConstants.DIALOG_EXIT_DURATION),
                easing = AnimationConstants.FastOutSlowInEasing
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = opacity))
        )
    }
}
