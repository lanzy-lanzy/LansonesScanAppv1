package dev.ml.lansonesscanapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.ml.lansonesscanapp.model.ScanResult

/**
 * Room database for the LansonesScanApp
 */
@Database(
    entities = [ScanResult::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun scanResultDao(): ScanResultDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lansones_scan_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
