# Implementation Plan

- [x] 1. Create foundation utilities and data models





  - Create PreferencesManager.kt for tracking guide dismissal state
  - Create GuideStep data class with icon, title, description, and color fields
  - Define guide steps content (3 steps: Select Mode, Capture Image, Analyze)
  - _Requirements: 1.1, 1.3, 1.4_

-

- [x] 2. Implement visual enhancement components


- [x] 2.1 Create GradientBackground component


  - Implement GradientBackground.kt with vertical gradient using Brush.verticalGradient
  - Use primaryContainer (10% alpha) to surface color transition
  - Make component reusable with content parameter
  - _Requirements: 4.1_

- [x] 2.2 Create ImagePlaceholder component


  - Implement ImagePlaceholder.kt with dashed border card (200dp height)
  - Add centered camera/image icon (64dp) with 60% alpha
  - Include instructional text "Tap to select image"
  - Make entire area clickable with hover effect
  - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [x] 2.3 Create EnhancedResultCard component


  - Implement EnhancedResultCard.kt with improved typography hierarchy
  - Use headlineMedium (28sp) for title, bodyLarge (16sp) for description
  - Add icon badge for result type (32dp)
  - Include dividers and proper spacing (24dp padding)
  - Set line height to 1.6x for better readability
  - _Requirements: 6.1, 6.2, 6.3, 6.4_

- [x] 3. Implement HowItWorksGuide component





- [x] 3.1 Create guide UI structure


  - Implement HowItWorksGuide.kt as modal dialog with scrim overlay
  - Add HorizontalPager for step navigation
  - Create GuideStepContent composable for individual step display
  - Include large icon (64dp), title, and description for each step
  - _Requirements: 1.2, 2.1, 2.2_

- [x] 3.2 Add guide navigation controls


  - Implement page indicators showing current position (dots)
  - Add Previous button (hidden on first page)
  - Add Next button (changes to "Got it" on last page)
  - Wire up pager state to navigation buttons
  - _Requirements: 3.1, 3.2, 3.3_

- [x] 3.3 Add guide animations and dismissal


  - Implement fade-in animation when guide appears (300ms)
  - Add smooth page transition animations between steps (300ms)
  - Enable dismissal by tapping outside the guide (scrim)
  - Add fade-out animation on dismissal
  - _Requirements: 2.3, 3.4_

- [x] 4. Enhance mode selection UI




- [x] 4.1 Create EnhancedModeChip component


  - Implement EnhancedModeChip.kt with icon + text layout
  - Add fruit icon (Apple/Restaurant) for FRUIT mode
  - Add leaf icon (Eco/Nature) for LEAF mode
  - Set icon size to 20dp with 8dp spacing from text
  - Apply enhanced styling: 16dp horizontal padding, 12dp vertical padding, 16dp corner radius
  - _Requirements: 4.3, 8.1_

- [x] 4.2 Add mode description display


  - Add AnimatedContent below mode chips showing description
  - Display "Analyze fruit ripeness and quality" for FRUIT mode
  - Display "Detect leaf diseases and health" for LEAF mode
  - Animate description changes with 200ms fade transition
  - _Requirements: 8.2_
-

- [x] 5. Integrate visual enhancements into AnalysisScreen



- [x] 5.1 Apply gradient background


  - Wrap AnalysisScreen content with GradientBackground component
  - Ensure gradient doesn't interfere with content readability
  - Test with both light and dark themes
  - _Requirements: 4.1, 4.4_

- [x] 5.2 Replace mode chips with enhanced version


  - Replace AnimatedModeChip with EnhancedModeChip
  - Add mode description display below chips
  - Maintain existing animation behavior
  - _Requirements: 4.3, 8.1, 8.2, 8.3, 8.4_

- [x] 5.3 Add image placeholder for empty state


  - Display ImagePlaceholder when no image is selected
  - Replace placeholder with image preview when image selected
  - Wire up placeholder click to show bottom sheet
  - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [x] 5.4 Replace result card with enhanced version


  - Replace existing result Card with EnhancedResultCard component
  - Pass result data to new component
  - Maintain existing animations
  - _Requirements: 6.1, 6.2, 6.3, 6.4_
-

- [x] 6. Integrate guide system into AnalysisScreen





- [x] 6.1 Add guide state management

  - Extend AnalysisUiState with showGuide and hasSeenGuide fields
  - Add ViewModel functions: showGuide(), dismissGuide(), checkGuideStatus()
  - Initialize PreferencesManager in ViewModel
  - Check guide status on ViewModel initialization
  - _Requirements: 1.1, 1.3_

