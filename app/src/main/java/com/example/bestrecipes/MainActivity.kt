package com.example.bestrecipes

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bestrecipes.data.responses.Instructions
import com.example.bestrecipes.data.responses.RecipeEntity
import com.example.bestrecipes.favouriteRecipes.FavoriteRecipeListScreen
import com.example.bestrecipes.homeScreen.HomeScreen
import com.example.bestrecipes.recipeDetail.RecipeDetailScreen
import com.example.bestrecipes.recipeList.RecipeListScreen
import com.example.bestrecipes.ui.theme.BestRecipesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BestRecipesTheme {
                AppNavigation()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Home.route
                ) {
                    composable(NavRoutes.Home.route) {
                        HomeScreen(navController = navController)
                    }
                    composable(NavRoutes.RecipeList.route) {
                        val recipeId = it.arguments?.getLong("id") ?: 0L
                        val currentDestination = ""
                        RecipeListScreen(navController = navController, recipeId = recipeId, currentDestination = currentDestination)
                    }
                    composable(
                        NavRoutes.RecipeDetail.route,
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) {
                        val id = it.arguments?.getLong("id") ?: 0L
                        val instructions = emptyList<Instructions>()
                        val currentDestination = ""
                        val recipeEntity = RecipeEntity(
                            id = id,
                            title = "",
                            image = "",
                            isFavorite = false,
                            readyInMinutes = 0,
                            servings = 0,
                            summary = "",
                            instructions = instructions ?: emptyList()
                        )
                        RecipeDetailScreen(recipeId = id, navController = navController, recipeEntity = recipeEntity, currentDestination = currentDestination)
                    }
                    composable(NavRoutes.FavoriteList.route) {
                        val currentDestination = ""
                        FavoriteRecipeListScreen(navController = navController, currentDestination = currentDestination)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentDestination?.destination?.route)
        }
    ) {
        NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
            composable(NavRoutes.Home.route) { HomeScreen(navController) }
            composable(NavRoutes.RecipeList.route) { RecipeListScreen(id.toLong(), navController = navController, currentDestination = currentDestination.toString()) }
            composable(NavRoutes.FavoriteList.route) { FavoriteRecipeListScreen(navController, currentDestination.toString()) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentDestination: String?) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(color = colorResource(id = R.color.background), shape = RoundedCornerShape(50))
            .shadow(8.dp, shape = RoundedCornerShape(50))
    ) {
        NavigationBar(
            containerColor = Color.White,
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_home),
                        contentDescription = "Home",
                        tint = if (currentDestination == NavRoutes.Home.route) colorResource(id = R.color.secondary) else colorResource(
                            id = R.color.background
                        )
                    )
                },
                label = { Text("Home") },
                selected = false,
                onClick = { navController.navigate(NavRoutes.Home.route) }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_recipes),
                        contentDescription = "Recipes",
                        tint = if (currentDestination == NavRoutes.Home.route) colorResource(id = R.color.secondary) else colorResource(
                            id = R.color.background
                        )
                    )
                },
                label = { Text("Recipes") },
                selected = false,
                onClick = { navController.navigate(NavRoutes.RecipeList.route) }
            )
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Profile",
                        tint = if (currentDestination == NavRoutes.Home.route) colorResource(id = R.color.secondary) else colorResource(
                            id = R.color.background
                        )
                    )
                },
                label = { Text("Favourite recipes") },
                selected = false,
                onClick = { navController.navigate(NavRoutes.FavoriteList.route) }
            )
        }
    }
}
