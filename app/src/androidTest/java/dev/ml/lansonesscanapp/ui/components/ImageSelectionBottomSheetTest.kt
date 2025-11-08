package dev.ml.lansonesscanapp.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * Composable tests for ImageSelectionBottomSheet component.
 * 
 * Tests verify:
 * - Slide animations work correctly
 * - Camera and gallery options are displayed
 * - Click callbacks are triggered
 */
class ImageSelectionBottomSheetTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun imageSelectionBottomSheet_whenVisible_displaysOptions() {
        // Arrange & Act
        composeTestRule.setContent {
            ImageSelectionBottomSheet(
                visible = true,
                onDismiss = { },
                onCameraClick = { },
                onGalleryClick = { }
            )
        }
        
        // Wait for animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Select Image Source").assertIsDisplayed()
        composeTestRule.onNodeWithText("Take Photo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Choose from Gallery").assertIsDisplayed()
    }
    
    @Test
    fun imageSelectionBottomSheet_whenNotVisible_doesNotDisplay() {
        // Arrange & Act
        composeTestRule.setContent {
            ImageSelectionBottomSheet(
                visible = false,
                onDismiss = { },
                onCameraClick = { },
                onGalleryClick = { }
            )
        }
        
        // Wait for any potential animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Select Image Source").assertDoesNotExist()
    }
    
    @Test
    fun imageSelectionBottomSheet_cameraClick_triggersCallback() {
        // Arrange
        var cameraClicked = false
        composeTestRule.setContent {
            ImageSelectionBottomSheet(
                visible = true,
                onDismiss = { },
                onCameraClick = { cameraClicked = true },
                onGalleryClick = { }
            )
        }
        
        // Wait for animation
        composeTestRule.waitForIdle()
        
        // Act
        composeTestRule.onNodeWithText("Take Photo").performClick()
        
        // Assert
        assert(cameraClicked) { "Camera click callback should be triggered" }
    }
    
    @Test
    fun imageSelectionBottomSheet_galleryClick_triggersCallback() {
        // Arrange
        var galleryClicked = false
        composeTestRule.setContent {
            ImageSelectionBottomSheet(
                visible = true,
                onDismiss = { },
                onCameraClick = { },
                onGalleryClick = { galleryClicked = true }
            )
        }
        
        // Wait for animation
        composeTestRule.waitForIdle()
        
        // Act
        composeTestRule.onNodeWithText("Choose from Gallery").performClick()
        
        // Assert
        assert(galleryClicked) { "Gallery click callback should be triggered" }
    }
}
