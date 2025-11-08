package dev.ml.lansonesscanapp.ui.animations

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext

/**
 * Device performance tier classification for adaptive animations.
 */
enum class PerformanceTier {
    LOW,      // Budget devices, reduce animation complexity
    MEDIUM,   // Mid-range devices, standard animations
    HIGH      // High-end devices, full animation effects
}

/**
 * Detects the device performance tier based on hardware capabilities.
 * Uses RAM, CPU cores, and Android version as indicators.
 * 
 * @param context Android context
 * @return PerformanceTier classification
 */
fun detectPerformanceTier(context: Context): PerformanceTier {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryInfo = ActivityManager.MemoryInfo()
    activityManager.getMemoryInfo(memoryInfo)
    
    // Get total RAM in GB
    val totalRamGB = memoryInfo.totalMem / (1024.0 * 1024.0 * 1024.0)
    
    // Get number of CPU cores
    val cpuCores = Runtime.getRuntime().availableProcessors()
    
    // Classify based on RAM and CPU cores
    return when {
        // High-end: 6GB+ RAM and 6+ cores, or Android 12+
        totalRamGB >= 6.0 && cpuCores >= 6 -> PerformanceTier.HIGH
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && totalRamGB >= 4.0 -> PerformanceTier.HIGH
        
        // Low-end: Less than 3GB RAM or fewer than 4 cores
        totalRamGB < 3.0 || cpuCores < 4 -> PerformanceTier.LOW
        
        // Medium: Everything else
        else -> PerformanceTier.MEDIUM
    }
}

/**
 * Remembers the device performance tier for the current device.
 * Cached for the lifetime of the composition.
 * 
 * @return PerformanceTier for the current device
 */
@Composable
fun rememberPerformanceTier(): PerformanceTier {
    val context = LocalContext.current
    return remember {
        detectPerformanceTier(context)
    }
}

/**
 * Returns an adaptive animation duration based on device performance tier.
 * Lower-tier devices get shorter durations for smoother performance.
 * 
 * @param baseDuration The standard animation duration in milliseconds
 * @param performanceTier The device performance tier
 * @return Adjusted duration based on performance tier
 */
fun getAdaptiveDuration(baseDuration: Int, performanceTier: PerformanceTier): Int {
    return when (performanceTier) {
        PerformanceTier.LOW -> (baseDuration * 0.7).toInt()  // 30% faster
        PerformanceTier.MEDIUM -> baseDuration
        PerformanceTier.HIGH -> baseDuration
    }
}

/**
 * Composable function that returns an adaptive animation duration.
 * Combines performance tier detection with accessibility settings.
 * 
 * @param baseDuration The standard animation duration in milliseconds
 * @return Adjusted duration based on device capabilities and accessibility settings
 */
@Composable
fun rememberAdaptiveAnimationDuration(baseDuration: Int): Int {
    val performanceTier = rememberPerformanceTier()
    val accessibilityDuration = rememberAnimationDuration(baseDuration)
    
    // Use derivedStateOf to avoid recomposition when values don't change
    val adaptiveDuration by remember(baseDuration, performanceTier, accessibilityDuration) {
        derivedStateOf {
            if (accessibilityDuration == AnimationConstants.DURATION_INSTANT) {
                // Accessibility takes precedence
                AnimationConstants.DURATION_INSTANT
            } else {
                // Apply performance tier adjustment
                getAdaptiveDuration(baseDuration, performanceTier)
            }
        }
    }
    
    return adaptiveDuration
}

/**
 * Determines if complex animations should be enabled based on performance tier.
 * Low-tier devices skip shimmer effects, staggered animations, etc.
 * 
 * @param performanceTier The device performance tier
 * @return true if complex animations should be enabled
 */
fun shouldEnableComplexAnimations(performanceTier: PerformanceTier): Boolean {
    return performanceTier != PerformanceTier.LOW
}

/**
 * Composable function that returns whether complex animations should be enabled.
 * 
 * @return true if complex animations should be enabled
 */
@Composable
fun rememberComplexAnimationsEnabled(): Boolean {
    val performanceTier = rememberPerformanceTier()
    return remember(performanceTier) {
        shouldEnableComplexAnimations(performanceTier)
    }
}

/**
 * Applies hardware-accelerated graphics layer for optimal animation performance.
 * Use this modifier for animated properties like scale, rotation, alpha, and translation.
 * 
 * Hardware acceleration offloads rendering to the GPU, improving performance.
 * 
 * @param alpha Optional alpha value (0f to 1f)
 * @param scaleX Optional horizontal scale factor
 * @param scaleY Optional vertical scale factor
 * @param rotationZ Optional rotation in degrees
 * @param translationX Optional horizontal translation in pixels
 * @param translationY Optional vertical translation in pixels
 * @return Modified Modifier with hardware-accelerated graphics layer
 */
fun Modifier.animatedGraphicsLayer(
    alpha: Float = 1f,
    scaleX: Float = 1f,
    scaleY: Float = 1f,
    rotationZ: Float = 0f,
    translationX: Float = 0f,
    translationY: Float = 0f
): Modifier = this.graphicsLayer(
    alpha = alpha,
    scaleX = scaleX,
    scaleY = scaleY,
    rotationZ = rotationZ,
    translationX = translationX,
    translationY = translationY
)

/**
 * Applies hardware-accelerated scale animation.
 * More performant than using the scale() modifier directly.
 * 
 * @param scale The scale factor to apply
 * @return Modified Modifier with hardware-accelerated scale
 */
fun Modifier.hardwareAcceleratedScale(scale: Float): Modifier = composed {
    this.graphicsLayer(
        scaleX = scale,
        scaleY = scale
    )
}

/**
 * Applies hardware-accelerated alpha animation.
 * More performant than using the alpha() modifier directly.
 * 
 * @param alpha The alpha value (0f to 1f)
 * @return Modified Modifier with hardware-accelerated alpha
 */
fun Modifier.hardwareAcceleratedAlpha(alpha: Float): Modifier = composed {
    this.graphicsLayer(
        alpha = alpha
    )
}

/**
 * DisposableEffect helper for cleaning up animations when a composable leaves composition.
 * Use this to ensure animations are properly cancelled and resources are released.
 * 
 * Example usage:
 * ```
 * val animatable = remember { Animatable(0f) }
 * AnimationCleanupEffect {
 *     animatable.stop()
 * }
 * ```
 * 
 * @param onDispose Cleanup function to call when the composable is disposed
 */
@Composable
fun AnimationCleanupEffect(onDispose: () -> Unit) {
    DisposableEffect(Unit) {
        onDispose {
            onDispose()
        }
    }
}

/**
 * Extension function to create a derived state for computed animation values.
 * This reduces recompositions by only triggering updates when the computed value changes.
 * 
 * Example usage:
 * ```
 * val progress by remember { derivedStateOf { animationValue * 2 } }
 * ```
 */
@Composable
fun <T> rememberDerivedAnimationState(
    calculation: () -> T
): androidx.compose.runtime.State<T> {
    return remember { derivedStateOf(calculation) }
}
