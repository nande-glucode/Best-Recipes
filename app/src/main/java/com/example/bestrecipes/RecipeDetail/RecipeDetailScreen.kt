package com.example.bestrecipes.RecipeDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.Data.Responses.Step
import com.example.bestrecipes.R
import com.example.bestrecipes.RecipeList.RecipeList
import com.example.bestrecipes.RecipeList.RecipeListViewModel
import com.example.bestrecipes.RecipeList.RecipeRow
import com.example.bestrecipes.RecipeList.RetrySection

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    recipeId: Long
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Recipe",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            InstructionList(recipeId = recipeId, viewModel = viewModel)
        }
    }
}

@Composable
fun InstructionList(
    recipeId: Long,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val instructionList by viewModel.instructionList.observeAsState(listOf())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val loadError by viewModel.loadError.observeAsState("")

            if (isLoading) {
                CircularProgressIndicator()
            } else if (loadError.isNotEmpty()) {
                Text(text = loadError, color = MaterialTheme.colorScheme.error)
            } else {
                Column {
                    instructionList.forEach { instruction ->
                        Text(text = instruction.name, style = MaterialTheme.typography.bodyMedium)
                        instruction.steps.forEach { step ->
                            StepItem(step = step)
                        }
                    }
                }
            }
}

@Composable
fun StepItem(step: Step) {
    Column {
        Text(text = step.step, style = MaterialTheme.typography.bodyMedium)
        Row {
            step.ingredients.forEach { ingredient ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ingredient.image)
                            .build(),
                        contentDescription = ingredient.name,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = ingredient.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Row {
            step.equipment.forEach { equipment ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(equipment.image)
                            .build(),
                        contentDescription = equipment.name,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = equipment.name, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun InstructionRow(
    rowIndex: Int,
    steps: List<Instructions>,
    navController: NavController
) {
    Column {
        Row {
            InstructionItem(
                instruction = steps[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (steps.size >= rowIndex * 2 + 2) {
                InstructionItem(
                    instruction = steps[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InstructionItem(
    instruction: Instructions,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.observeAsState(false)

            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .shadow(5.dp, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .aspectRatio(1f)

            ) {
                Column(
                    verticalArrangement = Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(CenterHorizontally)
                        )
                    }
                    Text(
                        text = instruction.name,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
}