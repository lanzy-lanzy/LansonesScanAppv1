package dev.ml.lansonesscanapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import dev.ml.lansonesscanapp.model.ScanResult

/**
 * Enhanced result card with improved typography hierarchy and visual organization.
 * 
 * Features:
 * - Clear visual hierarchy with larger title typography (headlineMedium - 28sp)
 * - Improved readability with bodyLarge (16sp) for description
 * - Icon badge for result type (32dp)
 * - Dividers for content separation
 * - Proper spacing (24dp padding)
 * - Line height set to 1.6x for better readability
 * 
 * @param result The scan result to display
 * @param modifier Modifier to be applied to the card
 */
@Composable
fun EnhancedResultCard(
    result: ScanResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large, // MD3: 16dp corner radius for cards
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp // MD3: Result card elevation (higher than default)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp) // MD3: Large padding for content cards
        ) {
            // Title with icon badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Result",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(12.dp)) // MD3: Small spacing between icon and text
                
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
            
            // Divider for visual separation
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 1.dp // MD3: Standard divider thickness
            )
            
            Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
            
            // Description with improved line height
            Text(
                text = result.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight.times(1.6f)
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}
