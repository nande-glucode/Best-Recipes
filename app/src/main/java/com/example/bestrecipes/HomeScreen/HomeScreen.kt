package com.example.bestrecipes.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bestrecipes.RecipeList.RecipeListViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val favouriteRecipe = viewModel.favoriteRecipe.observeAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            favouriteRecipe.value?.let { recipe ->
                Image(
                    painter = rememberImagePainter(recipe.image),
                    contentDescription = recipe.title,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            } ?: run {
                Text(
                    text = "No favourite recipe found",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }
        }
    }
}