package com.example.bestrecipes.RecipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestrecipes.Data.Responses.Recipe
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailViewModel @Inject constructor(
    private val repository: SpoonRepository
): ViewModel() {

    fun getRecipeById(recipeId: Long, onResult: (Recipe?) -> Unit)  {
        viewModelScope.launch {
            val result = repository.getRecipeDetails(recipeId.toString())
            onResult((result as? Resource.Success)?.data)
        }
    }

   /* fun addRecipeToFavorites(recipe: Recipe) {
        viewModelScope.launch {
            repository.addRecipeToFavorites(recipe)
        }
    } */

}