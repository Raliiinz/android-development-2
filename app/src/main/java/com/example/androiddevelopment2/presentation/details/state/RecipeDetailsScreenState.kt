//package com.example.androiddevelopment2.presentation.details
//
//import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
//
//sealed class RecipeDetailsScreenState {
//    data object Loading : RecipeDetailsScreenState()
//
//    data class Success(val recipeDetails: RecipeDetailsModel) : RecipeDetailsScreenState()
//
//    data class Failure(val reason: FailureReason) : RecipeDetailsScreenState()
//
//    sealed interface FailureReason {
//        data object NoInternet : FailureReason
//
//        data object InvalidApiKey : FailureReason
//
//        data object RecipeNotFound : FailureReason
//    }
//}
//
////sealed interface DetailsScreenEvent {
////    data object GoBack : DetailsScreenEvent
////}