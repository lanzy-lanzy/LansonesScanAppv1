package dev.ml.lansonesscanapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.ml.lansonesscanapp.data.AppDatabase
import dev.ml.lansonesscanapp.model.ScanResult
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing scan history
 */
class HistoryViewModel(context: Context) : ViewModel() {
    
    private val database = AppDatabase.getDatabase(context)
    private val dao = database.scanResultDao()
    private val imageStorageManager = dev.ml.lansonesscanapp.utils.ImageStorageManager(context)
    
    val scanResults: StateFlow<List<ScanResult>> = dao.getAllResults()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun deleteResult(id: String) {
        viewModelScope.launch {
            // Get the result to find the image URI
            val result = dao.getResultById(id)
            
            // Delete from database
            dao.deleteResult(id)
            
            // Delete the image file
            result?.let {
                imageStorageManager.deleteImage(it.imageUri)
            }
        }
    }
    
    fun clearAllHistory() {
        viewModelScope.launch {
            // Delete all images
            imageStorageManager.deleteAllImages()
            
            // Delete all database records
            dao.deleteAllResults()
        }
    }
}
