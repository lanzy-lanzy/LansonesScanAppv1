package dev.ml.lansonesscanapp.cache

import android.content.Context
import android.graphics.Bitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.ml.lansonesscanapp.model.ScanMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

// Create DataStore as a top-level singleton to avoid multiple instances
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "analysis_cache")

/**
 * Cache manager for analysis results
 * Stores results based on image hash to provide instant results for duplicate images
 */
class AnalysisCache(private val context: Context) {
    
    companion object {
        private const val CACHE_NAME = "analysis_cache"
        private const val MAX_CACHE_SIZE = 50 // Maximum number of cached results
        const val CACHE_EXPIRY_DAYS = 7 // Cache expires after 7 days
    }
    
    /**
     * Generate a unique hash for an image
     * Uses MD5 hash of downsampled bitmap for fast comparison and memory efficiency
     * Downsamples to max 100x100 to avoid OutOfMemoryError on large images
     */
    fun generateImageHash(bitmap: Bitmap): String {
        try {
            // Downsample bitmap to max 100x100 for hashing
            // This is sufficient for uniqueness while being memory-efficient
            val maxHashSize = 100
            val scale = maxOf(
                bitmap.width.toFloat() / maxHashSize,
                bitmap.height.toFloat() / maxHashSize,
                1f
            )
            
            val hashWidth = (bitmap.width / scale).toInt()
            val hashHeight = (bitmap.height / scale).toInt()
            
            // Create small bitmap for hashing
            val smallBitmap = Bitmap.createScaledBitmap(bitmap, hashWidth, hashHeight, false)
            
            // Get pixels from small bitmap
            val pixels = IntArray(hashWidth * hashHeight)
            smallBitmap.getPixels(pixels, 0, hashWidth, 0, 0, hashWidth, hashHeight)
            
            // Recycle small bitmap to free memory
            if (smallBitmap != bitmap) {
                smallBitmap.recycle()
            }
            
            // Generate MD5 hash
            val md = MessageDigest.getInstance("MD5")
            
            // Convert pixels to bytes more efficiently
            val buffer = ByteArray(pixels.size * 3)
            var bufferIndex = 0
            for (pixel in pixels) {
                buffer[bufferIndex++] = (pixel shr 16 and 0xFF).toByte()
                buffer[bufferIndex++] = (pixel shr 8 and 0xFF).toByte()
                buffer[bufferIndex++] = (pixel and 0xFF).toByte()
            }
            
            val digest = md.digest(buffer)
            return digest.joinToString("") { "%02x".format(it) }
        } catch (e: OutOfMemoryError) {
            // Fallback: use bitmap dimensions and timestamp as hash
            android.util.Log.e("AnalysisCache", "OutOfMemoryError generating hash, using fallback", e)
            return "${bitmap.width}x${bitmap.height}_${System.currentTimeMillis()}"
        } catch (e: Exception) {
            // Fallback for any other error
            android.util.Log.e("AnalysisCache", "Error generating hash, using fallback", e)
            return "${bitmap.width}x${bitmap.height}_${System.currentTimeMillis()}"
        }
    }
    
    /**
     * Get cached result for an image hash and mode
     */
    suspend fun getCachedResult(imageHash: String, mode: ScanMode): CachedAnalysis? {
        val key = stringPreferencesKey("${mode.name}_$imageHash")
        
        return context.dataStore.data.map { preferences ->
            preferences[key]?.let { json ->
                try {
                    CachedAnalysis.fromJson(json)?.takeIf { 
                        !it.isExpired() 
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }.first()
    }
    
    /**
     * Save analysis result to cache
     */
    suspend fun cacheResult(imageHash: String, mode: ScanMode, result: String) {
        val key = stringPreferencesKey("${mode.name}_$imageHash")
        val cachedAnalysis = CachedAnalysis(
            result = result,
            timestamp = System.currentTimeMillis()
        )
        
        context.dataStore.edit { preferences ->
            preferences[key] = cachedAnalysis.toJson()
        }
        
        // Clean up old cache entries if needed
        cleanupOldCache()
    }
    
    /**
     * Remove expired cache entries
     */
    private suspend fun cleanupOldCache() {
        context.dataStore.edit { preferences ->
            val keysToRemove = mutableListOf<Preferences.Key<String>>()
            
            preferences.asMap().forEach { (key, value) ->
                if (value is String) {
                    try {
                        val cached = CachedAnalysis.fromJson(value)
                        if (cached?.isExpired() == true) {
                            @Suppress("UNCHECKED_CAST")
                            keysToRemove.add(key as Preferences.Key<String>)
                        }
                    } catch (e: Exception) {
                        // Invalid entry, remove it
                        @Suppress("UNCHECKED_CAST")
                        keysToRemove.add(key as Preferences.Key<String>)
                    }
                }
            }
            
            keysToRemove.forEach { key ->
                preferences.remove(key)
            }
        }
    }
    
    /**
     * Clear all cached results
     */
    suspend fun clearCache() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    /**
     * Get cache statistics
     */
    suspend fun getCacheStats(): CacheStats {
        return context.dataStore.data.map { preferences ->
            val totalEntries = preferences.asMap().size
            var validEntries = 0
            var expiredEntries = 0
            
            preferences.asMap().values.forEach { value ->
                if (value is String) {
                    try {
                        val cached = CachedAnalysis.fromJson(value)
                        if (cached != null) {
                            if (cached.isExpired()) {
                                expiredEntries++
                            } else {
                                validEntries++
                            }
                        }
                    } catch (e: Exception) {
                        // Ignore invalid entries
                    }
                }
            }
            
            CacheStats(
                totalEntries = totalEntries,
                validEntries = validEntries,
                expiredEntries = expiredEntries
            )
        }.first()
    }
}

/**
 * Cached analysis result with timestamp
 */
data class CachedAnalysis(
    val result: String,
    val timestamp: Long
) {
    fun isExpired(): Boolean {
        val expiryTime = timestamp + (AnalysisCache.CACHE_EXPIRY_DAYS * 24 * 60 * 60 * 1000L)
        return System.currentTimeMillis() > expiryTime
    }
    
    fun toJson(): String {
        return "$timestamp|$result"
    }
    
    companion object {
        fun fromJson(json: String): CachedAnalysis? {
            return try {
                val parts = json.split("|", limit = 2)
                if (parts.size == 2) {
                    CachedAnalysis(
                        timestamp = parts[0].toLong(),
                        result = parts[1]
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}

/**
 * Cache statistics
 */
data class CacheStats(
    val totalEntries: Int,
    val validEntries: Int,
    val expiredEntries: Int
)
