package com.example.bestrecipes.HomeScreen

import androidx.lifecycle.ViewModel
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: SpoonRepository
) : ViewModel() {

   /* fun getRandomFavoriteRecipe(): RecipeEntity? {
        return repository.getRandomFavoriteRecipe()
    } */
}