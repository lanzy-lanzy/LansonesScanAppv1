package dev.ml.lansonesscanapp.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAdaptiveAnimationDuration
import dev.ml.lansonesscanapp.ui.components.AnimatedCard
import kotlinx.coroutines.delay

/**
 * Dashboard screen - entry point of the app
 * Enhanced with staggered entrance animations for a polished experience
 */
@Composable
fun DashboardScreen(
    onNavigateToAnalysis: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    // Animation values for staggered entrance
    val iconAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    
    // Adaptive animation durations based on device performance and accessibility
    val iconDuration = rememberAdaptiveAnimationDuration(AnimationConstants.DURATION_LONG)
    val titleDuration = rememberAdaptiveAnimationDuration(AnimationConstants.DURATION_LONG)
    
    // Trigger entrance animations on composition
    LaunchedEffect(Unit) {
        // Icon fades in immediately (0ms delay)
        iconAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = iconDuration,
                easing = AnimationConstants.FastOutSlowInEasing
            )
        )
    }
    
    LaunchedEffect(Unit) {
        // Title fades in with 100ms delay
        delay(AnimationConstants.STAGGER_DELAY_MEDIUM.toLong())
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = titleDuration,
                easing = AnimationConstants.FastOutSlowInEasing
            )
        )
    }
    
    // Cleanup animations when screen is disposed
    DisposableEffect(Unit) {
        onDispose {
            // Animations are automatically cleaned up by Compose
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Decorative icon with fade-in animation (hardware-accelerated)
            Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = "Lansones",
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer(alpha = iconAlpha.value),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App Title with fade-in animation (hardware-accelerated)
            Text(
                text = "Welcome to\nLansones Scan",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.graphicsLayer(alpha = titleAlpha.value)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "AI-Powered Analysis",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.graphicsLayer(alpha = titleAlpha.value)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Identify fruit varieties and detect leaf diseases",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.graphicsLayer(alpha = titleAlpha.value)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Scan Button with entrance animation (200ms delay)
            AnimatedCard(
                onClick = onNavigateToAnalysis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                enterAnimation = true,
                enterDelay = AnimationConstants.STAGGER_DELAY_LONG,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Start Scanning",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Analyze fruits & leaves",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Scanner,
                            contentDescription = "Scan",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // History Button with entrance animation (300ms delay)
            AnimatedCard(
                onClick = onNavigateToHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                enterAnimation = true,
                enterDelay = AnimationConstants.DURATION_MEDIUM,
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.outlinedCardElevation()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "View History",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Past scan results",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
