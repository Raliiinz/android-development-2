package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.androiddevelopment2.base.components.cards.ImageCard
import com.example.androiddevelopment2.base.components.info.InfoRow
import com.example.androiddevelopment2.base.components.sections.TextSection
import com.example.androiddevelopment2.base.theme.Theme
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.base.R as R

@Composable
fun RecipeDetailsContentScreen(recipe: RecipeDetailsModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Theme.spacing.medium)
    ) {
        item {
            Spacer(modifier = Modifier.height(Theme.spacing.extraLarge))

            ImageCard(
                imageUrl = recipe.imageUrl,
                contentDescription = recipe.title,
                imageHeight = Theme.spacing.recipeImageHeight,
                cornerShape = Theme.shape.recipeImageCorner,
                elevation = Theme.shape.cardElevation
            )

            Spacer(modifier = Modifier.height(Theme.spacing.medium))

            Text(
                text = recipe.title,
                style = Theme.typography.title
            )

            Spacer(modifier = Modifier.height(Theme.spacing.medium))

            InfoRow(
                readyInMinutes = recipe.readyInMinutes,
                servings = recipe.servings
            )

            Spacer(modifier = Modifier.height(Theme.spacing.large))

            TextSection(
                title = stringResource(R.string.summary),
                content = recipe.summary,
                titleStyle = Theme.typography.sectionTitle,
                contentStyle = Theme.typography.body,
                spacing = Theme.spacing.small
            )

            Spacer(modifier = Modifier.height(Theme.spacing.large))

            TextSection(
                title = stringResource(R.string.instructions),
                content = recipe.instructions,
                titleStyle = Theme.typography.sectionTitle,
                contentStyle = Theme.typography.body,
                spacing = Theme.spacing.small
            )

            Spacer(modifier = Modifier.height(Theme.spacing.extraLarge))
        }
    }
}
