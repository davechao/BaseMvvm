package com.dabenxiang.mvvm.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dabenxiang.mvvm.R

@Composable
fun buildNewsStory() {
    val image = imageResource(R.drawable.header)
    Column(modifier = Modifier.padding(16.dp)) {

        val imageModifier = Modifier
            .preferredHeight(180.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(4.dp))

        Image(image, modifier = imageModifier, contentScale = ContentScale.Crop)

        Spacer(Modifier.preferredHeight(16.dp))

        Text(
            "A day wandering through the sandhills in Shark Fin Cove, and a few of the sights I saw",
            style = typography.h6,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text("Davenport, California", style = typography.body2)
        Text("December 2018", style = typography.body2)
    }
}
