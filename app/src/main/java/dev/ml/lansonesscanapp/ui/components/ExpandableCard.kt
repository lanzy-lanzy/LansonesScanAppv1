package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.IntSize
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAnimationDuration

/**
 * A Material 3 Card that smoothly expands and collapses to show additional content.
 * 
 * Features:
 * - Height animates smoothly using animateContentSize with spring animation
 * - Content fades in with 50ms delay when expanding (250ms total)
 * - Content fades out immediately when collapsing (150ms)
 * - Natural spring animation for expansion feel
 * - Maintains all Material 3 Card styling options
 * 
 * @param expanded Whether the card is currently expanded
 * @param onExpandChange Callback invoked when the expansion state should change
 * @param modifier Modifier to be applied to the card
 * @param shape The shape of the card
 * @param colors The colors to use for the card
 * @param elevation The elevation values to use for the card
 * @param header The content to always display (visible in both collapsed and expanded states)
 * @param expandedContent The content to display only when expanded
 */
@Composable
fun ExpandableCard(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(),
    header: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    val expandDuration = rememberAnimationDuration(AnimationConstants.EXPAND_DURATION)
    val fadeInDuration = rememberAnimationDuration(AnimationConstants.CONTENT_FADE_IN_DURATION)
    val fadeOutDuration = rememberAnimationDuration(AnimationConstants.CONTENT_FADE_OUT_DURATION)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { onExpandChange(!expanded) },
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header is always visible
            header()
            
            // Expanded content with fade animation
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = fadeInDuration,
                        delayMillis = AnimationConstants.STAGGER_DELAY_SHORT,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = fadeOutDuration,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            ) {
                expandedContent()
            }
        }
    }
}

/**
 * A variant of ExpandableCard that doesn't handle clicks internally.
 * Useful when you need custom click handling or want to control expansion externally.
 * 
 * @param expanded Whether the card is currently expanded
 * @param modifier Modifier to be applied to the card
 * @param shape The shape of the card
 * @param colors The colors to use for the card
 * @param elevation The elevation values to use for the card
 * @param header The content to always display (visible in both collapsed and expanded states)
 * @param expandedContent The content to display only when expanded
 */
@Composable
fun ExpandableCardNoClick(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: androidx.compose.material3.CardElevation = CardDefaults.cardElevation(),
    header: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    val fadeInDuration = rememberAnimationDuration(AnimationConstants.CONTENT_FADE_IN_DURATION)
    val fadeOutDuration = rememberAnimationDuration(AnimationConstants.CONTENT_FADE_OUT_DURATION)
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = shape,
        colors = colors,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header is always visible
            header()
            
            // Expanded content with fade animation
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = fadeInDuration,
                        delayMillis = AnimationConstants.STAGGER_DELAY_SHORT,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = fadeOutDuration,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            ) {
                expandedContent()
            }
        }
    }
}
