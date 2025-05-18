package com.example.androiddevelopment2.recipe_details.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil3.compose.AsyncImage
import com.example.androiddevelopment2.base.theme.Theme
import com.example.androiddevelopment2.domain.model.RecipeDetailsModel
import com.example.androiddevelopment2.recipe_details.utils.Dimens
import com.example.androiddevelopment2.recipe_details.utils.Typography
import com.example.androiddevelopment2.base.R as R

@Composable
fun RecipeDetailsContentScreen(recipe: RecipeDetailsModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(Theme.spacing.medium)
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.extraLarge))

        RecipeImage(imageUrl = recipe.imageUrl, title = recipe.title)

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        RecipeTitle(title = recipe.title)

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        RecipeMetaInfo(
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings
        )

        Spacer(modifier = Modifier.height(Theme.spacing.large))

        RecipeTextSection(
            title = stringResource(R.string.summary),
            content = recipe.summary
        )

        Spacer(modifier = Modifier.height(Theme.spacing.large))

        RecipeTextSection(
            title = stringResource(R.string.instructions),
            content = recipe.instructions
        )

        Spacer(modifier = Modifier.height(Theme.spacing.extraLarge))
    }
}






//        Spacer(modifier = Modifier.height(Dimens.LargeSpacer))
//
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            elevation = Dimens.CardElevation,
//            shape = RoundedCornerShape(Dimens.RecipeImageCornerRadius)
//        ) {
//            AsyncImage(
//                model = recipe.imageUrl,
//                contentDescription = recipe.title,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(Dimens.RecipeImageHeight),
//                contentScale = ContentScale.Crop,
//                error = painterResource(R.drawable.photo_placeholder),
//                placeholder = painterResource(R.drawable.photo_placeholder)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(Dimens.SmallSpacer))
//
//        Text(
//            text = recipe.title,
//            fontSize = Typography.TitleTextSize,
//            color = colorResource(R.color.black),
//            fontWeight = Typography.TitleFontWeight
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.SmallSpacer))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.weight(1f)
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_time),
//                    contentDescription = stringResource(R.string.time),
//                    modifier = Modifier.size(Dimens.IconSize),
//                    tint = colorResource(R.color.black)
//                )
//                Spacer(modifier = Modifier.width(Dimens.ExtraSmallSpacer))
//                Text(
//                    text = stringResource(R.string.ready_in_minutes_format, recipe.readyInMinutes),
//                    fontSize = Typography.MetaTextSize,
//                )
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.weight(1f)
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_group),
//                    contentDescription = stringResource(R.string.servings),
//                    modifier = Modifier.size(Dimens.IconSize),
//                    tint = colorResource(R.color.black)
//                )
//                Spacer(modifier = Modifier.width(Dimens.ExtraSmallSpacer))
//                Text(
//                    text = stringResource(R.string.servings_format, recipe.servings),
//                    fontSize = Typography.MetaTextSize
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(Dimens.MediumSpacer))
//
//        Text(
//            text = stringResource(R.string.summary),
//            fontSize = Typography.SectionTitleTextSize,
//            color = colorResource(R.color.black),
//            fontWeight = Typography.SectionTitleFontWeight
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.ExtraSmallSpacer))
//
//        Text(
//            text = recipe.summary,
//            lineHeight = Typography.SectionTitleTextSize
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.MediumSpacer))
//
//        Text(
//            text = stringResource(R.string.instructions),
//            fontSize = Typography.SectionTitleTextSize,
//            color = colorResource(R.color.black),
//            fontWeight = Typography.SectionTitleFontWeight
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.ExtraSmallSpacer))
//
//        Text(
//            text = recipe.instructions,
//            lineHeight = Typography.SectionTitleTextSize,
//
//        )
//
//        Spacer(modifier = Modifier.height(Dimens.LargeSpacer))
//    }
//}

@Composable
fun RecipeImage(imageUrl: String, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = Theme.shape.cardElevation,
        shape = Theme.shape.recipeImageCorner
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .fillMaxWidth()
                .height(Theme.spacing.recipeImageHeight),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.photo_placeholder),
            placeholder = painterResource(R.drawable.photo_placeholder)
        )
    }
}

@Composable
fun RecipeTitle(title: String) {
    Text(
        text = title,
        style = Theme.typography.title,
        fontSize = Typography.TitleTextSize
    )
}

@Composable
fun RecipeMetaInfo(readyInMinutes: Int, servings: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetaInfoItem(
            iconRes = R.drawable.ic_time,
            text = stringResource(R.string.ready_in_minutes_format, readyInMinutes)
        )

        MetaInfoItem(
            iconRes = R.drawable.ic_group,
            text = stringResource(R.string.servings_format, servings)
        )
    }
}

@Composable
fun MetaInfoItem(@DrawableRes iconRes: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(Theme.spacing.iconSize),
            tint = Theme.colors.onBackground
        )
        Spacer(modifier = Modifier.width(Dimens.ExtraSmallSpacer))
        Text(
            text = text,
            fontSize = Typography.MetaTextSize,
        )
    }
}

@Composable
fun RecipeTextSection(title: String, content: String) {
    Text(
        text = title,
        fontSize = Typography.SectionTitleTextSize,
        color = colorResource(R.color.black),
        fontWeight = Typography.SectionTitleFontWeight
    )

    Spacer(modifier = Modifier.height(Dimens.ExtraSmallSpacer))

    Text(
        text = content,
        lineHeight = Typography.SectionTitleTextSize
    )
}