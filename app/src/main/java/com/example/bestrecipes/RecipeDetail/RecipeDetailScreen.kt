// RecipeDetailScreen.kt
package com.example.bestrecipes.RecipeDetail

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bestrecipes.Data.Responses.Recipe

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    dominantColor: Color,
    recipeId: Long,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    var recipe by remember { mutableStateOf<Recipe?>(null) }
    val context = LocalContext.current

    LaunchedEffect(recipeId) {
        viewModel.getRecipeById(recipeId) { fetchedRecipe ->
            recipe = fetchedRecipe
        }
    }

    recipe?.let {
        Surface(color = dominantColor, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Image(
                    painter = rememberImagePainter(it.image),
                    contentDescription = it.title,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it.title, style = MaterialTheme.typography.headlineMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = it.imageType, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                   // viewModel.addRecipeToFavorites(it)
                }) {
                    Text("Add to Favorites")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                   // viewModel.addRecipeToFavorites(it)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, it.image)
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                }) {
                    Text("Share")
                }
            }
        }
    }
}
