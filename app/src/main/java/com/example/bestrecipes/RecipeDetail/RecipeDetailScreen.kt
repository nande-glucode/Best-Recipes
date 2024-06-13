package com.example.bestrecipes.RecipeDetail

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bestrecipes.Data.Local.RecipeEntity

@Composable
fun RecipeDetailScreen(
    navController: NavController,
    dominantColor: Color,
    recipeId: Long,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    var recipe by remember { mutableStateOf<RecipeEntity?>(null) }
    val context = LocalContext.current

    LaunchedEffect(recipeId) {
        viewModel.getRecipeById(recipeId)
    }


    recipe?.let { detailedRecipe ->
        Surface(color = dominantColor, modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(detailedRecipe.image),
                    contentDescription = detailedRecipe.title,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = detailedRecipe.title, style = MaterialTheme.typography.headlineMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = detailedRecipe.imageType, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                     viewModel.addRecipeToFavorites(detailedRecipe)
                }) {
                    Text("Add to Favorites")
                }

                Button(onClick = {
                    viewModel.addRecipeToFavorites(detailedRecipe)
                }) {
                    Text("Add to Favorites")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // viewModel.addRecipeToFavorites(it)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, detailedRecipe.image)
                    }
                    context.startActivity(Intent.createChooser(intent, null))
                }) {
                    Text("Share")
                }
            }
        }
    } ?: CircularProgressIndicator()
}
