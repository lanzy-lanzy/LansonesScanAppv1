package dev.ml.lansonesscanapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Manages permanent storage of images for scan history
 * Copies temporary images to app's internal storage to persist across app restarts
 */
class ImageStorageManager(private val context: Context) {
    
    companion object {
        private const val TAG = "ImageStorageManager"
        private const val IMAGES_DIR = "scan_images"
        private const val MAX_IMAGE_SIZE = 1024 // Max dimension for stored images
        private const val JPEG_QUALITY = 85 // JPEG compression quality
    }
    
    /**
     * Copy image from temporary URI to permanent storage
     * Returns the permanent file URI or null if failed
     */
    fun saveImagePermanently(tempUri: Uri, scanId: String): Uri? {
        return try {
            // Create images directory if it doesn't exist
            val imagesDir = File(context.filesDir, IMAGES_DIR)
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }
            
            // Create permanent file
            val fileName = "${scanId}.jpg"
            val permanentFile = File(imagesDir, fileName)
            
            // Load bitmap from temporary URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(tempUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap from URI: $tempUri")
                return null
            }
            
            // Resize if needed to save space
            val resizedBitmap = resizeBitmap(bitmap, MAX_IMAGE_SIZE)
            
            // Save to permanent file
            FileOutputStream(permanentFile).use { out ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, out)
            }
            
            // Clean up bitmaps
            if (resizedBitmap != bitmap) {
                bitmap.recycle()
            }
            resizedBitmap.recycle()
            
            Log.d(TAG, "Image saved permanently: ${permanentFile.absolutePath}")
            
            // Return file URI
            Uri.fromFile(permanentFile)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save image permanently", e)
            null
        }
    }
    
    /**
     * Delete permanently stored image
     */
    fun deleteImage(imageUri: String) {
        try {
            val uri = Uri.parse(imageUri)
            if (uri.scheme == "file") {
                val file = File(uri.path ?: return)
                if (file.exists()) {
                    file.delete()
                    Log.d(TAG, "Image deleted: ${file.absolutePath}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete image", e)
        }
    }
    
    /**
     * Delete all permanently stored images
     */
    fun deleteAllImages() {
        try {
            val imagesDir = File(context.filesDir, IMAGES_DIR)
            if (imagesDir.exists()) {
                imagesDir.listFiles()?.forEach { file ->
                    file.delete()
                }
                Log.d(TAG, "All images deleted")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete all images", e)
        }
    }
    
    /**
     * Get total size of stored images in bytes
     */
    fun getTotalStorageSize(): Long {
        return try {
            val imagesDir = File(context.filesDir, IMAGES_DIR)
            if (imagesDir.exists()) {
                imagesDir.listFiles()?.sumOf { it.length() } ?: 0L
            } else {
                0L
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to calculate storage size", e)
            0L
        }
    }
    
    /**
     * Get number of stored images
     */
    fun getImageCount(): Int {
        return try {
            val imagesDir = File(context.filesDir, IMAGES_DIR)
            if (imagesDir.exists()) {
                imagesDir.listFiles()?.size ?: 0
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to count images", e)
            0
        }
    }
    
    /**
     * Resize bitmap to fit within maxSize while maintaining aspect ratio
     */
    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // If image is already small enough, return as-is
        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }
        
        // Calculate new dimensions maintaining aspect ratio
        val ratio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int
        
        if (width > height) {
            newWidth = maxSize
            newHeight = (maxSize / ratio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }
        
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
