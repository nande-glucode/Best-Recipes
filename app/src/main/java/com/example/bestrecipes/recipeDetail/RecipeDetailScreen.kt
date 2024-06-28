package com.example.bestrecipes.recipeDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bestrecipes.BottomNavigationBar
import com.example.bestrecipes.data.responses.Instructions
import com.example.bestrecipes.data.responses.RecipeEntity
import com.example.bestrecipes.data.responses.Step


@Composable
fun RecipeDetailScreen(
    navController: NavController,
    recipeEntity: RecipeEntity,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    recipeId: Long,
    currentDestination: String?
) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, currentDestination) }
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                Spacer(modifier = Modifier.height(20.dp))
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(recipeEntity.image)
                        .build(),
                    contentDescription = recipeEntity.title,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                InstructionList(viewModel = viewModel, recipeEntity = recipeEntity)
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun InstructionList(
    recipeEntity: RecipeEntity,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val instructionList by viewModel.instructionList.observeAsState(listOf())
    val isFavourite by viewModel.isFavoriteRecipe.observeAsState(recipeEntity.isFavorite)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val loadError by viewModel.loadError.observeAsState("")

    var showIngredients by remember { mutableStateOf(true) }

    if (isLoading) {
        CircularProgressIndicator()
    } else if (loadError.isNotEmpty()) {
        Text(text = loadError, color = MaterialTheme.colorScheme.error)
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilledTonalButton(onClick = { showIngredients = true }) {
                    Text(text = "Ingredients")
                }
                FilledTonalButton(onClick = { showIngredients = false }) {
                    Text(text = "Equipment")
                }
            }

            if (showIngredients) {
                IngredientsList(instructionList = instructionList)
            } else {
                EquipmentList(instructionList = instructionList)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(instructionList) { instruction ->
                    Text(text = instruction.name, style = MaterialTheme.typography.bodyMedium)
                    instruction.steps.forEachIndexed { index, step ->
                        StepItem(step = step, stepNumber = index + 1)
                    }
                    FilledTonalButton(onClick = {
                        if (isFavourite) {
                            viewModel.removeFromFavorites(recipeEntity)
                        } else {
                            viewModel.toggleFavoriteStatus(recipeEntity)
                        }
                    }) {
                        Text(text = if (isFavourite) "Remove from Favorites" else "Add to Favorites")
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientsList(instructionList: List<Instructions>) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
        instructionList.flatMap { it.steps }.forEach { step ->
            step.ingredients.forEach { ingredient ->
                Column(
                    horizontalAlignment = CenterHorizontally,
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
    }
}

@Composable
fun EquipmentList(instructionList: List<Instructions>) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier.horizontalScroll(scrollState)) {
        instructionList.flatMap { it.steps }.forEach { step ->
            step.equipment.forEach { equipment ->
                Column(
                    horizontalAlignment = CenterHorizontally,
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
    }
}

@Composable
fun StepItem(step: Step, stepNumber: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Step $stepNumber",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = step.step, style = MaterialTheme.typography.bodyMedium)
        }
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
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (steps.size >= rowIndex * 2 + 2) {
                InstructionItem(
                    instruction = steps[rowIndex * 2 + 1],
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
            verticalArrangement = Arrangement.Center,
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
