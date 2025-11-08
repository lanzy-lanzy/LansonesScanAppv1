# Design Document

## Overview

This design implements professional animations and transitions across the Lansones Scan App using Jetpack Compose's animation APIs. The solution focuses on Material Design 3 motion principles, creating a cohesive and polished user experience through coordinated animations, smooth transitions, and responsive feedback.

The design leverages Compose's declarative animation system including AnimatedVisibility, animateContentSize, Animatable, and Transition APIs to create fluid interactions without compromising performance.

## Architecture

### Animation System Structure

```
ui/
├── animations/
│   ├── AnimationConstants.kt          # Centralized animation durations and specs
│   ├── AnimationExtensions.kt         # Reusable animation modifiers
│   └── TransitionSpecs.kt             # Screen transition specifications
├── components/
│   ├── AnimatedCard.kt                # Pressable card with scale animation
│   ├── AnimatedDialog.kt              # Dialog with entrance/exit animations
│   ├── ImageSelectionBottomSheet.kt   # Modal bottom sheet for image selection
│   └── ExpandableCard.kt              # Card with smooth expand/collapse
└── screens/
    ├── DashboardScreen.kt             # Enhanced with entrance animations
    ├── AnalysisScreen.kt              # Enhanced with result animations
    └── HistoryScreen.kt               # Enhanced with list animations
```

### Key Design Principles

1. **Consistency**: All animations use centralized timing and easing specifications
2. **Performance**: Animations are optimized to maintain 60fps on target devices
3. **Interruptibility**: All animations can be interrupted and reversed smoothly
4. **Accessibility**: Animations respect system animation preferences


## Components and Interfaces

### 1. Animation Constants

Centralized configuration for all animation timings and specifications:

```kotlin
object AnimationConstants {
    // Durations
    const val DURATION_SHORT = 150
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 400
    
    // Easing
    val FastOutSlowInEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
    val EmphasizedEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
    
    // Spring specs
    val SpringSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
}
```

### 2. AnimatedCard Component

Reusable card with press animation and optional entrance animation:

```kotlin
@Composable
fun AnimatedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enterAnimation: Boolean = false,
    enterDelay: Int = 0,
    content: @Composable () -> Unit
)
```

**Features:**
- Scale animation on press (0.95x)
- Optional slide-in entrance animation
- Configurable delay for staggered animations
- Maintains Material 3 card styling

### 3. AnimatedDialog Component

Dialog wrapper with scale and fade animations:

```kotlin
@Composable
fun AnimatedDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
)
```


**Animation Behavior:**
- Enter: Fade in + scale from 0.8 to 1.0 (250ms)
- Exit: Fade out + scale from 1.0 to 0.8 (200ms)
- Backdrop: Fade in/out (200ms)
- Uses overshoot interpolator for natural feel

### 4. ImageSelectionBottomSheet Component

Modal bottom sheet for camera/gallery selection:

```kotlin
@Composable
fun ImageSelectionBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
)
```

**Features:**
- Slides up from bottom (300ms enter, 250ms exit)
- Scrim overlay with 40% opacity
- Large touch targets for accessibility
- Icon + text layout for clarity

### 5. ExpandableCard Component

Card that smoothly expands to show additional content:

```kotlin
@Composable
fun ExpandableCard(
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    header: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit
)
```

**Animation Behavior:**
- Height animates with animateContentSize()
- Content fades in with 50ms delay when expanding
- Content fades out immediately when collapsing
- Uses spring animation for natural motion


## Data Models

### AnimationState

Tracks animation states for complex multi-step animations:

```kotlin
data class AnimationState(
    val isEntering: Boolean = false,
    val isExiting: Boolean = false,
    val progress: Float = 0f
)
```

### TransitionConfig

Configuration for screen transitions:

```kotlin
data class TransitionConfig(
    val duration: Int = AnimationConstants.DURATION_MEDIUM,
    val easing: Easing = AnimationConstants.FastOutSlowInEasing,
    val slideDistance: Dp = 50.dp,
    val fadeEnabled: Boolean = true
)
```

## Screen-Specific Implementations

### DashboardScreen Enhancements

