package com.example.androiddevelopment2.presentation.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androiddevelopment2.R
import com.example.androiddevelopment2.databinding.FragmentRecipesBinding
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.presentation.extensions.hideKeyboard
import com.example.androiddevelopment2.presentation.search.adapter.RecipesAdapter
import com.example.androiddevelopment2.presentation.search.state.SearchErrorEvent
import com.example.androiddevelopment2.presentation.search.state.SearchScreenEvent
import com.example.androiddevelopment2.presentation.search.state.SearchScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment: Fragment(R.layout.fragment_recipes) {
    private val viewBinding: FragmentRecipesBinding by viewBinding(FragmentRecipesBinding::bind)
    private val viewModel: RecipesViewModel by viewModels()
    private var rvAdapter: RecipesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()
        observeErrorEvents()
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            rvAdapter = RecipesAdapter(
                requestManager = Glide.with(this),
                onItemClick = ::onListItemClick,
            )
        }

        viewBinding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }
    }

    private fun onListItemClick(recipeModel: RecipeModel) {
        viewModel.reduce(event = SearchScreenEvent.OnListItemClick(recipeModel.id))
    }

    private fun setupSearch() {
        viewBinding.etSearch.doOnTextChanged { input, _, _, _ ->
            viewBinding.textInputSearch.error = null

            if (input?.isNotEmpty() == true) {
                viewModel.reduce(
                    event = SearchScreenEvent.OnSearchQueryChanged(query = input.toString())
                )
            }
        }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pageState.collect { state ->
                when (state) {
                    is SearchScreenState.Loading -> {
//                        loadingContainer.isVisible = true
//                        loadingContainer.requestFocus()
                    }

                    is SearchScreenState.SearchResult -> {
                        hideKeyboard()
//                        loadingContainer.isVisible = false
//                        loadingContainer.clearFocus()
                        rvAdapter?.submitList(state.result)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun observeErrorEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorEvent.collect { event ->
                when (event) {
                    is SearchErrorEvent.ValidationError -> {
                        showValidationError(event.reason)
                    }
                    is SearchErrorEvent.ServerError -> {
                        hideKeyboard()
                        //loadingContainer.isVisible = false
//////                        loadingContainer.clearFocus()
                        showServerErrorDialog(event.reason)
                    }
                    SearchErrorEvent.ClearValidationError -> {
                        viewBinding.textInputSearch.error = null
                    }
                }
            }
        }
    }

    private fun showValidationError(reason: SearchErrorEvent.ValidationFailureReason) {
        val errorMessage = when (reason) {
            SearchErrorEvent.ValidationFailureReason.EmptyInput ->
                getString(R.string.error_empty_input)
            SearchErrorEvent.ValidationFailureReason.MinLength ->
                getString(R.string.error_min_length)
            SearchErrorEvent.ValidationFailureReason.InvalidFormat ->
                getString(R.string.error_invalid_format)
        }
        viewBinding.textInputSearch.error = errorMessage
    }

    private fun showServerErrorDialog(reason: SearchErrorEvent.ServerFailureReason) {
        val (titleRes, messageRes) = when (reason) {
            SearchErrorEvent.ServerFailureReason.Unauthorized ->
                Pair(R.string.error_title_auth, R.string.error_unauthorized)
            SearchErrorEvent.ServerFailureReason.Forbidden ->
                Pair(R.string.error_title_auth, R.string.error_forbidden)
            SearchErrorEvent.ServerFailureReason.NotFound ->
                Pair(R.string.error_title_server, R.string.error_not_found)
            SearchErrorEvent.ServerFailureReason.BadRequest ->
                Pair(R.string.error_title_validation, R.string.error_bad_request)
            SearchErrorEvent.ServerFailureReason.Server ->
                Pair(R.string.error_title_server, R.string.error_server)
            SearchErrorEvent.ServerFailureReason.Network ->
                Pair(R.string.error_title_network, R.string.error_network)
            SearchErrorEvent.ServerFailureReason.Unknown ->
                Pair(R.string.error_title_unknown, R.string.error_unknown)
        }
        AlertDialog.Builder(requireContext())
            .setTitle(getString(titleRes))
            .setMessage(getString(messageRes))
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

}

