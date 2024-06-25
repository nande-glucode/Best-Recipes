package com.example.bestrecipes.RecipeList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.NavRoutes
import com.example.bestrecipes.R
import com.example.bestrecipes.RecipeDetail.RecipeDetailViewModel

@Composable
fun RecipeListScreen(
    recipeId: Long,
    navController: NavController,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val backgroundColor = colorResource(id = R.color.background)

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = backgroundColor)
                .padding(16.dp)
        ) {
            TopSection()
            SearchBar(modifier = Modifier
                .background(color = backgroundColor)){
                viewModel.searchRecipeList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            RecipeList(navController = navController, viewModel = viewModel, recipeId = recipeId)

        }
    }

}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
            .background(color = colorResource(id = R.color.background))
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp),

        )

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier.background(color = colorResource(id = R.color.background))) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused && text.isEmpty()
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable fun RecipeList(
    recipeId: Long,
    navController: NavController,
    viewModel: RecipeListViewModel = hiltViewModel()
) {
    val recipeList by viewModel.recipeList. observeAsState( listOf( ) )
    val endReached by viewModel.endReached. observeAsState( false)
    val loadError by viewModel.loadError. observeAsState( " " )
    val isLoading by viewModel.isLoading. observeAsState( false)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = colorResource(id = R.color.background))
    )  {
        val itemCount = if(recipeList.size % 2 == 0) {
            recipeList.size / 2
        } else {
            recipeList.size / 2 + 1
        }
        items(itemCount) {
            if(it >= itemCount - 1 && !endReached && !isLoading) {
                viewModel.loadRecipePaginated()
                viewModel.loadRecipeInformation(recipeId)
            }
            RecipeRow(rowIndex = it, entries = recipeList, navController = navController)
        }
    }

    Box(
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background))
    ) {
        if(isLoading) {
            CircularProgressIndicator( color = MaterialTheme.colorScheme. primary)
        }
        if(loadError.isNotEmpty( ) )  {
            RetrySection(error = loadError) {
                viewModel.loadRecipePaginated( )
            }
        }
    }
}

@Composable
fun RecipeEntry(
    entry: RecipeEntity,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: RecipeListViewModel = hiltViewModel(),
    viewModel2: RecipeDetailViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(true) }
    var backgroundColor = colorResource(id = R.color.primary)
    var secondaryColor = colorResource(id = R.color.secondary)

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(color = backgroundColor)
            .clickable {
                navController.navigate(NavRoutes.RecipeDetail.createRoute(entry.id))
            }
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.primary))

        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.image)
                    .crossfade(true)
                    .listener(
                        onSuccess = { _, result ->
                            viewModel.calcDominantColor(result.drawable) { color ->
                            }
                            isLoading = false
                        },
                        onError = { _, _ ->
                            isLoading = false
                        }
                    )
                    .build(),
                contentDescription = entry.title,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = entry.title,
                        color = colorResource(id = R.color.textPrimary),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Ready in minutes: ${entry.readyInMinutes} • Servings: ${entry.servings}",
                        color = colorResource(id = R.color.textSecondary),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeRow(
    rowIndex: Int,
    entries: List<RecipeEntity>,
    navController: NavController
) {
    Column {
        Row {
            RecipeEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                RecipeEntry(
                    entry = entries[rowIndex * 2 + 1],
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
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}