**Entrance Animation Sequence:**
1. App icon fades in (400ms)
2. Title fades in with 100ms delay
3. Scan card slides up from bottom with 200ms delay
4. History card slides up from bottom with 300ms delay

**Implementation Approach:**
- Use LaunchedEffect to trigger animations on composition
- Animate each element with Animatable for precise control
- Stagger delays using coroutine delays

**Card Interactions:**
- Scale animation on press using interactionSource
- Elevation increase on hover (desktop/tablet)
- Ripple effect maintained from Material 3


### AnalysisScreen Enhancements

**Mode Selection Animation:**
- Animated indicator slides between options (300ms)
- Selected chip scales up slightly (1.05x)
- Color transitions smoothly (200ms)
- Implementation: Use animateDpAsState for indicator position

**Image Selection Flow:**
1. User taps "Select Image" button
2. ModalBottomSheet slides up (300ms)
3. User selects camera or gallery
4. Sheet slides down (250ms)
5. Image preview fades in (400ms)

**Image Preview Animation:**
- Fade in with alpha 0 to 1 (400ms)
- Subtle scale from 0.95 to 1.0
- Shimmer effect during loading
- Analyze button slides up with fade (300ms)

**Result Display Animation:**
1. Result card fades in (400ms)
2. Card slides up 50dp simultaneously
3. Title appears first
4. Description fades in with 100ms delay
5. Creates staggered, engaging reveal

**Loading State:**
- Pulsing animation on analyze button
- Scale between 0.95 and 1.05 (1000ms cycle)
- Infinite repeat with reverse mode

**Error Animation:**
- Slide in from top (300ms)
- Shake animation: 3 oscillations (400ms)
- Attention-grabbing without being jarring


### HistoryScreen Enhancements

**List Item Animations:**
- New items slide in from right (300ms) with fade
- Deleted items slide out to right (250ms) with fade
- Use AnimatedVisibility with slideInHorizontally/slideOutHorizontally
- Key-based animations for proper item tracking

**Expandable Items:**
- Height animates with animateContentSize modifier
- Description content uses AnimatedVisibility
- Expand: fade in with 50ms delay (250ms total)
- Collapse: fade out immediately (150ms)
- Smooth spring animation for natural feel

**FAB Animation:**
- Scale in from 0.0 to 1.0 (300ms) with 200ms delay
- Scale out from 1.0 to 0.0 (200ms) when list empties
- Press animation: scale to 0.9 (100ms)
- Release animation: scale to 1.0 (100ms)
- Uses AnimatedVisibility with scaleIn/scaleOut

**Empty State:**
- Fade in when list becomes empty (400ms)
- Fade out when first item added (300ms)
- Crossfade between empty and list states

## Error Handling

### Animation Performance Issues

**Problem:** Animations drop frames on lower-end devices
**Solution:** 
- Detect device performance tier
- Reduce animation durations by 30% on low-tier devices
- Disable complex animations (shimmer, stagger) on very low-tier devices


### Accessibility Considerations

**Problem:** Users with motion sensitivity may find animations uncomfortable
**Solution:**
- Check system animation settings via AccessibilityManager
- Reduce animation durations to 1ms when animations disabled
- Maintain layout changes but skip visual transitions
- Provide instant feedback without motion

**Implementation:**
```kotlin
@Composable
fun rememberAnimationDuration(baseDuration: Int): Int {
    val context = LocalContext.current
    val accessibilityManager = remember {
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }
    return if (accessibilityManager.isEnabled && 
               Settings.Global.getFloat(
                   context.contentResolver,
                   Settings.Global.ANIMATOR_DURATION_SCALE,
                   1f
               ) == 0f) {
        1 // Effectively instant
    } else {
        baseDuration
    }
}
```

### Memory Leaks

**Problem:** Animations may hold references after screen disposal
**Solution:**
- Use DisposableEffect to cancel animations on disposal
- Ensure all coroutines are properly scoped to composition lifecycle
- Use rememberCoroutineScope for animation coroutines

## Testing Strategy

### Unit Tests

**AnimationConstants Tests:**
- Verify duration values are within acceptable ranges
- Ensure easing functions produce expected curves
- Test spring specifications for stability


### Composable Tests

**AnimatedCard Tests:**
- Verify scale animation triggers on press
- Test entrance animation timing
- Ensure animations complete properly
- Validate accessibility behavior

