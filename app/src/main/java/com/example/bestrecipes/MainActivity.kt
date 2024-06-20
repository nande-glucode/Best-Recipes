package com.example.bestrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestrecipes.HomeScreen.HomeScreen
import com.example.bestrecipes.RecipeDetail.RecipeDetailScreen
import com.example.bestrecipes.RecipeList.RecipeListScreen
import com.example.bestrecipes.ui.theme.BestRecipesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BestRecipesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Home.route
                ) {
                    composable(NavRoutes.Home.route) {
                        HomeScreen(navController = navController)
                    }
                    composable(NavRoutes.RecipeList.route) {
                        RecipeListScreen(navController = navController)
                    }
                    composable(
                        NavRoutes.RecipeDetail.route,
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val dominantColor = Color(it.arguments?.getInt("dominantColor") ?: 0)
                        val id = it.arguments?.getLong("id") ?: 0L
                        RecipeDetailScreen(recipeId = id)
                    }
                }
            }
        }
    }
}