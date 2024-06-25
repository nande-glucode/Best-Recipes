package com.example.bestrecipes.HomeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.NavRoutes
import com.example.bestrecipes.R
import com.example.bestrecipes.RecipeList.RecipeEntry
import com.example.bestrecipes.RecipeList.RecipeListViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: RecipeListViewModel = hiltViewModel(),
    viewModel2: HomeScreenViewModel = hiltViewModel()
) {
    val recipeList by viewModel.recipeList.observeAsState(emptyList())
    val favoriteRecipe by viewModel2.isFavoriteRecipe.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(top = 30.dp)
    ) {
        favoriteRecipe?.let { recipe ->
            Recipe(recipe = recipe, navController = navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Based on the type of food you like", style = MaterialTheme.typography.headlineSmall)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipeList) { recipe ->
                RecipeEntry(recipe, navController)
            }
        }
        IconButton(onClick = { navController.navigate(NavRoutes.RecipeList.route) }) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Back")
        }

    }
}

@Composable
fun Recipe(
    recipe: RecipeEntity,
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    viewModel2: RecipeListViewModel = hiltViewModel()
) {
    Card(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .clickable {
                navController.navigate(NavRoutes.RecipeDetail.createRoute(recipe.id))
            }
    ) {
        Column {
            viewModel2.loadRecipePaginated()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.image)
                    .crossfade(true)
                    .build(),
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(350.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(recipe.title, style = MaterialTheme.typography.headlineSmall)
                Text(recipe.title, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun FavoriteRecipe(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val favoriteRecipe by viewModel.isFavoriteRecipe.observeAsState()

    Surface(
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            favoriteRecipe?.let { recipe ->
                Image(
                    painter = rememberAsyncImagePainter(recipe.image),
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
                    text = "No favorite recipe found",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }
            Button(onClick = {
                navController.navigate("recipe_list_screen")
            }) {
                Text("Go to recipe list")
            }
        }
    }
}
