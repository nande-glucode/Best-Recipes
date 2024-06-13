package com.example.bestrecipes.RecipeDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestrecipes.Data.Local.RecipeEntity
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: SpoonRepository
): ViewModel() {

    fun getRecipeById(recipeId: Long) {
        viewModelScope.launch {
            repository.getRecipeDetails(recipeId.toString())
        }
    }

     fun addRecipeToFavorites(recipe: RecipeEntity) {
         viewModelScope.launch {
             repository.insertRecipe(recipe)
         }
     }

}