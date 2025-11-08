# Design Document

## Overview

This design enhances the Lansones Scan App's AnalysisScreen by adding an interactive "How it Works" guide and comprehensive visual improvements. The solution creates a more engaging, intuitive, and visually appealing user experience while maintaining the app's existing functionality and performance.

The design leverages Material Design 3 principles, Jetpack Compose's modern UI toolkit, and SharedPreferences for persistent user preferences. The guide system uses a pager-based approach with smooth animations, while visual enhancements include gradient backgrounds, improved card styling, enhanced iconography, and better typography hierarchy.

## Architecture

### Component Structure

```
ui/
├── components/
│   ├── HowItWorksGuide.kt              # Interactive guide component
│   ├── GradientBackground.kt           # Reusable gradient background
│   ├── EnhancedModeChip.kt            # Mode chip with icons
│   ├── ImagePlaceholder.kt            # Empty state placeholder
│   └── EnhancedResultCard.kt          # Improved result display
├── screens/
│   └── AnalysisScreen.kt              # Enhanced with guide and visuals
└── utils/
    └── PreferencesManager.kt          # Guide dismissal tracking

viewmodel/
└── AnalysisViewModel.kt               # Extended with guide state
```

### Key Design Principles

1. **User-Centric**: Guide appears for first-time users, easily accessible later
2. **Visual Hierarchy**: Clear organization with proper spacing and elevation
3. **Consistency**: Unified design language across all components
4. **Performance**: Lightweight animations and efficient rendering
5. **Accessibility**: High contrast, readable text, and proper touch targets

## Components and Interfaces

### 1. HowItWorksGuide Component

Interactive guide displayed as a modal dialog with pager navigation:

