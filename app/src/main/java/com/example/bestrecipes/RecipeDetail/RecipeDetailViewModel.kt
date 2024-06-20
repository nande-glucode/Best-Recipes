package com.example.bestrecipes.RecipeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bestrecipes.Data.Responses.Instructions
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: SpoonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipe = MutableLiveData<RecipeEntity>()
    val recipe : LiveData<RecipeEntity> get() = _recipe

    private val _instructionList = MutableLiveData<List<Instructions>>()
    val instructionList: LiveData<List<Instructions>> get() = _instructionList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _loadError = MutableLiveData<String>()
    val loadError: LiveData<String> get() = _loadError

    private val _endReached = MutableLiveData<Boolean>()
    val endReached: LiveData<Boolean> get() = _endReached

    init {
        val recipeId = savedStateHandle.get<Long>("id")
        if (recipeId != null) {
            loadRecipeInstructions(recipeId)
        }
    }

    private fun loadRecipeInstructions(recipeId: Long) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val result = repository.getRecipeInstructions(recipeId)
            when (result) {
                is Resource.Success -> {
                    _instructionList.postValue(result.data!!)
                    _isLoading.postValue(false)
                }
                is Resource.Error -> {
                    _loadError.postValue(result.message!!)
                    _isLoading.postValue(false)
                }
                is Resource.Loading -> {
                }
            }
        }
    }
}
