package dev.ml.lansonesscanapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ml.lansonesscanapp.model.ScanResult
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for ScanResult
 */
@Dao
interface ScanResultDao {
    
    @Query("SELECT * FROM scan_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<ScanResult>>
    
    @Query("SELECT * FROM scan_results WHERE id = :id")
    suspend fun getResultById(id: String): ScanResult?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ScanResult)
    
    @Query("DELETE FROM scan_results WHERE id = :id")
    suspend fun deleteResult(id: String)
    
    @Query("DELETE FROM scan_results")
    suspend fun deleteAllResults()
}
