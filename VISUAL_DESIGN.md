# Visual Design Guide - Lansones Scan App

## 🎨 Color Palette

The app uses a beautiful, nature-inspired color scheme based on lansones fruits and leaves:

### Primary Colors
- **Light Yellow** (#FFFD8F) - Represents ripe lansones fruit
- **Light Green** (#B0CE88) - Fresh lansones leaves
- **Medium Green** (#4C763B) - Mature foliage
- **Dark Green** (#043915) - Deep forest tones

### Color Usage

#### Light Theme
- **Primary**: Medium Green (#4C763B)
- **Primary Container**: Light Green (#B0CE88)
- **Secondary**: Light Yellow (#FFFD8F)
- **Background**: Cream White (#FFFFF5)
- **Surface**: Pure White

#### Dark Theme
- **Primary**: Light Green (#B0CE88)
- **Primary Container**: Medium Green (#4C763B)
- **Secondary**: Light Yellow (#FFFD8F)
- **Background**: Dark Gray (#1A1C18)
- **Surface**: Charcoal (#2D2F2A)

## 🎭 Visual Elements

### Splash Screen
- **Duration**: 2.5 seconds
- **Animation**: Scale + Fade in
- **Background**: Gradient from primary container to background
- **Icon**: Spa leaf icon in circular background
- **Typography**: Bold app name with tagline

### Dashboard Screen
- **Background**: Subtle gradient overlay
- **Hero Icon**: Large Spa icon (80dp)
- **Primary Card**: Gradient button with shadow
  - Colors: Primary to Primary Container
  - Height: 140dp
  - Corner Radius: 20dp
  - Shadow: 8dp elevation
- **Secondary Card**: Outlined card
  - Border: 2dp
  - Height: 100dp
  - Corner Radius: 20dp

### Analysis Screen
- **Mode Chips**: FilterChip with custom colors
- **Image Preview**: Rounded card (300dp height)
- **Result Card**: Scrollable with secondary container color
  - Min Height: 200dp
  - Max Height: 600dp
  - Padding: 20dp
  - Line Height: 1.5x

### History Screen
- **Empty State**: Centered message
- **List Items**: Expandable cards
  - Thumbnail: 80dp square
  - Expanded Detail: Scrollable card (max 400dp)
- **FAB**: Error container color for delete action
  - Position: Bottom-end
  - Padding: 16dp

### Bottom Navigation
- **Container**: Surface container with 8dp elevation
- **Selected Item**: Primary container indicator
- **Icons**: Material Icons (Home, Scanner, History)
- **Labels**: Medium typography

## 📐 Spacing & Sizing

### Standard Spacing
- **Extra Small**: 4dp
- **Small**: 8dp
- **Medium**: 16dp
- **Large**: 24dp
- **Extra Large**: 48dp

### Component Sizes
- **Small Icon**: 20dp
- **Medium Icon**: 48dp
- **Large Icon**: 64dp
- **Hero Icon**: 80dp
- **Thumbnail**: 80dp square
- **Card Corner Radius**: 20dp
- **Button Height**: 56dp (default)

## 🎯 Typography

### Hierarchy
- **Headline Large**: App titles, bold
- **Headline Small**: Section headers
- **Title Large**: Card titles
- **Title Medium**: Subtitles
- **Body Large**: Main content
- **Body Medium**: Secondary content
- **Label Medium**: Navigation labels

### Font Weights
- **Bold**: 700 (Titles, headers)
- **SemiBold**: 600 (Subtitles)
- **Regular**: 400 (Body text)

## ✨ Animations

### Splash Screen
- **Scale Animation**: 0.5f → 1.0f
- **Alpha Animation**: 0f → 1f
- **Duration**: 1000ms
- **Easing**: Spring (Medium Bouncy)

### Bottom Navigation
- **Slide In**: From bottom
- **Slide Out**: To bottom
- **Duration**: 300ms (default)

### Card Interactions
- **Ripple Effect**: Material ripple
- **Elevation Change**: On press
- **State Transitions**: Smooth color changes

## 🎨 Gradients

### Background Gradients
```kotlin
Brush.verticalGradient(
    colors = listOf(
        primaryContainer.copy(alpha = 0.3f),
        background
    )
)
```

### Card Gradients
```kotlin
Brush.horizontalGradient(
    colors = listOf(
        primary,
        primaryContainer
    )
)
```

## 🖼️ Icons

### Material Icons Used
- **Spa**: Lansones leaf representation
- **Scanner**: Scan functionality
- **History**: Past scans
- **Home**: Dashboard
- **CameraAlt**: Camera capture
- **PhotoLibrary**: Gallery selection
- **Delete**: Remove items

## 📱 Responsive Design

### Breakpoints
- **Compact**: < 600dp (phones)
- **Medium**: 600-840dp (tablets)
- **Expanded**: > 840dp (large tablets)

### Adaptive Layouts
- Cards scale with screen width
- Text sizes remain consistent
- Padding adjusts for larger screens
- Bottom navigation always visible

## 🎪 User Experience

### Visual Feedback
- **Loading**: CircularProgressIndicator
- **Success**: Secondary container color
- **Error**: Error container color
- **Empty State**: Centered message with icon

### Accessibility
- **Contrast Ratios**: WCAG AA compliant
- **Touch Targets**: Minimum 48dp
- **Content Descriptions**: All interactive elements
- **Text Scaling**: Supports system font size

## 🚀 Performance

### Optimizations
- **Lazy Loading**: LazyColumn for history
- **Image Caching**: Coil library
- **State Management**: Remember and State
- **Recomposition**: Minimal recomposition scope

---

**Design System Version**: 1.0  
**Last Updated**: November 2024  
**Theme**: Material 3 with Custom Lansones Palette
