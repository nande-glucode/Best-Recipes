package com.example.bestrecipes.HomeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: SpoonRepository
) : ViewModel() {

    private val _isFavoriteRecipe = MutableLiveData<RecipeEntity?>()
    val isFavoriteRecipe: LiveData<RecipeEntity?> get() = _isFavoriteRecipe

    init {
        loadFavoriteRecipe()
    }

    fun loadFavoriteRecipe() {
        viewModelScope.launch {
            val favoriteRecipe = repository.getRandomFavoriteRecipe()
            _isFavoriteRecipe.postValue(favoriteRecipe)
        }
    }


}