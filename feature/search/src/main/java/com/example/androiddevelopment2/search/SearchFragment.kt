package com.example.androiddevelopment2.search

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.domain.model.RecipeResult
import com.example.androiddevelopment2.search.adapter.SearchAdapter
import com.example.androiddevelopment2.search.databinding.FragmentRecipesBinding
import com.example.androiddevelopment2.search.state.SearchErrorEvent
import com.example.androiddevelopment2.search.state.SearchScreenEvent
import com.example.androiddevelopment2.search.state.SearchScreenState
import com.example.androiddevelopment2.search.state.SearchUiEvent
import com.example.androiddevelopment2.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.androiddevelopment2.search.R as searchR

@AndroidEntryPoint
class SearchFragment: Fragment(searchR.layout.fragment_recipes) {
    private val viewBinding: FragmentRecipesBinding by viewBinding(FragmentRecipesBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private var rvAdapter: SearchAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchButton()
        observeState()
        observeErrors()
        observeUiEvents()
        setupFab()
        setUpCustomButton()
    }

    private fun setUpCustomButton() {
        viewBinding.customButton.setOnClickListener {
            viewModel.navigateToCustom()
        }
    }

    private fun setupRecyclerView() {
        if (rvAdapter == null) {
            rvAdapter = SearchAdapter(
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

    private fun setupSearchButton() {
        viewBinding.searchButton.setOnClickListener {
            val query = viewBinding.etSearch.text.toString()
            viewModel.reduce(event = SearchScreenEvent.OnSearchButtonClicked(query))
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pageState.collect { state ->
                when (state) {
                    is SearchScreenState.Loading -> {
                        showLoading()
                    }
                    is SearchScreenState.SearchResult -> {
                        hideLoading()
                        rvAdapter?.submitList(state.result)
                    }
                    is SearchScreenState.Initial -> {
                        hideLoading()
                        rvAdapter?.submitList(emptyList())
                    }
                }
            }
        }
    }

    private fun showLoading() {
        hideKeyboard()
        with(viewBinding) {
            shimmerContainer.run {
                visibility = View.VISIBLE
                startShimmer()
            }
            rvSearchResult.visibility = View.INVISIBLE
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            shimmerContainer.run {
                stopShimmer()
                visibility = View.GONE
            }
            rvSearchResult.visibility = View.VISIBLE
        }
    }

    private fun observeErrors() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorEvent.collect { event ->
                when (event) {
                    is SearchErrorEvent.ValidationError -> {
                        showValidationError(event.reason)
                    }
                    is SearchErrorEvent.ServerError -> {
                        showServerErrorDialog(event.reason)
                    }
                    SearchErrorEvent.ClearValidationError -> {
                        viewBinding.textInputSearch.error = null
                    }
                }
            }
        }
    }

    private fun observeUiEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is SearchUiEvent.ShowDataSourceToast -> showDataSourceToast(event.source)
                    SearchUiEvent.ShowFeatureDisabledMessage -> showSnackbar("This feature is currently unavailable")
                }
            }
        }
    }

    private fun setupFab() {
        viewBinding.fabGraph.setOnClickListener {
            viewModel.reduce(event = SearchScreenEvent.OnGraphButtonClicked)
        }
    }

    private fun showValidationError(reason: SearchErrorEvent.ValidationFailureReason) {
        viewBinding.textInputSearch.error = when (reason) {
            SearchErrorEvent.ValidationFailureReason.EmptyInput -> getString(R.string.error_empty_input)
            SearchErrorEvent.ValidationFailureReason.MinLength -> getString(R.string.error_min_length)
            SearchErrorEvent.ValidationFailureReason.InvalidFormat -> getString(R.string.error_invalid_format)
        }
    }

    private fun showServerErrorDialog(reason: SearchErrorEvent.ServerFailureReason) {
        hideKeyboard()
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

    private fun showDataSourceToast(source: RecipeResult.Source) {
        val message = when (source) {
            RecipeResult.Source.CACHE -> getString(R.string.toast_data_source_cache)
            RecipeResult.Source.API -> getString(R.string.toast_data_source_api)
        }

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
