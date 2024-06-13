package com.example.bestrecipes.RecipeList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.bestrecipes.Data.Local.RecipeEntity
import com.example.bestrecipes.Data.Models.RecipeListEntry
import com.example.bestrecipes.Data.Responses.Recipe
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Constants.PAGE_SIZE
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: SpoonRepository
) : ViewModel() {

    private var curPage = 0
    private var currentQuery: String? = null

    private val _recipeList = MutableLiveData<List<RecipeListEntry>>()
    val recipeList: LiveData<List<RecipeListEntry>> get() = _recipeList

    private val _loadError = MutableLiveData<String>()
    val loadError: LiveData<String> get() = _loadError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _endReached = MutableLiveData<Boolean>()
    val endReached: LiveData<Boolean> get() = _endReached

    private var cachedRecipeList = mutableListOf<RecipeListEntry>()
    private var isSearchStarting = true

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> get() = _isSearching

    private val _favoriteRecipe = MutableLiveData<RecipeEntity?>()
    val favoriteRecipe: LiveData<RecipeEntity?> get() = _favoriteRecipe

    init {
        loadRecipePaginated()
    }

      fun addRecipeToFavorites(recipe: RecipeEntity) {
          viewModelScope.launch {
              repository.insertRecipe(recipe)
          }
      }

      fun getRandomFavouriteRecipe() {
          viewModelScope.launch {
              _favoriteRecipe.postValue(repository.getRandomFavoriteRecipe())
          }
      }

      fun removeRecipeFromFavorites(recipeId: Long) {
          viewModelScope.launch {
              repository.deleteRecipe(recipeId)
          }
      }

    fun searchRecipeList(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                _recipeList.postValue(cachedRecipeList)
                _isSearching.postValue(false)
                isSearchStarting = true
            } else {
                currentQuery = query
                cachedRecipeList.clear()
                curPage = 0
                loadRecipePaginated()
            }
        }
    }

    fun loadRecipePaginated() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = repository.getRecipeList(PAGE_SIZE, curPage * PAGE_SIZE, currentQuery)
            when(result) {
                is Resource.Success -> {
                    _endReached.postValue(curPage * PAGE_SIZE >= result.data!!.totalResults)
                    val recipeEntries = result.data.results.mapIndexed { index, entry ->
                        val image = entry.image
                        val number = if(image.endsWith("-312x231.jpg")) {
                            image.dropLast(11).takeLastWhile { it.isDigit() }
                        } else {
                            image.takeLastWhile { it.isDigit() }
                        }

                        val recipeNumber = number.toIntOrNull() ?: 0
                        val url = if (entry.id.toInt() != 0) {
                            "https://spoonacular.com/recipeImages/${entry.id}-312x231.jpg"
                        } else {
                            "https://img.spoonacular.com/recipes/716429-312x231.jpg"
                        }
                        RecipeListEntry(entry.title.capitalize(Locale.ROOT), url, recipeNumber)
                    }
                    curPage++

                    _isLoading.postValue(false)
                    cachedRecipeList.addAll(recipeEntries)
                    _recipeList.postValue(cachedRecipeList)
                }
                is Resource.Error -> {
                    _loadError.postValue(result.message!!)
                    _isLoading.postValue(false)
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