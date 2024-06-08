package com.example.bestrecipes.RecipeList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.bestrecipes.Data.Models.RecipeListEntry
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Constants.API_KEY
import com.example.bestrecipes.Utils.Constants.PAGE_SIZE
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: SpoonRepository
) : ViewModel() {

    private var curPage = 0

    var recipeList = mutableStateOf<List<RecipeListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    init {
        loadRecipePaginated()
    }

   /* fun searchRecipeList(query: String) {
        val listToSearch = if(isSearchStarting) {
            recipeList.value
        } else {
            cachedRecipeList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                _recipeList.value = cachedRecipeList
                _isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.recipeName.contains(query.trim(), ignoreCase = true) ||
                        it.recipeName == query.trim()
            }
            if(isSearchStarting) {
                cachedRecipeList = recipeList.value
                isSearchStarting = false
            }
            _recipeList.value = results
            _isSearching.value = true
        }
    } */

    fun loadRecipePaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getRecipeList(PAGE_SIZE, curPage * PAGE_SIZE, API_KEY)
            when(result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.number
                    val recipeEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.image.endsWith("/")) {
                            entry.image.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.image.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://img.spoonacular.com/recipes/${number}-312x231.jpg"
                        RecipeListEntry(entry.title.capitalize(Locale.ROOT), url,
                            number.toInt().toString(), "")
                    }
                    curPage++

                    loadError.value = ""
                    isLoading.value = false
                    recipeList.value += recipeEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}