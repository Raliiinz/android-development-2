package com.example.androiddevelopment2.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.usecase.SearchRecipesUseCase
import com.example.androiddevelopment2.presentation.base.navigation.NavMain
import com.example.androiddevelopment2.presentation.search.state.SearchScreenEvent
import com.example.androiddevelopment2.presentation.search.state.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val navMain: NavMain,
) : ViewModel() {

//    private val _searchState = MutableStateFlow<RecipesScreenState>(RecipesScreenState.Loading)
//    val searchState: StateFlow<RecipesScreenState> = _searchState
//
//    fun searchRecipes(ingredients: String) {
//        _searchState.value = RecipesScreenState.Loading
//        viewModelScope.launch {
//            searchRecipesUseCase(ingredients)
//                .onSuccess { recipes ->
//                    _searchState.update { RecipesScreenState.Success(recipes) }
//                }
//                .onFailure { ex ->
//                    val reason = when (ex) {
////                        is EmptyInputException -> FailureReason.EmptyInput
////                        is NoRecipesFoundException -> FailureReason.NoRecipesFound
//                        is InvalidApiKeyException -> RecipesScreenState.FailureReason.InvalidApiKey
//                        else -> RecipesScreenState.FailureReason.NoInternet
////                        is NetworkException -> FailureReason.NoInternet
////                        else -> FailureReason.UnknownError
//                    }
//                    _searchState.update { RecipesScreenState.Failure(reason) }
//                }
//        }
//    }
//
//    fun onTripClicked(recipeId: Int) {
//        navigator.navigateToRecipeDetailsFragment(recipeId)
//    }

    private val _pageState = MutableStateFlow<SearchScreenState>(value = SearchScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _searchFlow = MutableStateFlow(value = "")

    init {
        observeTextChanges()
    }

    fun reduce(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.OnSearchQueryChanged -> {
                _searchFlow.tryEmit(event.query)
            }

            is SearchScreenEvent.OnListItemClick -> {
                navMain.goToDetailsPage(event.recipeId) // Передаем ID
            }

            else -> throw IllegalStateException("Incorrect event: $event")
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeTextChanges() {
        viewModelScope.launch {
            _searchFlow
                .debounce(1000L)
                .collect { ingredients ->
                    if (ingredients.isEmpty()) return@collect
                    searchForQuery(ingredients = ingredients )
                }
        }
    }

    private fun searchForQuery(ingredients: String) {
        viewModelScope.launch {
            _pageState.value = SearchScreenState.Loading
            delay(2000L)
            runCatching {
                searchRecipesUseCase.invoke(ingredients = ingredients)
            }.onSuccess { result ->
                _pageState.value = SearchScreenState.SearchResult(result = result)
            }.onFailure {
                _pageState.value = SearchScreenState.Error(
                    message = it.message,
                    ex = it
                )
            }
        }
    }
}