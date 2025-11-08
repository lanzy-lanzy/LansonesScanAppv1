package dev.ml.lansonesscanapp.data

import androidx.room.TypeConverter
import dev.ml.lansonesscanapp.model.ScanMode

/**
 * Type converters for Room database
 */
class Converters {
    
    @TypeConverter
    fun fromScanMode(mode: ScanMode): String {
        return mode.name
    }
    
    @TypeConverter
    fun toScanMode(value: String): ScanMode {
        return ScanMode.valueOf(value)
    }
}
