package com.example.androiddevelopment2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.exception.InvalidApiKeyException
import com.example.androiddevelopment2.domain.exception.RecipeNotFoundException
import com.example.androiddevelopment2.domain.usecase.GetRecipeDetailsUseCase
import com.example.androiddevelopment2.presentation.RecipeDetailsScreenState.FailureReason
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _detailsState = MutableStateFlow<RecipeDetailsScreenState>(RecipeDetailsScreenState.Loading)
    val detailsState: StateFlow<RecipeDetailsScreenState> = _detailsState

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            getRecipeDetailsUseCase(recipeId)
                .onSuccess { recipeDetails ->
                    _detailsState.update { RecipeDetailsScreenState.Success(recipeDetails) }
                }
                .onFailure { ex ->
                    val reason = when (ex) {
                        is RecipeNotFoundException -> FailureReason.RecipeNotFound
                        is InvalidApiKeyException -> FailureReason.InvalidApiKey
                        else -> FailureReason.NoInternet
//                        is NetworkException -> FailureReason.NoInternet
//                        else -> FailureReason.UnknownError
                    }
                    _detailsState.update { RecipeDetailsScreenState.Failure(reason) }
                }
        }
    }
}

//sealed class DetailsScreenState {
//    object Idle : DetailsScreenState()
//    object Loading : DetailsScreenState()
//    data class Success(val recipeDetails: RecipeDetailsModel) : DetailsScreenState()
//    data class Failure(val reason: FailureReason) : DetailsScreenState()
//}
