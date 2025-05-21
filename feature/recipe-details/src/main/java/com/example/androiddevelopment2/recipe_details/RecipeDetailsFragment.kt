package com.example.androiddevelopment2.recipe_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.androiddevelopment2.base.theme.AppTheme
import com.example.androiddevelopment2.recipe_details.screen.RecipeDetailsScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {
    private val viewModel: RecipeDetailsViewModel by viewModels()
    private val args: RecipeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    RecipeDetailsScreen(
                        viewModel = viewModel,
                        recipeId = args.recipeId,
                    )
                }
            }
        }
    }
}
