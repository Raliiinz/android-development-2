package com.example.androiddevelopment2.recipe_details.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.androiddevelopment2.base.components.loading.ShimmerBox
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun ShimmerLoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = Theme.spacing.extraLarge + Theme.spacing.medium,
                start = Theme.spacing.medium,
                end = Theme.spacing.medium
            )
    ) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(Theme.spacing.recipeImageHeight)
                .clip(Theme.shape.recipeImageCorner)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(Theme.typography.shimmerTitleHeight)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(Theme.typography.shimmerMetaHeight)
                    .padding(end = Theme.spacing.small))
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(Theme.typography.shimmerMetaHeight))
        }

        Spacer(modifier = Modifier.height(Theme.spacing.large))

        repeat(2) { section ->
            ShimmerBox(
                modifier = Modifier
                    .width(150.dp)
                    .height(Theme.typography.shimmerSectionTitleHeight)
            )

            Spacer(Modifier.height(Theme.spacing.small))

            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Theme.typography.shimmerSectionContentHeight)
            )

            if (section == 0) Spacer(Modifier.height(Theme.spacing.large))
        }
    }
}