```kotlin
@Composable
fun HowItWorksGuide(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**
- Modal dialog with scrim overlay
- Horizontal pager for step navigation
- Page indicators showing progress
- Next/Previous/Got it buttons
- Smooth page transitions (300ms)
- Dismissible by tapping outside

**Guide Steps:**

**Step 1: Select Mode**
- Icon: Tune/Settings icon
- Title: "Choose Your Scan Mode"
- Description: "Select 'Fruit' to analyze lansones fruit ripeness and quality, or 'Leaf' to detect diseases and health issues."
- Color: Primary container

**Step 2: Capture Image**
- Icon: Camera icon
- Title: "Capture or Select Image"
- Description: "Take a photo with your camera or choose an existing image from your gallery. Make sure the image is clear and well-lit."
- Color: Secondary container

**Step 3: Analyze**
- Icon: Search/Analytics icon
- Title: "Get Instant Analysis"
- Description: "Tap 'Analyze Image' and our AI will process your image and provide detailed insights about your lansones."
- Color: Tertiary container

**Layout Structure:**
```
┌─────────────────────────────┐
│  [Icon]                     │
│                             │
│  Step Title                 │
│                             │
│  Step description text      │
│  explaining the feature     │
│                             │
│  ● ○ ○  (Page indicators)   │
│                             │
│  [Previous]  [Next/Got it]  │
└─────────────────────────────┘
```

### 2. GradientBackground Component

Subtle gradient background for visual appeal:

```kotlin
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
```

**Features:**
- Vertical gradient from primary container (10% alpha) to surface
- Smooth color transitions
- Doesn't interfere with content readability
- Adapts to theme colors

**Gradient Specification:**
- Start: primaryContainer with 10% alpha
- End: surface color
- Direction: Top to bottom
- Angle: 180 degrees

### 3. EnhancedModeChip Component

Mode selection chip with icons and descriptions:

```kotlin
@Composable
fun EnhancedModeChip(
    mode: ScanMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**
- Icon + text layout
- Fruit icon: Apple or similar fruit icon
- Leaf icon: Eco/Nature leaf icon
- Animated selection state
- Tooltip description on long press
- Enhanced visual feedback

**Visual Specifications:**
- Icon size: 20dp
- Icon-text spacing: 8dp
- Padding: 16dp horizontal, 12dp vertical
- Border width: 2dp when selected
- Corner radius: 16dp

### 4. ImagePlaceholder Component

Engaging empty state for image selection:

```kotlin
@Composable
fun ImagePlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)
```

**Features:**
- Large dashed border card
- Centered icon (image/photo icon)
- Instructional text
- Hover effect
- Clickable entire area

**Layout:**
```
┌─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐
│                          │
│        [📷 Icon]         │
│                          │
│   Tap to select image    │
│                          │
└─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
```

**Visual Specifications:**
- Height: 200dp
- Border: 2dp dashed, outline color
- Icon size: 64dp
- Icon color: primary with 60% alpha
- Corner radius: 16dp
- Hover: Slight elevation increase

### 5. EnhancedResultCard Component

Improved result display with better organization:

```kotlin
@Composable
fun EnhancedResultCard(
    result: AnalysisResult,
    modifier: Modifier = Modifier
)
```

**Features:**
- Clear visual hierarchy
- Icon badge for result type
- Larger title typography
- Organized content sections
- Color accents for emphasis
- Proper spacing and dividers

**Layout Structure:**
```
┌─────────────────────────────┐
│ [Icon] Result Title         │
│ ─────────────────────────   │
│                             │
│ Description text with       │
│ proper line height and      │
│ spacing for readability     │
│                             │
│ • Key point 1               │
│ • Key point 2               │
│                             │
└─────────────────────────────┘
```

**Visual Specifications:**
- Title: headlineMedium (28sp)
- Description: bodyLarge (16sp)
- Line height: 1.6x
- Padding: 24dp
- Divider: 1dp, outline color with 20% alpha
- Icon size: 32dp

### 6. PreferencesManager

Manages guide dismissal state:

```kotlin
class PreferencesManager(context: Context) {
    private val prefs = context.getSharedPreferences(
        "lansones_scan_prefs",
        Context.MODE_PRIVATE
    )
    
    fun hasSeenGuide(): Boolean
    fun setGuideAsSeen()
    fun resetGuide() // For testing
}
```

**Storage:**
- Key: "has_seen_how_it_works_guide"
- Type: Boolean
- Default: false

## Data Models

### GuideStep

Represents a single step in the guide:

```kotlin
data class GuideStep(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val containerColor: Color
)
```

### AnalysisUiState Extension

Extended to include guide state:

```kotlin
data class AnalysisUiState(
    // Existing fields...
    val showGuide: Boolean = false,
    val hasSeenGuide: Boolean = false
)
```

## Screen Layout Enhancements

### AnalysisScreen Visual Structure

```
┌─────────────────────────────────────┐
│ [?] Help Button          (Top-right)│
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ Gradient Background             │ │
│ │                                 │ │
│ │ Select Scan Mode                │ │
│ │ ┌──────────┐  ┌──────────┐     │ │
│ │ │🍎 Fruit  │  │🍃 Leaf   │     │ │
│ │ └──────────┘  └──────────┘     │ │
│ │                                 │ │
│ │ Mode description text           │ │
│ │                                 │ │
│ │ ┌─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐   │ │
│ │ │   [📷] Image Placeholder │   │ │
│ │ │   Tap to select image    │   │ │
│ │ └─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘   │ │
│ │                                 │ │
│ │ [Select Image Button]           │ │
│ │                                 │ │
│ │ [Result Card if available]      │ │
│ │                                 │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

### Visual Enhancements Applied

**1. Spacing System (Material Design 3)**
- Extra small: 4dp
- Small: 8dp
- Medium: 16dp
- Large: 24dp
- Extra large: 32dp

**2. Card Elevation**
- Default: 2dp
- Hover: 4dp
- Pressed: 1dp
- Result card: 3dp

**3. Corner Radius**
- Small components: 12dp
- Cards: 16dp
- Dialogs: 28dp

**4. Typography Scale**
- Display: 57sp (Guide titles)
- Headline Medium: 28sp (Result titles)
- Title Large: 22sp (Section headers)
- Title Medium: 16sp (Labels)
- Body Large: 16sp (Content)
- Body Medium: 14sp (Secondary text)

**5. Color Usage**
- Primary: Mode selection, buttons, accents
- Secondary: Alternative actions
- Tertiary: Highlights, badges
- Error: Error states
- Surface variants: Card backgrounds
- Outline: Borders, dividers

## User Flow

### First-Time User Experience

1. User opens AnalysisScreen
2. PreferencesManager checks if guide has been seen
3. If not seen, guide automatically appears with fade-in animation
4. User navigates through 3 steps using Next button
5. On last step, "Got it" button dismisses guide
6. PreferencesManager marks guide as seen
7. User proceeds with normal app usage

### Returning User Experience

1. User opens AnalysisScreen
2. Guide doesn't appear (already seen)
3. Help button visible in top-right corner
4. User can tap help button anytime to reopen guide
5. Guide appears with same animations
6. User can dismiss by tapping outside or completing steps

### Image Selection Flow

1. User sees prominent placeholder with dashed border
2. Placeholder shows camera icon and instructional text
3. User taps placeholder or "Select Image" button
4. Bottom sheet appears with camera/gallery options
5. After selection, placeholder replaced with image preview
6. Success checkmark briefly appears
7. Analyze button becomes prominent

## Error Handling

### Guide Display Issues

**Problem:** Guide fails to load or display
**Solution:**
- Graceful fallback to standard screen
- Log error for debugging
- Don't block user from using app
- Show help button regardless

### Preferences Storage Failure

**Problem:** SharedPreferences unavailable or corrupted
**Solution:**
- Default to showing guide (better UX)
- Use in-memory fallback for session
- Log error for investigation
- Don't crash app

### Animation Performance

**Problem:** Animations lag on low-end devices
**Solution:**
- Use existing adaptive animation system
- Reduce guide animation complexity on low-tier devices
- Ensure guide remains functional without animations

## Accessibility Considerations

### Screen Reader Support

- All icons have content descriptions
- Guide steps are properly labeled
- Page indicators announce current position
- Buttons have clear action descriptions

### Touch Targets

- Minimum 48dp touch targets for all interactive elements
- Help button: 48x48dp
- Navigation buttons: 48dp height
- Mode chips: 48dp minimum height

### Color Contrast

- All text meets WCAG AA standards (4.5:1 minimum)
- Icons have sufficient contrast
- Gradient doesn't reduce readability
- Error states use high contrast colors

### Motion Sensitivity

- Respect system animation settings
- Reduce motion when accessibility settings enabled
- Guide still functional without animations
- Instant transitions when animations disabled

## Testing Strategy

### Unit Tests

**PreferencesManager Tests:**
- Test guide seen state persistence
- Test reset functionality
- Test default values
- Test concurrent access

**GuideStep Tests:**
- Verify all steps have required data
- Test step count (should be 3)
- Validate icon resources exist

### Composable Tests

**HowItWorksGuide Tests:**
- Test visibility state changes
- Verify page navigation
- Test dismissal behavior
- Ensure proper cleanup

**EnhancedModeChip Tests:**
- Test icon display for each mode
- Verify selection state
- Test click handling
- Validate accessibility labels

**ImagePlaceholder Tests:**
- Test click handling
- Verify icon and text display
- Test hover effects (if applicable)

### UI Tests

**First-Time User Flow:**
- Launch app for first time
- Verify guide appears automatically
- Navigate through all steps
- Dismiss guide
- Verify guide doesn't reappear

**Help Button Flow:**
- Launch app (guide already seen)
- Tap help button
- Verify guide appears
- Dismiss guide
- Verify app state unchanged

**Visual Regression:**
- Capture screenshots of enhanced screen
- Compare gradient rendering
- Verify card styling
- Check spacing and alignment

### Accessibility Tests

- Run with TalkBack enabled
- Test with large text settings
- Verify with high contrast mode
- Test with animations disabled

## Implementation Details

### Gradient Implementation

Use Brush.verticalGradient with theme colors:

```kotlin
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val surface = MaterialTheme.colorScheme.surface
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryContainer.copy(alpha = 0.1f),
                        surface
                    )
                )
            )
    ) {
        content()
    }
}
```

### Guide Pager Implementation

Use HorizontalPager from accompanist or Compose foundation:

```kotlin
val pagerState = rememberPagerState(pageCount = { guideSteps.size })

HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxWidth()
) { page ->
    GuideStepContent(step = guideSteps[page])
}
```

### Help Button Placement

Position in top-right corner using Scaffold or Box:

```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    // Main content
    AnalysisContent()
    
    // Help button overlay
    IconButton(
        onClick = { showGuide = true },
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.HelpOutline,
            contentDescription = "How it Works"
        )
    }
}
```

### Mode Description Display

Show description below chips based on selection:

```kotlin
AnimatedContent(
    targetState = selectedMode,
    transitionSpec = {
        fadeIn(tween(200)) with fadeOut(tween(200))
    }
) { mode ->
    Text(
        text = when (mode) {
            ScanMode.FRUIT -> "Analyze fruit ripeness and quality"
            ScanMode.LEAF -> "Detect leaf diseases and health"
        },
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
```

## Dependencies

### Required Libraries

```kotlin
// Existing dependencies (already in project)
implementation("androidx.compose.material3:material3:1.1.2")
implementation("androidx.compose.animation:animation:1.5.4")
implementation("io.coil-kt:coil-compose:2.5.0")

// New dependencies
implementation("androidx.compose.foundation:foundation:1.5.4") // For HorizontalPager
implementation("androidx.datastore:datastore-preferences:1.0.0") // Optional: Better than SharedPreferences
```

### Icon Resources

Use Material Icons Extended for additional icons:

```kotlin
implementation("androidx.compose.material:material-icons-extended:1.5.4")
```

**Icons Needed:**
- HelpOutline (help button)
- Tune (mode selection step)
- CameraAlt (capture step)
- Analytics/Search (analyze step)
- Apple/Restaurant (fruit mode)
- Eco/Nature (leaf mode)
- Image/Photo (placeholder)
- CheckCircle (success indicator)

## Migration Strategy

### Phase 1: Foundation Components
1. Create PreferencesManager
2. Implement GradientBackground
3. Create GuideStep data model
4. Build HowItWorksGuide component

### Phase 2: Visual Enhancements
1. Implement EnhancedModeChip with icons
2. Create ImagePlaceholder component
3. Build EnhancedResultCard
4. Apply gradient background to screen

### Phase 3: Integration
1. Integrate guide into AnalysisScreen
2. Add help button
3. Connect PreferencesManager
4. Wire up first-time user flow

### Phase 4: Polish
1. Fine-tune animations
2. Adjust spacing and typography
3. Test accessibility
4. Gather user feedback

## Performance Considerations

### Memory Usage

- Guide steps: ~1KB in memory
- SharedPreferences: Minimal overhead
- Gradient: Hardware-accelerated, no performance impact
- Icons: Vector drawables, scalable without memory cost

### Rendering Performance

- Use remember for static content
- Avoid unnecessary recompositions
- Leverage derivedStateOf for computed values
- Use graphicsLayer for animations

### First Launch Time

- Guide loads asynchronously
- Doesn't block main content
- Preferences check is fast (<1ms)
- No network requests required

## Future Enhancements

### Potential Additions

1. **Interactive Tutorial**: Highlight actual UI elements during guide
2. **Video Demos**: Short clips showing scanning process
3. **Tips System**: Contextual tips based on user behavior
4. **Customization**: Let users choose color themes
5. **Advanced Mode**: Additional features for power users
6. **Feedback System**: In-app feedback collection
7. **Analytics**: Track which guide steps users spend most time on
8. **Localization**: Multi-language support for guide content
