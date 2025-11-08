package dev.ml.lansonesscanapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * Composable tests for ExpandableCard component.
 * 
 * Tests verify:
 * - Height transitions work correctly
 * - Content visibility changes based on expanded state
 * - Click handling toggles expansion
 */
class ExpandableCardTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun expandableCard_whenCollapsed_showsOnlyHeader() {
        // Arrange & Act
        composeTestRule.setContent {
            ExpandableCard(
                expanded = false,
                onExpandChange = { }
            ) {
                Text("Header Content")
            } expandedContent = {
                Text("Expanded Content")
            }
        }
        
        // Wait for any animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Header Content").assertIsDisplayed()
        composeTestRule.onNodeWithText("Expanded Content").assertDoesNotExist()
    }
    
    @Test
    fun expandableCard_whenExpanded_showsHeaderAndContent() {
        // Arrange & Act
        composeTestRule.setContent {
            ExpandableCard(
                expanded = true,
                onExpandChange = { }
            ) {
                Text("Header Content")
            } expandedContent = {
                Text("Expanded Content")
            }
        }
        
        // Wait for animation
        composeTestRule.waitForIdle()
        
        // Assert
        composeTestRule.onNodeWithText("Header Content").assertIsDisplayed()
        composeTestRule.onNodeWithText("Expanded Content").assertIsDisplayed()
    }
    
    @Test
    fun expandableCard_clickTogglesExpansion() {
        // Arrange
        var expanded = false
        composeTestRule.setContent {
            var isExpanded by remember { mutableStateOf(false) }
            ExpandableCard(
                expanded = isExpanded,
                onExpandChange = { isExpanded = it }
            ) {
                Text("Clickable Header")
            } expandedContent = {
                Text("Hidden Content")
            }
        }
        
        // Assert initially collapsed
        composeTestRule.onNodeWithText("Clickable Header").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hidden Content").assertDoesNotExist()
        
        // Act - click to expand
        composeTestRule.onNodeWithText("Clickable Header").performClick()
        composeTestRule.waitForIdle()
        
        // Assert expanded
        composeTestRule.onNodeWithText("Hidden Content").assertIsDisplayed()
        
        // Act - click to collapse
        composeTestRule.onNodeWithText("Clickable Header").performClick()
        composeTestRule.waitForIdle()
        
        // Assert collapsed again
        composeTestRule.onNodeWithText("Hidden Content").assertDoesNotExist()
    }
    
    @Test
    fun expandableCardNoClick_doesNotHandleClicksInternally() {
        // Arrange & Act
        composeTestRule.setContent {
            ExpandableCardNoClick(
                expanded = false
            ) {
                Text("No Click Header")
            } expandedContent = {
                Text("No Click Content")
            }
        }
        
        // Wait for any animation
        composeTestRule.waitForIdle()
        
        // Assert - content should remain hidden even after click
        composeTestRule.onNodeWithText("No Click Header").assertIsDisplayed()
        composeTestRule.onNodeWithText("No Click Content").assertDoesNotExist()
        
        // Click should not expand (no internal click handling)
        composeTestRule.onNodeWithText("No Click Header").performClick()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("No Click Content").assertDoesNotExist()
    }
}
