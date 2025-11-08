package dev.ml.lansonesscanapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ml.lansonesscanapp.data.AppDatabase
import dev.ml.lansonesscanapp.model.ScanMode
import dev.ml.lansonesscanapp.model.ScanResult
import dev.ml.lansonesscanapp.network.GeminiClient
import dev.ml.lansonesscanapp.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel for handling image analysis
 */
class AnalysisViewModel(context: Context) : ViewModel() {
    
    private val geminiClient = GeminiClient(context)
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.scanResultDao()
    private val preferencesManager = PreferencesManager(context)
    
    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()
    
    init {
        checkGuideStatus()
    }
    
    fun setMode(mode: ScanMode) {
        _uiState.value = _uiState.value.copy(mode = mode)
    }
    
    fun setImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri, error = null)
    }
    
    fun analyzeImage() {
        val currentState = _uiState.value
        val uri = currentState.imageUri
        val mode = currentState.mode
        
        if (uri == null) {
            _uiState.value = currentState.copy(error = "Please select an image first")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val result = geminiClient.analyzeImage(uri, mode)
            
            result.fold(
                onSuccess = { jsonResponse ->
                    val scanResult = createScanResult(uri, mode, jsonResponse)
                    
                    if (scanResult != null) {
                        // Save to database
                        dao.insertResult(scanResult)
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            result = scanResult,
                            error = null
                        )
                    } else {
                        // Show the actual response for debugging
                        val errorMsg = if (jsonResponse.length > 500) {
                            "Failed to parse: ${jsonResponse.take(500)}..."
                        } else {
                            "Failed to parse: $jsonResponse"
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMsg
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Analysis failed"
                    )
                }
            )
        }
    }
    
    private fun createScanResult(uri: Uri, mode: ScanMode, response: String): ScanResult? {
        // Now we receive formatted text instead of JSON
        if (response.isBlank()) return null
        
        return when (mode) {
            ScanMode.FRUIT -> {
                ScanResult(
                    id = UUID.randomUUID().toString(),
                    mode = mode,
                    imageUri = uri.toString(),
                    title = "Fruit Analysis Result",
                    description = response
                )
            }
            ScanMode.LEAF -> {
                ScanResult(
                    id = UUID.randomUUID().toString(),
                    mode = mode,
                    imageUri = uri.toString(),
                    title = "Leaf Disease Analysis",
                    description = response
                )
            }
        }
    }
    
    fun clearResult() {
        _uiState.value = _uiState.value.copy(result = null, error = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Checks if the user has seen the guide and updates UI state accordingly
     */
    fun checkGuideStatus() {
        val hasSeenGuide = preferencesManager.hasSeenGuide()
        _uiState.value = _uiState.value.copy(
            hasSeenGuide = hasSeenGuide,
            showGuide = !hasSeenGuide
        )
    }
    
    /**
     * Shows the "How it Works" guide
     */
    fun showGuide() {
        _uiState.value = _uiState.value.copy(showGuide = true)
    }
    
    /**
     * Dismisses the guide and marks it as seen
     */
    fun dismissGuide() {
        preferencesManager.setGuideAsSeen()
        _uiState.value = _uiState.value.copy(
            showGuide = false,
            hasSeenGuide = true
        )
    }
}

data class AnalysisUiState(
    val mode: ScanMode = ScanMode.FRUIT,
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val result: ScanResult? = null,
    val error: String? = null,
    val showGuide: Boolean = false,
    val hasSeenGuide: Boolean = false
)
