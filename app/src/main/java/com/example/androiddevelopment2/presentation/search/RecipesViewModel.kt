package com.example.androiddevelopment2.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.exception.BadRequestException
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.exception.NotFoundException
import com.example.androiddevelopment2.domain.exception.ServerException
import com.example.androiddevelopment2.domain.exception.UnauthorizedException
import com.example.androiddevelopment2.domain.usecase.SearchRecipesUseCase
import com.example.androiddevelopment2.presentation.base.navigation.NavMain
import com.example.androiddevelopment2.presentation.search.state.SearchErrorEvent
import com.example.androiddevelopment2.presentation.search.state.SearchErrorEvent.ServerFailureReason
import com.example.androiddevelopment2.presentation.search.state.SearchErrorEvent.ValidationFailureReason
import com.example.androiddevelopment2.presentation.search.state.SearchScreenEvent
import com.example.androiddevelopment2.presentation.search.state.SearchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val navMain: NavMain,
) : ViewModel() {

    private val _pageState = MutableStateFlow<SearchScreenState>(value = SearchScreenState.Initial)
    val pageState = _pageState.asStateFlow()

    private val _errorEvent = MutableSharedFlow<SearchErrorEvent>()
    val errorEvent = _errorEvent.asSharedFlow()

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
                navMain.goToDetailsPage(event.recipeId)
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
                    when {
                        ingredients.isBlank() -> {
                            _pageState.update { SearchScreenState.Initial }
                            _errorEvent.emit(SearchErrorEvent.ValidationError(
                                ValidationFailureReason.EmptyInput
                            ))
                        }
                        ingredients.length < 3 -> {
                            _pageState.update { SearchScreenState.Initial }
                            _errorEvent.emit(SearchErrorEvent.ValidationError(
                                ValidationFailureReason.MinLength
                            ))
                        }
                        !isValidIngredientsFormat(ingredients) -> {
                            _pageState.update { SearchScreenState.Initial }
                            _errorEvent.emit(SearchErrorEvent.ValidationError(
                                ValidationFailureReason.InvalidFormat
                            ))
                        }
                        else -> {
                            _errorEvent.emit(SearchErrorEvent.ClearValidationError)
                            searchForQuery(ingredients)
                        }
                    }
                }
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
            delay(2000L)
            runCatching {
                searchRecipesUseCase.invoke(ingredients = ingredients)
            }.onSuccess { result ->
                _pageState.update { SearchScreenState.SearchResult(result = result) }
            }.onFailure {
                handleError(it)
            }
//            }.onFailure { ex ->
//                val errorMessage = when (ex) {
//                    is UnauthorizedException -> "Вы не авторизованы. Пожалуйста, войдите в систему."
//                    is ForbiddenException -> "Доступ запрещен. У вас нет прав."
//                    is NotFoundException -> "Рецепты не найдены."
//                    is ServerException -> "Ошибка на сервере. Попробуйте позже."
//                    is IOException -> "Нет подключения к интернету."
//                    else -> ex.message ?: "Неизвестная ошибка"
//                }
//
//                _pageState.value = SearchScreenState.Error(
//                    message = errorMessage,
//                    ex = ex
//                )
//            }
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
}