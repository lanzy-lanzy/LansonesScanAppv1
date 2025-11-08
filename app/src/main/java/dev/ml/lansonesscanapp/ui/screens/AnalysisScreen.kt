package dev.ml.lansonesscanapp.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.ml.lansonesscanapp.model.ScanMode
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.ui.animations.rememberAdaptiveAnimationDuration
import dev.ml.lansonesscanapp.ui.animations.shakeAnimation
import dev.ml.lansonesscanapp.ui.components.EnhancedModeChip
import dev.ml.lansonesscanapp.ui.components.EnhancedResultCard
import dev.ml.lansonesscanapp.ui.components.GradientBackground
import dev.ml.lansonesscanapp.ui.components.HowItWorksGuide
import dev.ml.lansonesscanapp.ui.components.ImagePlaceholder
import dev.ml.lansonesscanapp.ui.components.ImageSelectionBottomSheet
import dev.ml.lansonesscanapp.viewmodel.AnalysisUiState
import dev.ml.lansonesscanapp.viewmodel.AnalysisViewModel
import java.io.File

/**
 * Analysis screen for scanning fruits and leaves
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Camera permission
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Photo URI for camera capture
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    
    // Bottom sheet visibility state
    var showBottomSheet by remember { mutableStateOf(false) }
    
    // Success indicator state - shows briefly when image is selected
    var showSuccessIndicator by remember { mutableStateOf(false) }
    
    // Track when image URI changes to show success indicator
    LaunchedEffect(uiState.imageUri) {
        if (uiState.imageUri != null) {
            showSuccessIndicator = true
            kotlinx.coroutines.delay(1000) // Show for 1 second
            showSuccessIndicator = false
        }
    }
    
    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            viewModel.setImageUri(photoUri)
        }
    }
    
    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.setImageUri(it) }
    }
    
    // Image Selection Bottom Sheet
    ImageSelectionBottomSheet(
        visible = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        onCameraClick = {
            if (cameraPermissionState.status.isGranted) {
                val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
                cameraLauncher.launch(photoUri!!)
            } else {
                cameraPermissionState.launchPermissionRequest()
            }
        },
        onGalleryClick = {
            galleryLauncher.launch("image/*")
        }
    )
    
    Box(modifier = Modifier.fillMaxSize()) {
        GradientBackground(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // MD3: Medium spacing for screen edges
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            // Mode Selection with animations
            Text(
                text = "Select Scan Mode",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp) // MD3: Small spacing
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // MD3: Small spacing between chips
            ) {
                // Fruit chip with enhanced styling
                EnhancedModeChip(
                    mode = ScanMode.FRUIT,
                    selected = uiState.mode == ScanMode.FRUIT,
                    onClick = { viewModel.setMode(ScanMode.FRUIT) },
                    modifier = Modifier.weight(1f)
                )
                
                // Leaf chip with enhanced styling
                EnhancedModeChip(
                    mode = ScanMode.LEAF,
                    selected = uiState.mode == ScanMode.LEAF,
                    onClick = { viewModel.setMode(ScanMode.LEAF) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Mode description display with animation
            AnimatedContent(
                targetState = uiState.mode,
                transitionSpec = {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                },
                label = "modeDescription"
            ) { mode ->
                Text(
                    text = when (mode) {
                        ScanMode.FRUIT -> "Analyze fruit ripeness and quality"
                        ScanMode.LEAF -> "Detect leaf diseases and health"
                        ScanMode.UNKNOWN -> "General analysis"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp) // MD3: Small spacing
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp)) // MD3: Large spacing between sections
            
            // Image Selection Section
            Text(
                text = "Capture or Select Image",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp) // MD3: Small spacing
            )
            
            // Show placeholder when no image is selected, otherwise show image preview
            if (uiState.imageUri == null) {
                ImagePlaceholder(
                    onClick = { showBottomSheet = true }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp,
                        hoveredElevation = 3.dp
                    )
                ) {
                    Icon(Icons.Default.PhotoLibrary, "Select Image", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Image")
                }
            } else {
                // Image Preview with animations
                // Adaptive animation duration
                val imageDuration = rememberAdaptiveAnimationDuration(AnimationConstants.IMAGE_FADE_DURATION)
                
                // Animate image preview scale
                val imageScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = imageDuration,
                        easing = AnimationConstants.FastOutSlowInEasing
                    ),
                    label = "imageScale"
                )
                
                // Animate image preview alpha
                val imageAlpha by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = imageDuration,
                        easing = AnimationConstants.FastOutSlowInEasing
                    ),
                    label = "imageAlpha"
                )
                
                // Use derivedStateOf to reduce recompositions
                val scaleValue by remember { derivedStateOf { imageScale } }
                val alphaValue by remember { derivedStateOf { imageAlpha } }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            // Use graphicsLayer for hardware acceleration
                            .graphicsLayer(
                                scaleX = scaleValue,
                                scaleY = scaleValue,
                                alpha = alphaValue
                            ),
                        shape = MaterialTheme.shapes.large, // MD3: 16dp corner radius for cards
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp // MD3: Default card elevation
                        )
                    ) {
                        AsyncImage(
                            model = uiState.imageUri,
                            contentDescription = "Selected image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    // Success indicator overlay
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showSuccessIndicator,
                        enter = fadeIn(tween(300)) + scaleIn(
                            initialScale = 0.5f,
                            animationSpec = tween(300)
                        ),
                        exit = fadeOut(tween(300)) + scaleOut(
                            targetScale = 0.5f,
                            animationSpec = tween(300)
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Image selected successfully",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Analyze Button with slide-up, fade-in, and pulsing animation during loading
                val buttonDuration = rememberAdaptiveAnimationDuration(AnimationConstants.BUTTON_SLIDE_DURATION)
                
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { AnimationConstants.SLIDE_OFFSET_SMALL },
                        animationSpec = tween(
                            durationMillis = buttonDuration,
                            easing = AnimationConstants.FastOutSlowInEasing
                        )
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = buttonDuration,
                            easing = AnimationConstants.FastOutSlowInEasing
                        )
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Pulsing scale animation during loading
                        val buttonScale by animateFloatAsState(
                            targetValue = if (uiState.isLoading) AnimationConstants.SCALE_LOADING_MAX else AnimationConstants.SCALE_NORMAL,
                            animationSpec = if (uiState.isLoading) {
                                infiniteRepeatable(
                                    animation = tween(
                                        durationMillis = AnimationConstants.LOADING_PULSE_DURATION,
                                        easing = AnimationConstants.FastOutSlowInEasing
                                    ),
                                    repeatMode = RepeatMode.Reverse
                                )
                            } else {
                                tween(
                                    durationMillis = AnimationConstants.DURATION_SHORT,
                                    easing = AnimationConstants.FastOutSlowInEasing
                                )
                            },
                            label = "buttonScale"
                        )
                        
                        // Use derivedStateOf to reduce recompositions
                        val buttonScaleValue by remember { derivedStateOf { buttonScale } }
                        
                        Button(
                            onClick = { viewModel.analyzeImage() },
                            modifier = Modifier
                                .fillMaxWidth()
                                // Use graphicsLayer for hardware acceleration
                                .graphicsLayer(
                                    scaleX = buttonScaleValue,
                                    scaleY = buttonScaleValue
                                ),
                            enabled = !uiState.isLoading,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp,
                                hoveredElevation = 3.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(if (uiState.isLoading) "Analyzing..." else "Analyze Image")
                        }
                        
                        // Enhanced loading state with descriptive text
                        AnimatedVisibility(
                            visible = uiState.isLoading,
                            enter = fadeIn(tween(300)) + slideInVertically(
                                initialOffsetY = { -20 },
                                animationSpec = tween(300)
                            ),
                            exit = fadeOut(tween(300))
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(48.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 4.dp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Analyzing your image...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "This may take a few moments",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            // Error Message with slide-in from top, shake, and fade-out animations
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(
                        durationMillis = AnimationConstants.ERROR_SLIDE_DURATION,
                        easing = AnimationConstants.EmphasizedDecelerateEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.ERROR_SLIDE_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ),
                exit = fadeOut(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.ERROR_DISMISS_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            ) {
                uiState.error?.let { error ->
                    Column {
                        Spacer(modifier = Modifier.height(16.dp)) // MD3: Medium spacing
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp)
                                .shakeAnimation(trigger = uiState.error != null),
                            shape = MaterialTheme.shapes.large, // MD3: 16dp corner radius for cards
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp // MD3: Default card elevation
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp), // MD3: Medium padding
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Error icon
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(top = 2.dp) // Align with text baseline
                                )
                                
                                // Error message
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Error",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Text(
                                        text = error,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Result Display with animations
            AnimatedVisibility(
                visible = uiState.result != null,
                enter = slideInVertically(
                    initialOffsetY = { AnimationConstants.SLIDE_OFFSET_SMALL },
                    animationSpec = tween(
                        durationMillis = AnimationConstants.RESULT_CARD_DURATION,
                        easing = AnimationConstants.EmphasizedDecelerateEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.RESULT_CARD_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { AnimationConstants.SLIDE_OFFSET_SMALL },
                    animationSpec = tween(
                        durationMillis = AnimationConstants.RESULT_CARD_DURATION,
                        easing = AnimationConstants.EmphasizedAccelerateEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.RESULT_CARD_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            ) {
                uiState.result?.let { result ->
                    Column {
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        EnhancedResultCard(
                            result = result,
                            modifier = Modifier.heightIn(min = 200.dp, max = 600.dp)
                        )
                    }
                }
            }
        }
        }
        
        // Help button in top-right corner with elevated surface for better visibility
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp), // MD3: Medium padding from edges
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        ) {
            IconButton(
                onClick = { viewModel.showGuide() },
                modifier = Modifier
                    .size(48.dp) // MD3: Minimum touch target size
                    .semantics {
                        contentDescription = "Show how it works guide"
                    }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
                    contentDescription = "Help icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp) // MD3: Standard icon size
                )
            }
        }
        
        // How it Works Guide
        HowItWorksGuide(
            visible = uiState.showGuide,
            onDismiss = { viewModel.dismissGuide() }
        )
    }
}

/**
 * Animated mode selection chip with scale and color transitions.
 * 
 * Features:
 * - Color transition animation (200ms) when selected state changes
 * - Subtle scale animation (1.05x) for selected chip
 * - Handles rapid mode changes with animation cancellation
 * 
 * @param selected Whether this chip is currently selected
 * @param onClick Callback invoked when the chip is clicked
 * @param label The text label for the chip
 * @param modifier Modifier to be applied to the chip
 */
@Composable
private fun AnimatedModeChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    // Animate scale for selected chip (1.05x when selected)
    val scale by animateFloatAsState(
        targetValue = if (selected) AnimationConstants.SCALE_CHIP_SELECTED else AnimationConstants.SCALE_NORMAL,
        animationSpec = tween(
            durationMillis = AnimationConstants.MODE_SELECTION_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipScale"
    )
    
    // Use derivedStateOf to reduce recompositions
    val scaleValue by remember { derivedStateOf { scale } }
    
    // Animate colors for smooth transitions
    val containerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipContainerColor"
    )
    
    val labelColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipLabelColor"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(
            durationMillis = AnimationConstants.CHIP_COLOR_DURATION,
            easing = AnimationConstants.FastOutSlowInEasing
        ),
        label = "chipBorderColor"
    )
    
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { 
            Text(
                text = label,
                color = labelColor
            )
        },
        enabled = true,
        // Use graphicsLayer for hardware-accelerated scale animation
        modifier = modifier.graphicsLayer(
            scaleX = scaleValue,
            scaleY = scaleValue
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = containerColor,
            selectedContainerColor = containerColor
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = borderColor,
            selectedBorderColor = borderColor,
            borderWidth = 1.dp
        )
    )
}
