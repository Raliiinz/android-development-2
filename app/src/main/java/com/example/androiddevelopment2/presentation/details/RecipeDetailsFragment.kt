package com.example.androiddevelopment2.presentation.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androiddevelopment2.R
import com.example.androiddevelopment2.databinding.FragmentRecipeDetailsBinding
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.presentation.details.state.DetailsScreenEvent
import com.example.androiddevelopment2.presentation.details.state.DetailsScreenState
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
        setupListeners()
        observeViewModel()
        viewModel.getRecipeDetails(args.recipeId)
    }

    private fun setupListeners() {
//        viewBinding.toolbar.setNavigationOnClickListener {
//            viewModel.reduce(DetailsScreenEvent.OnBackClicked)
//        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.detailsState.collect { state ->
                when (state) {
                    DetailsScreenState.Initial -> Unit
                    DetailsScreenState.Loading -> showLoading()
                    is DetailsScreenState.DetailsResult -> showRecipeDetails(state.result)
                    is DetailsScreenState.Error -> showError(state.message.toString())
                }
            }
        }
    }

    private fun showLoading() {
//        with(viewBinding) {
//            progressBar.visibility = View.VISIBLE
//            scrollView.visibility = View.GONE
//        }
    }

    private fun showRecipeDetails(recipe: RecipeDetailsModel) {
        with(viewBinding) {
//            progressBar.visibility = View.GONE
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

    private fun showError(message: String) {
        with(viewBinding) {
//            progressBar.visibility = View.GONE
            scrollView.visibility = View.GONE

            Toast.makeText(
                requireContext(),
                "Error occurred: $message",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val RECIPE_ID = "RECIPE_ID"

        fun getInstance(recipeId: Int): RecipeDetailsFragment {
            return RecipeDetailsFragment().apply {
                arguments = bundleOf(RECIPE_ID to recipeId)
            }
        }
    }
}