- [x] 6.2 Add help button to screen


  - Add IconButton with HelpOutline icon in top-right corner
  - Position using Box with Alignment.TopEnd
  - Set size to 48x48dp for proper touch target
  - Wire up to show guide when clicked
  - Add tooltip "How it Works" on long press
  - _Requirements: 1.4, 9.1, 9.2, 9.3, 9.4_

- [x] 6.3 Implement first-time user flow

  - Show guide automatically when hasSeenGuide is false
  - Call PreferencesManager.setGuideAsSeen() when guide dismissed
  - Ensure guide only auto-shows once per installation
  - _Requirements: 1.1, 1.3_

- [x] 6.4 Wire up guide component

  - Add HowItWorksGuide to AnalysisScreen composable
  - Pass showGuide state and dismissGuide callback
  - Ensure guide appears above all other content
  - Test dismissal by tapping outside
  - _Requirements: 1.2, 1.4, 3.4_

- [x] 7. Add visual feedback enhancements





- [x] 7.1 Implement success indicator


  - Show checkmark icon briefly when image selected (1 second)
  - Use AnimatedVisibility with fade and scale animations
  - Position over image preview
  - _Requirements: 7.2_

- [x] 7.2 Enhance loading state


  - Add descriptive text below loading indicator ("Analyzing your image...")
  - Ensure loading indicator is prominent and centered
  - Maintain existing pulsing animation
  - _Requirements: 7.1_

- [x] 7.3 Improve error state display


  - Add error icon (ErrorOutline) to error card
  - Ensure error message is clear and actionable
  - Maintain existing shake animation
  - _Requirements: 7.3_

- [x] 7.4 Add visual cues for interactive elements


  - Ensure all buttons have proper elevation and shadows
  - Add subtle hover effects where applicable
  - Use ripple effects for all clickable elements
  - _Requirements: 7.4_

- [x] 8. Apply Material Design 3 spacing and styling





  - Update all spacing to use Material Design 3 tokens (4dp, 8dp, 16dp, 24dp, 32dp)
  - Apply consistent card elevation (2dp default, 3dp for results)
  - Set corner radius to 16dp for cards, 12dp for chips
  - Ensure proper margins and padding throughout screen
  - _Requirements: 4.2, 4.4, 10.1, 10.2, 10.3, 10.4_


- [x] 9. Implement accessibility features




- [x] 9.1 Add content descriptions


  - Add contentDescription to all icons (help button, mode icons, placeholder icon)
  - Ensure guide steps are properly labeled for screen readers
  - Add semantic labels to page indicators
  - _Requirements: 9.1, 9.2, 9.3_

- [x] 9.2 Ensure proper touch targets


  - Verify all interactive elements are minimum 48x48dp
  - Test help button, mode chips, and navigation buttons
  - Adjust padding if needed to meet accessibility standards
  - _Requirements: 9.1, 9.2_

- [x] 9.3 Test with accessibility services


  - Test with TalkBack enabled
  - Verify guide navigation with screen reader
  - Test with large text settings
  - Ensure animations respect system settings
  - _Requirements: 9.1, 9.2, 9.3, 9.4_

- [ ]* 10. Create tests for new components
- [ ]* 10.1 Write unit tests for PreferencesManager
  - Test hasSeenGuide() returns correct default value
  - Test setGuideAsSeen() persists state correctly
  - Test resetGuide() functionality
  - _Requirements: 1.1, 1.3_

- [ ]* 10.2 Write composable tests for guide component
  - Test HowItWorksGuide visibility state changes
  - Test page navigation (Next/Previous buttons)
  - Test dismissal behavior (tap outside, Got it button)
  - Verify correct number of steps displayed
  - _Requirements: 1.2, 2.3, 3.1, 3.2, 3.3, 3.4_

- [ ]* 10.3 Write composable tests for visual components
  - Test ImagePlaceholder click handling
  - Test EnhancedModeChip icon display for each mode
  - Test EnhancedResultCard content rendering
  - Test GradientBackground applies correctly
  - _Requirements: 4.3, 5.1, 5.2, 6.1, 8.1_

- [ ]* 10.4 Write UI tests for first-time user flow
  - Test guide appears automatically on first launch
  - Navigate through all guide steps
  - Dismiss guide and verify it doesn't reappear
  - Test help button reopens guide
  - _Requirements: 1.1, 1.3, 1.4, 9.1, 9.2_

- [ ]* 10.5 Write accessibility tests
  - Test with TalkBack enabled
  - Verify content descriptions are present
  - Test touch target sizes
  - Verify animations respect system settings
  - _Requirements: 9.1, 9.2, 9.3, 9.4_
