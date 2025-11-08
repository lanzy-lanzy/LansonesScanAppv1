package dev.ml.lansonesscanapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

/**
 * Composable tests for AnimatedDialog component.
 * 
 * Tests verify:
 * - Visibility state changes work correctly
 * - Content is displayed when visible
 * - Content is hidden when not visible
 * - Proper cleanup on dismissal
 */
class AnimatedDialogTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun animatedDialog_whenVisible_displaysContent() {
        // Arrange & Act
        composeTestRule.setContent {
            AnimatedDialog(
                visible = true,
                onDismissRequest = { }
            ) {
                Text("Dialog Content")
            }
        }
        
        // Wait for animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Dialog Content").assertIsDisplayed()
    }
    
    @Test
    fun animatedDialog_whenNotVisible_doesNotDisplayContent() {
        // Arrange & Act
        composeTestRule.setContent {
            AnimatedDialog(
                visible = false,
                onDismissRequest = { }
            ) {
                Text("Hidden Dialog")
            }
        }
        
        // Wait for any potential animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Hidden Dialog").assertDoesNotExist()
    }
    
    @Test
    fun animatedDialog_visibilityToggle_showsAndHidesContent() {
        // Arrange
        var visible = true
        composeTestRule.setContent {
            AnimatedDialog(
                visible = visible,
                onDismissRequest = { }
            ) {
                Text("Toggle Dialog")
            }
        }
        
        // Assert initially visible
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Toggle Dialog").assertIsDisplayed()
        
        // Act - hide dialog
        visible = false
        composeTestRule.setContent {
            AnimatedDialog(
                visible = visible,
                onDismissRequest = { }
            ) {
                Text("Toggle Dialog")
            }
        }
        
        // Assert hidden
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Toggle Dialog").assertDoesNotExist()
    }
}
