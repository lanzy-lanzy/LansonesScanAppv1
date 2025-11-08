package dev.ml.lansonesscanapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.ml.lansonesscanapp.model.GuideStep
import dev.ml.lansonesscanapp.model.GuideSteps
import dev.ml.lansonesscanapp.ui.animations.rememberAnimationDuration
import kotlinx.coroutines.launch

/**
 * Interactive "How it Works" guide component
 * Displays a modal dialog with step-by-step instructions for using the app
 * 
 * @param visible Whether the guide should be displayed
 * @param onDismiss Callback invoked when the guide is dismissed
 * @param modifier Optional modifier for the component
 */
@Composable
fun HowItWorksGuide(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get guide steps with theme colors
    val guideSteps = GuideSteps.getSteps(
        primaryContainer = MaterialTheme.colorScheme.primaryContainer,
        secondaryContainer = MaterialTheme.colorScheme.secondaryContainer,
        tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    )
    
    // Respect system animation settings for accessibility
    val fadeDuration = rememberAnimationDuration(300)
    
    // Animated visibility with fade in/out
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(fadeDuration)),
        exit = fadeOut(animationSpec = tween(fadeDuration)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                    onClickLabel = "Dismiss guide"
                ),
            contentAlignment = Alignment.Center
        ) {
            // Guide content card
            GuideContent(
                steps = guideSteps,
                onDismiss = onDismiss,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Prevent clicks from propagating to scrim */ }
                    )
            )
        }
    }
}

/**
 * Main guide content with pager and navigation
 */
@Composable
private fun GuideContent(
    steps: List<GuideStep>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { steps.size })
    val coroutineScope = rememberCoroutineScope()
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp), // MD3: Extra large corner radius for dialogs
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp // MD3: High elevation for modal dialogs
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // MD3: Large padding for dialog content
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Horizontal pager for steps
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                GuideStepContent(step = steps[page])
            }
            
            Spacer(modifier = Modifier.height(24.dp)) // MD3: Large spacing between sections
            
            // Page indicators
            PageIndicators(
                pageCount = steps.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(vertical = 16.dp) // MD3: Medium padding
            )
            
            Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
            
            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Previous button (hidden on first page)
                if (pagerState.currentPage > 0) {
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.heightIn(min = 48.dp) // MD3: Minimum touch target
                    ) {
                        Text("Previous")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
                // Next/Got it button
                Button(
                    onClick = {
                        if (pagerState.currentPage < steps.size - 1) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        } else {
                            onDismiss()
                        }
                    },
                    modifier = Modifier.heightIn(min = 48.dp) // MD3: Minimum touch target
                ) {
                    Text(
                        if (pagerState.currentPage < steps.size - 1) "Next" else "Got it"
                    )
                }
            }
        }
    }
}

/**
 * Individual guide step content display
 */
@Composable
private fun GuideStepContent(
    step: GuideStep,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // MD3: Medium padding
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(80.dp) // MD3: Large icon container
                .clip(CircleShape)
                .background(step.containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = step.icon,
                contentDescription = "${step.title} icon",
                modifier = Modifier.size(64.dp), // MD3: Large icon size
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp)) // MD3: Large spacing
        
        // Title
        Text(
            text = step.title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
        
        // Description
        Text(
            text = step.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.6f
        )
    }
}

/**
 * Page indicator dots showing current position
 */
@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.semantics {
            contentDescription = "Step ${currentPage + 1} of $pageCount"
        },
        horizontalArrangement = Arrangement.spacedBy(8.dp) // MD3: Small spacing between indicators
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    )
            )
        }
    }
}
