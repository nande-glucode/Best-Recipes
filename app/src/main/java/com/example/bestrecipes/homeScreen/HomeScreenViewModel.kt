package com.example.bestrecipes.homeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestrecipes.data.responses.RecipeEntity
import com.example.bestrecipes.spoonRepository.SpoonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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