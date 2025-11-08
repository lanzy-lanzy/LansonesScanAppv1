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
    
    val scanResults: StateFlow<List<ScanResult>> = dao.getAllResults()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun deleteResult(id: String) {
        viewModelScope.launch {
            dao.deleteResult(id)
        }
    }
    
    fun clearAllHistory() {
        viewModelScope.launch {
            dao.deleteAllResults()
        }
    }
}
