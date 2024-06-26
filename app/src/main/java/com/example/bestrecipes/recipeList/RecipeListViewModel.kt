package com.example.bestrecipes.recipeList

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.bestrecipes.data.responses.RecipeEntity
import com.example.bestrecipes.data.responses.RecipeList
import com.example.bestrecipes.spoonRepository.SpoonRepository
import com.example.bestrecipes.utils.Constants.PAGE_SIZE
import com.example.bestrecipes.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: SpoonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var curPage = 0
    private var currentQuery: String? = null

    private val _recipeList = MutableLiveData<List<RecipeEntity>>()
    val recipeList: LiveData<List<RecipeEntity>> get() = _recipeList

    private val _recipeList2 = MutableLiveData<List<RecipeList>>()
    val recipeList2: LiveData<List<RecipeList>> get() = _recipeList2

    private val _loadError = MutableLiveData<String>()
    val loadError: LiveData<String> get() = _loadError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _endReached = MutableLiveData<Boolean>()
    val endReached: LiveData<Boolean> get() = _endReached

    private var cachedRecipeList = mutableListOf<RecipeEntity>()
    private var isSearchStarting = true

    private val _isSearching = MutableLiveData<Boolean>()
    val isSearching: LiveData<Boolean> get() = _isSearching

    private val _favoriteRecipe = MutableLiveData<RecipeEntity?>()
    val favoriteRecipe: LiveData<RecipeEntity?> get() = _favoriteRecipe

    private val _isFavoriteRecipe = MutableLiveData<Boolean>()
    val isFavoriteRecipe: LiveData<Boolean> get() = _isFavoriteRecipe
    private val recipeId: Long = savedStateHandle["id"] ?: 0



    init {
        loadRecipePaginated()
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
                    _recipeList.postValue(result.data.results)
                    val recipeEntries = result.data.results!!.mapIndexed { index, entry ->
                        val name = entry.title.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                        }
                        val instructions = entry.instructions ?: emptyList()
                        val servings = entry.servings
                        val url = if (entry.id.toInt() != 0) {
                            "https://spoonacular.com/recipeImages/${entry.id}-312x231.jpg"
                        } else {
                            "https://img.spoonacular.com/recipes/716429-312x231.jpg"
                        }
                        val id = entry.id
                        val summary = entry.summary ?: ""
                        val readyInMinutes = entry.readyInMinutes
                        val isFavorite = entry.isFavorite
                        RecipeEntity(id, name, url, readyInMinutes, servings, summary, isFavorite, instructions)
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

                is Resource.Loading -> TODO()
            }
        }
    }

    fun loadRecipeInformation(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val result = repository.getRecipeInformation(id)
            when(result) {
                is Resource.Success -> {
                    _recipeList2.postValue(listOf(result.data!!))
                    _isLoading.postValue(false)
                }
                is Resource.Error -> {
                    _loadError.postValue(result.message!!)
                    _isLoading.postValue(false)
                }
                is Resource.Loading -> TODO()
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