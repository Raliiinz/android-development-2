package com.example.androiddevelopment2.recipe_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.androiddevelopment2.recipe_details.state.DetailsErrorEvent
import com.example.androiddevelopment2.recipe_details.state.DetailsScreenState

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    recipeId: Int
) {
    val state by viewModel.detailsState.collectAsState()
    val errorEvent by viewModel.errorEvent.collectAsState(initial = null)

    LaunchedEffect(recipeId) {
        viewModel.getRecipeDetails(recipeId)
    }

    errorEvent?.let { event ->
        if (event is DetailsErrorEvent.Error) {
            val context = LocalContext.current
            LaunchedEffect(event) {
//                showErrorDialog(context, event.reason)
            }
        }
    }

    when (state) {
        DetailsScreenState.Initial,
        DetailsScreenState.Loading -> Unit
        is DetailsScreenState.DetailsResult -> RecipeDetailsContent((state as DetailsScreenState.DetailsResult).result)
    }
}