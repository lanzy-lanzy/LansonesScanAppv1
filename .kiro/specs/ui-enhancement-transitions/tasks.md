# Implementation Plan

- [x] 1. Create animation foundation and utilities





  - Create AnimationConstants.kt with centralized timing, easing curves, and spring specifications
  - Create AnimationExtensions.kt with reusable modifier functions (pressAnimation, shakeAnimation, slideInFromBottom)
  - Create accessibility helper function to detect system animation preferences
  - _Requirements: 1.4, 2.1, 2.2, 11.2_
-

- [x] 2. Implement reusable animated components




- [x] 2.1 Create AnimatedCard component


  - Implement AnimatedCard.kt with press scale animation and optional entrance animation
  - Add configurable delay parameter for staggered animations
  - Integrate interactionSource for press detection
  - _Requirements: 2.1, 2.2, 12.3, 12.4_
-

- [ ] 2.2 Create AnimatedDialog component









  - Implement AnimatedDialog.kt with scale and fade entrance/exit animations
  - Add backdrop fade animation with 40% opacity
  - Use overshoot interpolator for scale animation
  - _Requirements: 3.1, 3.2, 3.3, 3.4_

- [x] 2.3 Create ImageSelectionBottomSheet component


  - Implement ModalBottomSheet for camera and gallery selection
  - Add slide up/down animations with proper timing
  - Include scrim overlay with fade animation
  - Design large touch targets with icon + text layout
  - _Requirements: 4.1, 4.2, 4.3, 4.4_

- [x] 2.4 Create ExpandableCard component


  - Implement ExpandableCard.kt with animateContentSize for height transitions
  - Add AnimatedVisibility for content with staggered fade timing
  - Use spring animation for natural expansion feel
  - _Requirements: 8.1, 8.2, 8.3, 8.4_
-

- [x] 3. Enhance DashboardScreen with animations




  - Add entrance animation sequence using LaunchedEffect and Animatable
  - Implement staggered fade-in for icon (0ms), title (100ms), scan card (200ms), history card (300ms)
  - Replace standard Cards with AnimatedCard components
  - Add slide-up animation for cards from bottom
  - _Requirements: 12.1, 12.2, 12.3, 12.4, 2.1, 2.2_

-

- [x] 4. Enhance AnalysisScreen with animations




- [x] 4.1 Implement animated mode selection


  - Add animateDpAsState for indicator position animation between Fruit/Leaf modes
  - Implement color transition animation for selected chip (200ms)
  - Add subtle scale animation (1.05x) for selected chip
  - Handle rapid mode changes with animation cancellation
  - _Requirements: 5.1, 5.2, 5.3, 5.4_

- [x] 4.2 Replace image selection with bottom sheet modal


  - Integrate ImageSelectionBottomSheet component
  - Replace existing camera/gallery buttons with single "Select Image" button
  - Wire up camera and gallery launchers to bottom sheet callbacks
  - _Requirements: 4.1, 4.2, 4.3, 4.4_

- [x] 4.3 Add image preview animations


  - Implement fade-in animation (400ms) when image is selected
  - Add subtle scale animation from 0.95 to 1.0 for image preview
  - Animate analyze button slide-up with fade-in (300ms)
  - Add shimmer effect during image loading using Coil's crossfade
  - _Requirements: 6.1, 6.2, 6.3, 6.4_

- [x] 4.4 Implement result display animations


  - Add fade-in and slide-up animation for result card (400ms, 50dp offset)
  - Implement staggered animation for title and description (100ms delay)
  - Use AnimatedVisibility with slideInVertically and fadeIn
  - _Requirements: 7.1, 7.2, 7.3_

- [x] 4.5 Add loading and error animations


  - Implement pulsing scale animation for analyze button during loading (0.95 to 1.05, 1000ms cycle)
  - Add slide-in from top animation for error messages (300ms)
  - Implement shake animation for error card (3 oscillations, 400ms)
  - Add fade-out animation when error is dismissed (250ms)
  - _Requirements: 7.4, 11.1, 11.2, 11.3_

-

- [x] 5. Enhance HistoryScreen with animations




- [x] 5.1 Implement list item animations


  - Add AnimatedVisibility with slideInHorizontally for new items (from right, 300ms)
  - Add slideOutHorizontally for deleted items (to right, 250ms)
  - Implement simultaneous fade animations with slide
  - Use key-based animations for proper item tracking in LazyColumn
  - _Requirements: 9.1, 9.2, 9.3, 9.4_

- [x] 5.2 Convert HistoryItem to use ExpandableCard


  - Replace existing Card with ExpandableCard component
  - Move thumbnail and header content to header parameter
  - Move description content to expandedContent parameter
  - Ensure delete button remains accessible in collapsed state
  - _Requirements: 8.1, 8.2, 8.3, 8.4_

- [x] 5.3 Add FAB animations


  - Implement scale-in animation for FAB when items exist (300ms with 200ms delay)
  - Add scale-out animation when list becomes empty (200ms)
  - Implement press/release scale animations (0.9x on press, 100ms)
  - Use AnimatedVisibility with scaleIn and scaleOut transitions
  - _Requirements: 10.1, 10.2, 10.3, 10.4_


- [x] 5.4 Enhance dialog animations

  - Replace standard AlertDialog with AnimatedDialog component for delete confirmation
  - Replace standard AlertDialog with AnimatedDialog component for clear all confirmation
  - _Requirements: 3.1, 3.2, 3.3, 3.4_


- [x] 5.5 Add empty state animation

  - Implement crossfade between empty state and list content
  - Add fade-in animation when list becomes empty (400ms)
  - Add fade-out animation when first item is added (300ms)
  - _Requirements: 9.1, 9.2_

-

- [x] 6. Implement navigation transitions




  - Configure NavHost with enterTransition and exitTransition
  - Add fadeIn + slideIntoContainer for screen entry (300ms)
  - Add fadeOut + slideOutOfContainer for screen exit (300ms)
  - Use Material Design easing curves for natural motion
  - Implement proper back navigation transitions (reverse animations)
  - _Requirements: 1.1, 1.2, 1.3, 1.4_

- [x] 7. Add performance optimizations




- [ ] 7. Add performance optimizations
  - Implement device performance tier detection
  - Create adaptive animation duration function based on device tier
  - Add graphicsLayer modifiers for hardware-accelerated animations
  - Use derivedStateOf for computed animation values to reduce recompositions
  - Add DisposableEffect for proper animation cleanup on screen disposal
  - _Requirements: 1.1, 1.2, 1.3, 2.1, 2.2_
-

- [x] 8. Create animation tests




- [x] 8.1 Write unit tests for AnimationConstants


  - Test duration values are within acceptable ranges (50-1000ms)
  - Verify easing functions produce expected output values
  - Test spring specifications for stability
  - _Requirements: All_

- [x] 8.2 Write composable tests for animated components


  - Test AnimatedCard press animation triggers and completes
  - Test AnimatedDialog visibility state changes and cleanup
  - Test ExpandableCard height transitions and content visibility
  - Test ImageSelectionBottomSheet slide animations
  - _Requirements: 2.1, 2.2, 3.1, 3.2, 4.1, 8.1_

- [x] 8.3 Write UI tests for screen animations



  - Test navigation transitions between all screens
  - Test DashboardScreen entrance animation sequence
  - Test AnalysisScreen result display animations
  - Test HistoryScreen list item animations
  - Verify animations respect accessibility settings
  - _Requirements: 1.1, 1.2, 1.3, 12.1, 12.2, 12.3, 12.4, 7.1, 7.2, 9.1, 9.2_
