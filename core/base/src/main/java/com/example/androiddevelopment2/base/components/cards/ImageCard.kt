package com.example.androiddevelopment2.base.components.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.androiddevelopment2.base.R

@Composable
fun ImageCard(
    imageUrl: String,
    contentDescription: String,
    imageHeight: Dp,
    cornerShape: CornerBasedShape,
    elevation: Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(elevation),
        shape =  cornerShape
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.photo_placeholder),
            placeholder = rememberAsyncImagePainter(R.drawable.photo_placeholder)
        )
    }
}