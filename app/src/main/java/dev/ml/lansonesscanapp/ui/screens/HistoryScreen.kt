package dev.ml.lansonesscanapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.ml.lansonesscanapp.model.ScanResult
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAdaptiveAnimationDuration
import dev.ml.lansonesscanapp.ui.components.AnimatedDialog
import dev.ml.lansonesscanapp.ui.components.ExpandableCard
import dev.ml.lansonesscanapp.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * History screen displaying all past scans
 */
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateBack: () -> Unit
) {
    val scanResults by viewModel.scanResults.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }
    
    // Adaptive animation durations
    val emptyStateDuration = rememberAdaptiveAnimationDuration(AnimationConstants.EMPTY_STATE_FADE_DURATION)
    val listItemEnterDuration = rememberAdaptiveAnimationDuration(AnimationConstants.LIST_ITEM_ENTER_DURATION)
    val listItemExitDuration = rememberAdaptiveAnimationDuration(AnimationConstants.LIST_ITEM_EXIT_DURATION)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Crossfade between empty state and list content
        Crossfade(
            targetState = scanResults.isEmpty(),
            animationSpec = tween(
                durationMillis = emptyStateDuration,
                easing = AnimationConstants.FastOutSlowInEasing
            ),
            label = "Empty state crossfade"
        ) { isEmpty ->
            if (isEmpty) {
                // Empty state with fade-in animation
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No scan history yet",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start scanning to see results here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // History list with item animations
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = scanResults,
                        key = { it.id }
                    ) { result ->
                        AnimatedVisibility(
                            visible = true,
                            enter = slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(
                                    durationMillis = listItemEnterDuration,
                                    easing = AnimationConstants.FastOutSlowInEasing
                                )
                            ) + fadeIn(
                                animationSpec = tween(
                                    durationMillis = listItemEnterDuration,
                                    easing = AnimationConstants.FastOutSlowInEasing
                                )
                            ),
                            exit = slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(
                                    durationMillis = listItemExitDuration,
                                    easing = AnimationConstants.FastOutSlowInEasing
                                )
                            ) + fadeOut(
                                animationSpec = tween(
                                    durationMillis = listItemExitDuration,
                                    easing = AnimationConstants.FastOutSlowInEasing
                                )
                            )
                        ) {
                            HistoryItem(
                                result = result,
                                onDelete = { viewModel.deleteResult(result.id) }
                            )
                        }
                    }
                }
            }
        }
        
        // Floating action button for clear all with animations
        val fabScaleInDuration = rememberAdaptiveAnimationDuration(AnimationConstants.FAB_SCALE_IN_DURATION)
        val fabScaleOutDuration = rememberAdaptiveAnimationDuration(AnimationConstants.FAB_SCALE_OUT_DURATION)
        val fabPressDuration = rememberAdaptiveAnimationDuration(AnimationConstants.PRESS_ANIMATION_DURATION)
        
        AnimatedVisibility(
            visible = scanResults.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            enter = scaleIn(
                initialScale = 0f,
                animationSpec = tween(
                    durationMillis = fabScaleInDuration,
                    delayMillis = AnimationConstants.FAB_ENTRANCE_DELAY,
                    easing = AnimationConstants.FastOutSlowInEasing
                )
            ),
            exit = scaleOut(
                targetScale = 0f,
                animationSpec = tween(
                    durationMillis = fabScaleOutDuration,
                    easing = AnimationConstants.FastOutSlowInEasing
                )
            )
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) AnimationConstants.SCALE_FAB_PRESSED else AnimationConstants.SCALE_NORMAL,
                animationSpec = tween(
                    durationMillis = fabPressDuration,
                    easing = AnimationConstants.FastOutSlowInEasing
                ),
                label = "FAB press scale"
            )
            
            // Use derivedStateOf to reduce recompositions
            val scaleValue by remember { derivedStateOf { scale } }
            
            FloatingActionButton(
                onClick = { showClearDialog = true },
                // Use graphicsLayer for hardware acceleration
                modifier = Modifier.graphicsLayer(
                    scaleX = scaleValue,
                    scaleY = scaleValue
                ),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                interactionSource = interactionSource
            ) {
                Icon(Icons.Default.Delete, "Clear All")
            }
        }
    }
    
    // Clear all confirmation dialog with animation
    AnimatedDialog(
        visible = showClearDialog,
        onDismissRequest = { showClearDialog = false }
    ) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear All History") },
            text = { Text("Are you sure you want to delete all scan history? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HistoryItem(
    result: ScanResult,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    
    ExpandableCard(
        expanded = expanded,
        onExpandChange = { expanded = it },
        modifier = Modifier.fillMaxWidth(),
        header = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Thumbnail
                Card(
                    modifier = Modifier.size(80.dp)
                ) {
                    AsyncImage(
                        model = result.imageUri,
                        contentDescription = "Scan thumbnail",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                
                // Content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = result.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = formatDate(result.timestamp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = result.mode.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Delete button - remains accessible in collapsed state
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        expandedContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        Text(
                            text = result.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.5f)
                        )
                    }
                }
            }
        }
    )
    
    // Delete confirmation dialog with animation
    AnimatedDialog(
        visible = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false }
    ) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Scan") },
            text = { Text("Are you sure you want to delete this scan result?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
