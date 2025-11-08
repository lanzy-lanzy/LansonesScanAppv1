# Image Validation Implementation

## Overview
Added validation to ensure users upload the correct type of image (leaf or fruit) based on their selected scan mode. Invalid images are now saved with mode "UNKNOWN" instead of showing the original mode (LEAF or FRUIT).

## Changes Made

### 1. ScanMode.kt - Added UNKNOWN Mode
**Location**: `app/src/main/java/dev/ml/lansonesscanapp/model/ScanMode.kt`

#### New UNKNOWN Mode
- Added `UNKNOWN` as a third scan mode enum value
- Used to indicate invalid or unrecognized images
- Displayed in history as "UNKNOWN" instead of "LEAF" or "FRUIT"

### 2. GeminiClient.kt - Updated Prompts
**Location**: `app/src/main/java/dev/ml/lansonesscanapp/network/GeminiClient.kt`

#### Leaf Prompt Enhancement
- Added validation instruction at the beginning of the prompt
- If the image is NOT a lansones leaf, Gemini will respond with: `"INVALID_IMAGE: Please select image that is leaf of a lansones"`
- This ensures users who select "Leaf" mode but upload a fruit or other image get a clear error message

#### Fruit Prompt Enhancement
- Added validation instruction at the beginning of the prompt
- If the image is NOT a lansones fruit, Gemini will respond with: `"INVALID_IMAGE: Please select image that is fruit of a lansones"`
- This ensures users who select "Fruit" mode but upload a leaf or other image get a clear error message

#### UNKNOWN Mode Handling
- Added handling for UNKNOWN mode in the when expression
- Returns an error if someone tries to analyze with UNKNOWN mode (shouldn't happen in normal flow)

### 3. AnalysisViewModel.kt - Handle Invalid Images
**Location**: `app/src/main/java/dev/ml/lansonesscanapp/viewmodel/AnalysisViewModel.kt`

#### Updated `createScanResult()` Method
- Added check for responses starting with `"INVALID_IMAGE:"`
- When an invalid image is detected:
  - Mode is set to **`ScanMode.UNKNOWN`** (instead of keeping FRUIT or LEAF)
  - Title is set to **"Unknown"** (instead of "Fruit Analysis Result" or "Leaf Disease Analysis")
  - Description contains the error message (e.g., "Please select image that is leaf of a lansones")
  - Result is still saved to history with the "Unknown" title and UNKNOWN mode
  - User can see the error message in both the analysis screen and history
- Added UNKNOWN case in the when expression for completeness

### 4. EnhancedModeChip.kt - Display UNKNOWN Mode
**Location**: `app/src/main/java/dev/ml/lansonesscanapp/ui/components/EnhancedModeChip.kt`

#### UNKNOWN Mode Display
- Added handling for UNKNOWN mode in icon and label selection
- Label displays as "Unknown"
- Uses default Restaurant icon (same as Fruit)
- Ensures history items with UNKNOWN mode display correctly

## User Experience Flow

### Scenario 1: User selects "Leaf" but uploads a fruit image
1. User clicks "Leaf" button
2. User uploads an image of a lansones fruit
3. Gemini AI detects it's not a leaf
4. Response: Title = "Unknown", Description = "Please select image that is leaf of a lansones"
5. Result is saved to history with "Unknown" title
6. User can see the error in history and knows to upload the correct image type

### Scenario 2: User selects "Fruit" but uploads a leaf image
1. User clicks "Fruit" button
2. User uploads an image of a lansones leaf
3. Gemini AI detects it's not a fruit
4. Response: Title = "Unknown", Description = "Please select image that is fruit of a lansones"
5. Result is saved to history with "Unknown" title
6. User can see the error in history and knows to upload the correct image type

### Scenario 3: User uploads correct image type
1. User selects appropriate mode (Leaf or Fruit)
2. User uploads matching image
3. Normal analysis proceeds
4. Result shows proper title ("Fruit Analysis Result" or "Leaf Disease Analysis")
5. Detailed analysis is displayed and saved to history

## Display in UI

### Analysis Screen
- Shows the result card with title "Unknown" and error message
- User immediately sees they uploaded the wrong image type

### History Screen
- Invalid scans appear with title "Unknown"
- Mode is displayed as **"UNKNOWN"** (not FRUIT or LEAF)
- Clicking to expand shows the full error message
- User can delete these entries if desired
- Clear visual distinction from valid scans

## Benefits

1. **Clear Feedback**: Users get immediate, specific feedback about what went wrong
2. **Persistent Record**: Invalid attempts are saved to history for reference
3. **Easy Identification**: "Unknown" title makes it easy to spot invalid scans in history
4. **Guided Correction**: Error messages tell users exactly what type of image to upload
5. **No Data Loss**: Even invalid attempts are recorded, helping users learn

## Technical Notes

- Validation happens at the AI level (Gemini prompt)
- No changes to database schema required
- No changes to UI components required
- Backward compatible with existing scan results
- Error messages are user-friendly and actionable