**AnimatedDialog Tests:**
- Test visibility state changes
- Verify backdrop animation
- Ensure proper cleanup on dismissal
- Test rapid open/close scenarios

**ExpandableCard Tests:**
- Verify smooth height transitions
- Test content visibility timing
- Ensure proper state management
- Test rapid expand/collapse

### UI Tests

**Screen Transition Tests:**
- Navigate between all screens
- Verify smooth transitions
- Test back navigation
- Ensure no visual glitches

**Interaction Tests:**
- Test all clickable elements
- Verify press animations
- Test long-press behaviors
- Ensure ripple effects work

**Performance Tests:**
- Measure frame rates during animations
- Test on low-end device emulators
- Verify memory usage during transitions
- Test with accessibility settings enabled

### Visual Regression Tests

- Capture screenshots at key animation frames
- Compare against baseline images
- Detect unintended visual changes
- Validate animation smoothness


## Implementation Details

### Animation Modifier Extensions

Create reusable modifiers for common animation patterns:

```kotlin
fun Modifier.pressAnimation(): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100)
    )
    this
        .scale(scale)
        .clickable(interactionSource = interactionSource, indication = null) { }
}

fun Modifier.shakeAnimation(trigger: Boolean): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(trigger) {
        if (trigger) {
            repeat(3) {
                offsetX.animateTo(10f, tween(50))
                offsetX.animateTo(-10f, tween(50))
            }
            offsetX.animateTo(0f, tween(50))
        }
    }
    this.offset { IntOffset(offsetX.value.roundToInt(), 0) }
}
```

### Navigation Transitions

Integrate with Compose Navigation for screen transitions:

```kotlin
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        enterTransition = {
            fadeIn(animationSpec = tween(300)) +
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) +
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        }
    ) {
        // Screen definitions
    }
}
```


### Performance Optimization

**Lazy Composition:**
- Use `derivedStateOf` for computed animation values
- Avoid unnecessary recompositions during animations
- Use `remember` for animation state that doesn't need to trigger recomposition

**Hardware Acceleration:**
- Ensure all animated properties use hardware-accelerated layers
- Use `graphicsLayer` modifier for scale, rotation, and alpha animations
- Avoid animating layout properties when possible

**Animation Cancellation:**
- Cancel ongoing animations when new ones start
- Use `animateTo` with proper cancellation handling
- Clean up animation resources in DisposableEffect

## Visual Design Enhancements

### Color Transitions

Smooth color transitions for state changes:
- Mode selection: Animate between primary and surface colors
- Error states: Animate to error container color
- Success states: Animate to tertiary container color
- Duration: 200ms with FastOutSlowIn easing

### Elevation Changes

Dynamic elevation for depth perception:
- Pressed state: Reduce elevation by 2dp
- Hover state: Increase elevation by 2dp
- Animate elevation changes over 150ms
- Use Material 3 elevation tokens

### Shape Morphing

Subtle shape transitions for visual interest:
- Cards can morph corner radius on expand
- Bottom sheet corners animate during slide
- Smooth transitions maintain visual continuity


## Dependencies

### Required Libraries

```kotlin
// Compose Animation
implementation("androidx.compose.animation:animation:1.5.4")
implementation("androidx.compose.animation:animation-graphics:1.5.4")

// Material 3 with animation support
implementation("androidx.compose.material3:material3:1.1.2")

// Accompanist for additional animation utilities
implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0")

// Image loading with crossfade
implementation("io.coil-kt:coil-compose:2.5.0")
```

### Gradle Configuration

Ensure Compose compiler is properly configured:

```kotlin
android {
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}
```

## Migration Strategy

### Phase 1: Foundation
1. Create AnimationConstants and extension functions
2. Implement reusable animated components
3. Add accessibility support

### Phase 2: Screen Updates
1. Update DashboardScreen with entrance animations
2. Update AnalysisScreen with modal and result animations
3. Update HistoryScreen with list and expand animations

### Phase 3: Polish
1. Fine-tune animation timings based on user feedback
2. Optimize performance on target devices
3. Add visual regression tests

### Phase 4: Validation
1. Conduct user testing
2. Measure performance metrics
3. Gather feedback and iterate
