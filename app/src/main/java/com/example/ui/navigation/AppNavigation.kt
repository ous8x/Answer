package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToSetup = { navController.navigate("setup") },
                onNavigateToSheet = { sheetId -> navController.navigate("sheet/$sheetId") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        
        composable("setup") {
            SetupScreen(
                onSheetCreated = { sheetId ->
                    navController.popBackStack()
                    navController.navigate("sheet/$sheetId")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            "sheet/{sheetId}",
            arguments = listOf(navArgument("sheetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val sheetId = backStackEntry.arguments?.getInt("sheetId") ?: 0
            AnswerSheetScreen(
                sheetId = sheetId,
                onNavigateBack = { navController.popBackStack() },
                onFinish = { sId ->
                    navController.popBackStack()
                    navController.navigate("results/$sId")
                }
            )
        }
        
        composable(
            "results/{sheetId}",
            arguments = listOf(navArgument("sheetId") { type = NavType.IntType })
        ) { backStackEntry ->
            val sheetId = backStackEntry.arguments?.getInt("sheetId") ?: 0
            ResultsScreen(
                sheetId = sheetId,
                onNavigateHome = {
                    navController.popBackStack("home", false)
                }
            )
        }
        
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
