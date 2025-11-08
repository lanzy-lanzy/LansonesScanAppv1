package dev.ml.lansonesscanapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.ml.lansonesscanapp.ui.screens.AnalysisScreen
import dev.ml.lansonesscanapp.ui.screens.DashboardScreen
import dev.ml.lansonesscanapp.ui.screens.HistoryScreen
import dev.ml.lansonesscanapp.viewmodel.AnalysisViewModel
import dev.ml.lansonesscanapp.viewmodel.HistoryViewModel

/**
 * Navigation routes
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Analysis : Screen("analysis")
    data object History : Screen("history")
}

/**
 * Navigation graph for the app
 */
@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
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
            val viewModel = AnalysisViewModel(context)
            AnalysisScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.History.route) {
            val viewModel = HistoryViewModel(context)
            HistoryScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
