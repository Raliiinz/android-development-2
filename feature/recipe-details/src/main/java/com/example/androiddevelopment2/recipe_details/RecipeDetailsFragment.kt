package com.example.androiddevelopment2.recipe_details

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.recipe_details.databinding.FragmentRecipeDetailsBinding
import com.example.androiddevelopment2.recipe_details.state.DetailsErrorEvent
import com.example.androiddevelopment2.recipe_details.state.DetailsScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.androiddevelopment2.recipe_details.R as detailsR

@AndroidEntryPoint
class RecipeDetailsFragment: Fragment(detailsR.layout.fragment_recipe_details) {
    private val viewBinding: FragmentRecipeDetailsBinding by viewBinding(
        FragmentRecipeDetailsBinding::bind)
    private val viewModel: RecipeDetailsViewModel by viewModels()

    private val args: RecipeDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeErrors()
        viewModel.getRecipeDetails(args.recipeId)
    }

    private fun observeState() {
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
            shimmerContainer.run {
                visibility = View.VISIBLE
                startShimmer()
            }
            scrollView.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            shimmerContainer.run {
                stopShimmer()
                visibility = View.GONE
            }
            scrollView.visibility = View.VISIBLE
        }
    }

    private fun showRecipeDetails(recipe: RecipeDetailsModel) {
        hideLoading()

        with(viewBinding) {
            Glide.with(requireContext())
                .load(recipe.imageUrl)
                .error(R.drawable.photo_placeholder)
                .fallback(R.drawable.photo_placeholder)
                .into(recipeImage)
            recipeTitleTextView.text = recipe.title
            summaryTextView.text = recipe.summary
            instructionsTextView.text = recipe.instructions
            readyInMinutesTextView.text = getString(R.string.ready_in_minutes_format, recipe.readyInMinutes)
            servingsTextView.text = getString(R.string.servings_format, recipe.servings)
        }
    }

    private fun observeErrors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorEvent.collect { event ->
                when (event) {
                    is DetailsErrorEvent.Error -> {
                        showErrorDialog(event.reason)
                    }
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
