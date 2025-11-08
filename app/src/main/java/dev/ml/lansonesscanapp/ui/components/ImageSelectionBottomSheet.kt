package dev.ml.lansonesscanapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants

/**
 * A modal bottom sheet for selecting image source (camera or gallery).
 * 
 * Features:
 * - Slides up from bottom with 300ms duration
 * - Slides down with 250ms duration on dismiss
 * - Scrim overlay with 40% opacity (handled by Material 3)
 * - Large touch targets (48dp minimum) for accessibility
 * - Icon + text layout for clarity
 * 
 * @param visible Whether the bottom sheet is currently visible
 * @param onDismiss Callback invoked when the bottom sheet is dismissed
 * @param onCameraClick Callback invoked when the camera option is selected
 * @param onGalleryClick Callback invoked when the gallery option is selected
 * @param sheetState The state of the bottom sheet for controlling its behavior
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSelectionBottomSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = AnimationConstants.SCRIM_OPACITY)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp), // MD3: Medium horizontal, large vertical padding
                verticalArrangement = Arrangement.spacedBy(8.dp) // MD3: Small spacing between options
            ) {
                // Title
                Text(
                    text = "Select Image Source",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp) // MD3: Small spacing
                )
                
                // Camera option
                ImageSourceOption(
                    icon = Icons.Default.CameraAlt,
                    label = "Take Photo",
                    onClick = {
                        onCameraClick()
                        onDismiss()
                    }
                )
                
                // Gallery option
                ImageSourceOption(
                    icon = Icons.Default.PhotoLibrary,
                    label = "Choose from Gallery",
                    onClick = {
                        onGalleryClick()
                        onDismiss()
                    }
                )
                
                // Bottom spacing for safe area
                Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
            }
        }
    }
}

/**
 * A single option in the image selection bottom sheet.
 * 
 * @param icon The icon to display
 * @param label The text label for the option
 * @param onClick Callback invoked when the option is clicked
 */
@Composable
private fun ImageSourceOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // MD3: Large touch target for accessibility (exceeds 48dp minimum)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp), // MD3: Large icon size
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp)) // MD3: Medium spacing between icon and text
            
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
