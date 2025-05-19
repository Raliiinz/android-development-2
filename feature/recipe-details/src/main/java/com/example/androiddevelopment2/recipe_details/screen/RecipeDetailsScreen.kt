package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.androiddevelopment2.recipe_details.RecipeDetailsViewModel
import com.example.androiddevelopment2.recipe_details.state.DetailsErrorEvent
import com.example.androiddevelopment2.recipe_details.state.DetailsScreenState

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    recipeId: Int
) {
    val state by viewModel.detailsState.collectAsState()
    val errorEvent by viewModel.errorEvent.collectAsState(initial = null)

    ErrorHandler(errorEvent)

    LaunchedEffect(recipeId) {
        viewModel.getRecipeDetails(recipeId)
    }

    when (val currentState = state) {
        DetailsScreenState.Initial -> Unit
        DetailsScreenState.Loading -> ShimmerLoadingScreen()
        is DetailsScreenState.DetailsResult -> RecipeDetailsContentScreen(currentState.result)
    }
}

@Composable
private fun ErrorHandler(errorEvent: DetailsErrorEvent?) {
    var showError by remember { mutableStateOf(false) }
    var currentError by remember { mutableStateOf<DetailsErrorEvent.Error?>(null) }

    LaunchedEffect(errorEvent) {
        if (errorEvent is DetailsErrorEvent.Error) {
            currentError = errorEvent
            showError = true
        }
    }

    if (showError && currentError != null) {
        ErrorDialogFactory.Create(
            errorEvent = currentError!!,
            onDismiss = { showError = false }
        )
    }
}
