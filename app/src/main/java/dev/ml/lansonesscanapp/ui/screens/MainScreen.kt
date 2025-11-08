package dev.ml.lansonesscanapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.ml.lansonesscanapp.navigation.BottomNavItem
import dev.ml.lansonesscanapp.navigation.Screen
import dev.ml.lansonesscanapp.navigation.getAllBottomNavItems
import dev.ml.lansonesscanapp.ui.animations.AnimationConstants
import dev.ml.lansonesscanapp.viewmodel.AnalysisViewModel
import dev.ml.lansonesscanapp.viewmodel.HistoryViewModel

/**
 * Main screen with bottom navigation
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Track if bottom bar should be visible
    val bottomBarVisible = remember(currentDestination) {
        getAllBottomNavItems().any { it.route == currentDestination?.route }
    }
    
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 8.dp
                ) {
                    getAllBottomNavItems().forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { 
                            it.route == item.route 
                        } == true
                        
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = { 
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelMedium
                                ) 
                            },
                            selected = selected,
                            onClick = {
                                if (currentDestination?.route != item.route) {
                                    navController.navigate(item.route) {
                                        // Pop up to the start destination
                                        popUpTo(Screen.Dashboard.route) {
                                            saveState = true
                                        }
                                        // Avoid multiple copies of the same destination
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                ) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(
                        durationMillis = AnimationConstants.NAVIGATION_DURATION,
                        easing = AnimationConstants.FastOutSlowInEasing
                    )
                )
            }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToAnalysis = {
                        navController.navigate(Screen.Analysis.route)
                    },
                    onNavigateToHistory = {
                        navController.navigate(Screen.History.route)
                    }
                )
            }
            
            composable(Screen.Analysis.route) {
                val viewModel = remember { AnalysisViewModel(context) }
                AnalysisScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(Screen.History.route) {
                val viewModel = remember { HistoryViewModel(context) }
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
