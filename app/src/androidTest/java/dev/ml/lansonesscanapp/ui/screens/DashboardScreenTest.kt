package dev.ml.lansonesscanapp.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for DashboardScreen animations.
 * 
 * Tests verify:
 * - Entrance animation sequence displays all elements
 * - Staggered animations complete successfully
 * - Navigation actions work correctly
 */
class DashboardScreenTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun dashboardScreen_displaysAllElements() {
        // Arrange & Act
        composeTestRule.setContent {
            DashboardScreen(
                onNavigateToAnalysis = { },
                onNavigateToHistory = { }
            )
        }
        
        // Wait for all animations to complete
        composeTestRule.waitForIdle()
        
        // Assert - all elements should be visible after animations
        composeTestRule.onNodeWithText("Welcome to\nLansones Scan").assertIsDisplayed()
        composeTestRule.onNodeWithText("AI-Powered Analysis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Identify fruit varieties and detect leaf diseases").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start Scanning").assertIsDisplayed()
        composeTestRule.onNodeWithText("View History").assertIsDisplayed()
    }
    
    @Test
    fun dashboardScreen_scanCardClick_triggersNavigation() {
        // Arrange
        var navigatedToAnalysis = false
        composeTestRule.setContent {
            DashboardScreen(
                onNavigateToAnalysis = { navigatedToAnalysis = true },
                onNavigateToHistory = { }
            )
        }
        
        // Wait for animations
        composeTestRule.waitForIdle()
        
        // Act
        composeTestRule.onNodeWithText("Start Scanning").performClick()
        
        // Assert
        assert(navigatedToAnalysis) { "Should navigate to analysis screen" }
    }
    
    @Test
    fun dashboardScreen_historyCardClick_triggersNavigation() {
        // Arrange
        var navigatedToHistory = false
        composeTestRule.setContent {
            DashboardScreen(
                onNavigateToAnalysis = { },
                onNavigateToHistory = { navigatedToHistory = true }
            )
        }
        
        // Wait for animations
        composeTestRule.waitForIdle()
        
        // Act
        composeTestRule.onNodeWithText("View History").performClick()
        
        // Assert
        assert(navigatedToHistory) { "Should navigate to history screen" }
    }
}
