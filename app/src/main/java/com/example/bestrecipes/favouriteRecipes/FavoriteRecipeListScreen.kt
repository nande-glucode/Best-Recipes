package com.example.bestrecipes.favouriteRecipes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bestrecipes.BottomNavigationBar

@Composable
fun FavoriteRecipeListScreen(navController: NavController, currentDestination: String?) {
    Scaffold(
    bottomBar = { BottomNavigationBar(navController, currentDestination = currentDestination) }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}