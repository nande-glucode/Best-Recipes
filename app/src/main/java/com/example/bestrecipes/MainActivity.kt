package com.example.bestrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                    startDestination = "recipe_list_screen"
                ) {
                    composable("recipe_list_screen") {
                        RecipeListScreen(navController = navController)
                }
                    composable(
                        "recipe_detail_screen/{dominantColor}/{recipeName}",
                            arguments = listOf(
                                navArgument("dominantColor") {
                                    type = NavType.IntType
                                },
                                navArgument("recipeName") {
                                    type = NavType.StringType
                                }
                            )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val recipeName = remember {
                            it.arguments?.getString("recipeName")
                        }
                    }
                }
            }
        }
    }
}