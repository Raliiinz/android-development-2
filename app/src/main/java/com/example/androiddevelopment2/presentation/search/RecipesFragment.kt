package com.example.androiddevelopment2.presentation.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
//                        hideKeyboard()
//                        loadingContainer.isVisible = false
//                        loadingContainer.clearFocus()
                        rvAdapter?.submitList(state.result)

                    }

                    is SearchScreenState.Error -> {
                        hideKeyboard()
//                        loadingContainer.isVisible = false
//                        loadingContainer.clearFocus()
                        Toast.makeText(
                            requireContext(),
                            "Error occurred: ${state.ex}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}

