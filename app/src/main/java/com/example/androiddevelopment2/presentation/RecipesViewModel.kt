package com.example.androiddevelopment2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.exception.InvalidApiKeyException
import com.example.androiddevelopment2.presentation.RecipesScreenState.FailureReason
import com.example.androiddevelopment2.domain.usecase.SearchRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class RecipesViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow<RecipesScreenState>(RecipesScreenState.Loading)
    val searchState: StateFlow<RecipesScreenState> = _searchState

    fun searchRecipes(ingredients: String) {
        _searchState.value = RecipesScreenState.Loading
        viewModelScope.launch {
            searchRecipesUseCase(ingredients)
                .onSuccess { recipes ->
                    _searchState.update { RecipesScreenState.Success(recipes) }
                }
                .onFailure { ex ->
                    val reason = when (ex) {
//                        is EmptyInputException -> FailureReason.EmptyInput
//                        is NoRecipesFoundException -> FailureReason.NoRecipesFound
                        is InvalidApiKeyException -> FailureReason.InvalidApiKey
                        else -> FailureReason.NoInternet
//                        is NetworkException -> FailureReason.NoInternet
//                        else -> FailureReason.UnknownError
                    }
                    _searchState.update { RecipesScreenState.Failure(reason) }
                }
        }
    }
}

// Состояния экранов
//sealed class SearchScreenState {
//    object Idle : SearchScreenState()
//    object Loading : SearchScreenState()
//    data class Success(val recipes: List<RecipeModel>) : SearchScreenState()
//    data class Failure(val reason: FailureReason) : SearchScreenState()
//}

// Причины ошибок
//enum class FailureReason {
//    EmptyInput,
//    NoRecipesFound,
//    RecipeNotFound,
//    InvalidApiKey,
//    NoInternet,
//    UnknownError
//}
//@HiltViewModel
//class SearchViewModel @Inject constructor(
//    private val searchRecipesUseCase: SearchRecipesUseCase
//) : ViewModel() {
//    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
//    val state: StateFlow<SearchState> = _state
//
//    fun searchRecipes(ingredients: String) {
//        viewModelScope.launch {
//            _state.value = SearchState.Loading
//            _state.value = when (val result = searchRecipesUseCase(ingredients)) {
//                is Result.Success -> SearchState.Success(result.data)
//                is Result.Error -> SearchState.Error(result.message ?: "Unknown error")
//            }
//        }
//    }
//}
//
//sealed class SearchState {
//    object Idle : SearchState()
//    object Loading : SearchState()
//    data class Success(val recipes: List<RecipeModel>) : SearchState()
//    data class Error(val message: String) : SearchState()
//}