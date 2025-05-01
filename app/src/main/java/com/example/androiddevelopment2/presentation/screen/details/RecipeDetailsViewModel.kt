package com.example.androiddevelopment2.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.exception.BadRequestException
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.exception.NetworkException
import com.example.androiddevelopment2.domain.exception.NotFoundException
import com.example.androiddevelopment2.domain.exception.ServerException
import com.example.androiddevelopment2.domain.exception.UnauthorizedException
import com.example.androiddevelopment2.domain.usecase.GetRecipeDetailsUseCase
import com.example.androiddevelopment2.presentation.screen.details.state.DetailsErrorEvent
import com.example.androiddevelopment2.presentation.screen.details.state.DetailsErrorEvent.FailureReason
import com.example.androiddevelopment2.presentation.screen.details.state.DetailsScreenState
import com.example.androiddevelopment2.presentation.utils.Constants.LOADING_DELAY_MS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _detailsState = MutableStateFlow<DetailsScreenState>(DetailsScreenState.Initial)
    val detailsState =_detailsState.asStateFlow()

    private val _errorEvent = MutableSharedFlow< DetailsErrorEvent>()
    val errorEvent = _errorEvent.asSharedFlow()

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _detailsState.update { DetailsScreenState.Loading }
            delay(LOADING_DELAY_MS)
            runCatching {
                getRecipeDetailsUseCase.invoke(recipeId)
            }.onSuccess { result ->
                _detailsState.update { DetailsScreenState.DetailsResult(result) }
            }.onFailure {
                handleError(it)
            }
        }
    }

    private suspend fun handleError(ex: Throwable) {
        val errorReason = when (ex) {
            is UnauthorizedException -> FailureReason.Unauthorized
            is ForbiddenException -> FailureReason.Forbidden
            is NotFoundException -> FailureReason.NotFound
            is BadRequestException -> FailureReason.BadRequest
            is ServerException -> FailureReason.Server
            is NetworkException -> FailureReason.Network
            else -> FailureReason.Unknown
        }
        _errorEvent.emit(DetailsErrorEvent.Error(errorReason))
        _detailsState.update { DetailsScreenState.Initial }
    }
}
