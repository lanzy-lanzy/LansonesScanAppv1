package dev.ml.lansonesscanapp.model

/**
 * Enum representing the type of scan to perform
 */
enum class ScanMode {
    FRUIT,   // Scan for fruit variety identification
    LEAF,    // Scan for leaf disease detection
    UNKNOWN  // Invalid or unrecognized image
}
