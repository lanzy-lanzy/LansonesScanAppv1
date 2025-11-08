package dev.ml.lansonesscanapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * Composable tests for AnimatedCard component.
 * 
 * Tests verify:
 * - Press animation triggers and completes
 * - Entrance animation works correctly
 * - Click handling functions properly
 */
class AnimatedCardTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun animatedCard_displaysContent() {
        // Arrange & Act
        composeTestRule.setContent {
            AnimatedCard(
                onClick = { }
            ) {
                Text("Test Content")
            }
        }
        
        // Assert
        composeTestRule.onNodeWithText("Test Content").assertIsDisplayed()
    }
    
    @Test
    fun animatedCard_clickTriggersCallback() {
        // Arrange
        var clicked = false
        composeTestRule.setContent {
            AnimatedCard(
                onClick = { clicked = true }
            ) {
                Text("Clickable Card")
            }
        }
        
        // Act
        composeTestRule.onNodeWithText("Clickable Card").performClick()
        
        // Assert
        assert(clicked) { "Click callback should be triggered" }
    }
    
    @Test
    fun animatedCard_withEntranceAnimation_displaysContent() {
        // Arrange & Act
        composeTestRule.setContent {
            AnimatedCard(
                onClick = { },
                enterAnimation = true
            ) {
                Text("Animated Entry")
            }
        }
        
        // Wait for animation to complete
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Animated Entry").assertIsDisplayed()
    }
    
    @Test
    fun animatedCard_withDelay_eventuallyDisplaysContent() {
        // Arrange & Act
        composeTestRule.setContent {
            AnimatedCard(
                onClick = { },
                enterAnimation = true,
                enterDelay = 100
            ) {
                Text("Delayed Entry")
            }
        }
        
        // Wait for animation and delay to complete
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Delayed Entry").assertIsDisplayed()
    }
}
