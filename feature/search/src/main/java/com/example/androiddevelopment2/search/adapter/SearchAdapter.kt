package com.example.androiddevelopment2.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.domain.model.RecipeModel
import com.example.androiddevelopment2.search.databinding.ItemRecipeBinding

class SearchAdapter(
    private val requestManager: RequestManager,
    private val onItemClick: (RecipeModel) -> Unit
) : ListAdapter<RecipeModel, SearchAdapter.RecipeViewHolder>(SearchDiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecipeViewHolder(binding, requestManager)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RecipeViewHolder(
        private val binding: ItemRecipeBinding,
        private val requestManager: RequestManager,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick(getItem(adapterPosition))
            }
        }

        fun bind(recipe: RecipeModel) = with(binding) {
            recipeTitleTv.text = recipe.title
            usedIngredientsTv.text = binding.root.context.getString(
                R.string.used_ingredients,
                recipe.usedIngredients
            )
            missingIngredientsTextView.text = binding.root.context.getString(
                R.string.missing_ingredients,
                recipe.missedIngredients
            )
            requestManager
                .load(recipe.imageUrl)
                .error(R.drawable.photo_placeholder)
                .fallback(R.drawable.photo_placeholder)
                .into(binding.recipeImage)
        }
    }
}
