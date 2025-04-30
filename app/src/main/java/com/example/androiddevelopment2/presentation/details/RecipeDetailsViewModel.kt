package com.example.androiddevelopment2.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.usecase.GetRecipeDetailsUseCase
import com.example.androiddevelopment2.presentation.base.navigation.NavMain
import com.example.androiddevelopment2.presentation.details.state.DetailsScreenEvent
import com.example.androiddevelopment2.presentation.details.state.DetailsScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val navMain: NavMain,
) : ViewModel() {

    private val _detailsState = MutableStateFlow<DetailsScreenState>(DetailsScreenState.Initial)
    val detailsState: StateFlow<DetailsScreenState> = _detailsState

    fun reduce(event: DetailsScreenEvent) {
//        when (event) {
//            DetailsScreenEvent.OnBackClicked -> navMain.navigateUp()
//        }
    }

    fun getRecipeDetails(recipeId: Int) {
        viewModelScope.launch {
            _detailsState.update { DetailsScreenState.Loading }
            runCatching {
                getRecipeDetailsUseCase.invoke(recipeId)
            }.onSuccess { result ->
                _detailsState.update { DetailsScreenState.DetailsResult(result) }
            }.onFailure {

                _detailsState.value = DetailsScreenState.Error(
                    message = it.message,
                    ex = it
                )
            }
//                .onSuccess { recipeDetails ->
//                    _detailsState.update { RecipeDetailsScreenState.Success(recipeDetails) }
//                }
//                .onFailure { ex ->
//                    val reason = when (ex) {
//                        is RecipeNotFoundException -> FailureReason.RecipeNotFound
//                        is InvalidApiKeyException -> FailureReason.InvalidApiKey
//                        else -> FailureReason.NoInternet
////                        is NetworkException -> FailureReason.NoInternet
////                        else -> FailureReason.UnknownError
//                    }
//                    _detailsState.update { RecipeDetailsScreenState.Failure(reason) }
//                }
        }
    }
}

//sealed class DetailsScreenState {
//    object Idle : DetailsScreenState()
//    object Loading : DetailsScreenState()
//    data class Success(val recipeDetails: RecipeDetailsModel) : DetailsScreenState()
//    data class Failure(val reason: FailureReason) : DetailsScreenState()
//}
