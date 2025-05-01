package com.example.androiddevelopment2.presentation.screen.details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androiddevelopment2.R
import com.example.androiddevelopment2.databinding.FragmentRecipeDetailsBinding
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.presentation.screen.details.state.DetailsErrorEvent
import com.example.androiddevelopment2.presentation.screen.details.state.DetailsScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeDetailsFragment: Fragment(R.layout.fragment_recipe_details) {
    private val viewBinding: FragmentRecipeDetailsBinding by viewBinding(
        FragmentRecipeDetailsBinding::bind)
    private val viewModel: RecipeDetailsViewModel by viewModels()

    private val args: RecipeDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        observeErrorEvents()
        viewModel.getRecipeDetails(args.recipeId)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.detailsState.collect { state ->
                when (state) {
                    DetailsScreenState.Initial -> Unit
                    DetailsScreenState.Loading -> showLoading()
                    is DetailsScreenState.DetailsResult -> showRecipeDetails(state.result)
                }
            }
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            shimmerContainer.visibility = View.VISIBLE
            shimmerContainer.startShimmer()
            scrollView.visibility = View.GONE
        }
    }

    private fun showRecipeDetails(recipe: RecipeDetailsModel) {
        with(viewBinding) {
            shimmerContainer.stopShimmer()
            shimmerContainer.visibility = View.GONE
            scrollView.visibility = View.VISIBLE

            Glide.with(requireContext())
                .load(recipe.imageUrl)
                .into(recipeImage)

            recipeTitleTextView.text = recipe.title
            summaryTextView.text = recipe.summary
            instructionsTextView.text = recipe.instructions
            readyInMinutesTextView.text = getString(R.string.ready_in_minutes_format, recipe.readyInMinutes)
            servingsTextView.text = getString(R.string.servings_format, recipe.servings)
        }
    }

    private fun observeErrorEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorEvent.collect { event ->
                when (event) {
                    is DetailsErrorEvent.Error -> {
                        //loadingContainer.isVisible = false
//////                        loadingContainer.clearFocus()
                        showErrorDialog(event.reason)
                    }
//                    SearchErrorEvent.ClearValidationError -> {
//                        viewBinding.textInputSearch.error = null
//                    }
                }
            }
        }
    }

    private fun showErrorDialog(reason: DetailsErrorEvent.FailureReason) {
        val (titleRes, messageRes) = when (reason) {
            DetailsErrorEvent.FailureReason.Unauthorized ->
                Pair(R.string.error_title_auth, R.string.error_unauthorized)
            DetailsErrorEvent.FailureReason.Forbidden ->
                Pair(R.string.error_title_auth, R.string.error_forbidden)
            DetailsErrorEvent.FailureReason.NotFound ->
                Pair(R.string.error_title_server, R.string.error_not_found)
            DetailsErrorEvent.FailureReason.BadRequest ->
                Pair(R.string.error_title_validation, R.string.error_bad_request)
            DetailsErrorEvent.FailureReason.Server ->
                Pair(R.string.error_title_server, R.string.error_server)
            DetailsErrorEvent.FailureReason.Network ->
                Pair(R.string.error_title_network, R.string.error_network)
            DetailsErrorEvent.FailureReason.Unknown ->
                Pair(R.string.error_title_unknown, R.string.error_unknown)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(titleRes))
            .setMessage(getString(messageRes))
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
