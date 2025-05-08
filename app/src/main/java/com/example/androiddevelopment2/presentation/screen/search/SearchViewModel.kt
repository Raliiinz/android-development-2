package com.example.androiddevelopment2.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.exception.BadRequestException
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.exception.NotFoundException
import com.example.androiddevelopment2.domain.exception.ServerException
import com.example.androiddevelopment2.domain.exception.UnauthorizedException
import com.example.androiddevelopment2.domain.model.RecipeResult
import com.example.androiddevelopment2.domain.usecase.SearchRecipesUseCase
import com.example.androiddevelopment2.presentation.base.navigation.NavMain
import com.example.androiddevelopment2.presentation.screen.search.state.SearchErrorEvent
import com.example.androiddevelopment2.presentation.screen.search.state.SearchErrorEvent.ServerFailureReason
import com.example.androiddevelopment2.presentation.screen.search.state.SearchErrorEvent.ValidationFailureReason
import com.example.androiddevelopment2.presentation.screen.search.state.SearchErrorEvent.ValidationResult
import com.example.androiddevelopment2.presentation.screen.search.state.SearchScreenEvent
import com.example.androiddevelopment2.presentation.screen.search.state.SearchScreenState
import com.example.androiddevelopment2.presentation.screen.search.state.SearchUiEvent
import com.example.androiddevelopment2.presentation.utils.Constants.LOADING_DELAY_MS
import com.example.androiddevelopment2.presentation.utils.Constants.MIN_SEARCH_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val navMain: NavMain
) : ViewModel() {

    private val _pageState = MutableStateFlow<SearchScreenState>(value = SearchScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<SearchErrorEvent>()
    val errorEvent = _errorEvent.asSharedFlow()

    private val _uiEvent = MutableSharedFlow<SearchUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun reduce(event: SearchScreenEvent) {
        when (event) {
            is SearchScreenEvent.OnSearchButtonClicked -> processSearchQuery(event.query)
            is SearchScreenEvent.OnListItemClick -> navigateToDetails(event.recipeId)
        }
    }

    private fun processSearchQuery(query: String) {
        viewModelScope.launch {
            when (val validationResult = validateQuery(query)) {
                is ValidationResult.Valid -> {
                    _errorEvent.emit(SearchErrorEvent.ClearValidationError)
                    searchForQuery(validationResult.query)
                }
                is ValidationResult.Invalid -> {
                    _pageState.update { SearchScreenState.Initial }
                    _errorEvent.emit(SearchErrorEvent.ValidationError(validationResult.reason))
                }
            }
        }
    }

    private fun validateQuery(query: String): ValidationResult {
        return when {
            query.isBlank() -> ValidationResult.Invalid(ValidationFailureReason.EmptyInput)
            query.length < MIN_SEARCH_LENGTH -> ValidationResult.Invalid(ValidationFailureReason.MinLength)
            !isValidIngredientsFormat(query) -> ValidationResult.Invalid(ValidationFailureReason.InvalidFormat)
            else -> ValidationResult.Valid(query)
        }
    }

    private fun isValidIngredientsFormat(input: String): Boolean {
        val trimmed = input.trim()
        return when {
            trimmed.isEmpty() -> false
            !trimmed.contains(",") -> true
            else -> {
                val parts = trimmed.split(",").map { it.trim() }
                parts.all { it.isNotEmpty() } && parts.size > 1
            }
        }
    }

    private fun searchForQuery(ingredients: String) {
        viewModelScope.launch {
            _pageState.update { SearchScreenState.Loading }
            delay(LOADING_DELAY_MS)
            runCatching {
                searchRecipesUseCase.invoke(ingredients)
            }.onSuccess { result ->
                _pageState.update {
                    SearchScreenState.SearchResult(result = result.data)
                }
                _uiEvent.emit(SearchUiEvent.ShowDataSourceToast(result.source))
            }.onFailure {
                _pageState.update { SearchScreenState.Initial }
                handleError(it)
            }
        }
    }


    private suspend fun handleError(ex: Throwable) {
        val errorReason = when (ex) {
            is UnauthorizedException -> ServerFailureReason.Unauthorized
            is ForbiddenException -> ServerFailureReason.Forbidden
            is NotFoundException -> ServerFailureReason.NotFound
            is BadRequestException -> ServerFailureReason.BadRequest
            is ServerException -> ServerFailureReason.Server
            is NetworkException -> ServerFailureReason.Network
            else -> ServerFailureReason.Unknown
        }
        _errorEvent.emit(SearchErrorEvent.ServerError(errorReason))
    }

    private fun navigateToDetails(recipeId: Int) {
        navMain.goToDetailsPage(recipeId)
    }
}
