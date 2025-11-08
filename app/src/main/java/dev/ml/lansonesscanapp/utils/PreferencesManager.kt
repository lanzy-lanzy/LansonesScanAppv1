package dev.ml.lansonesscanapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages user preferences for the Lansones Scan App
 * Handles guide dismissal state and other user preferences
 */
class PreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    
    /**
     * Checks if the user has seen the "How it Works" guide
     * @return true if the guide has been seen, false otherwise
     */
    fun hasSeenGuide(): Boolean {
        return prefs.getBoolean(KEY_HAS_SEEN_GUIDE, false)
    }
    
    /**
     * Marks the "How it Works" guide as seen
     * This prevents the guide from showing automatically on subsequent launches
     */
    fun setGuideAsSeen() {
        prefs.edit().putBoolean(KEY_HAS_SEEN_GUIDE, true).apply()
    }
    
    /**
     * Resets the guide seen state
     * Useful for testing or allowing users to see the guide again
     */
    fun resetGuide() {
        prefs.edit().putBoolean(KEY_HAS_SEEN_GUIDE, false).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "lansones_scan_prefs"
        private const val KEY_HAS_SEEN_GUIDE = "has_seen_how_it_works_guide"
    }
}
