package dev.ml.lansonesscanapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data model representing a scan result
 * Stored in Room database for history
 */
@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey
    val id: String,
    val mode: ScanMode,
    val imageUri: String,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
