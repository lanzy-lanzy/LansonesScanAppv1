package dev.ml.lansonesscanapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom navigation items
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        title = "Home",
        icon = Icons.Default.Home
    )
    
    data object Analysis : BottomNavItem(
        route = Screen.Analysis.route,
        title = "Scan",
        icon = Icons.Default.Scanner
    )
    
    data object History : BottomNavItem(
        route = Screen.History.route,
        title = "History",
        icon = Icons.Default.History
    )
}

fun getAllBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Analysis,
        BottomNavItem.History
    )
}
