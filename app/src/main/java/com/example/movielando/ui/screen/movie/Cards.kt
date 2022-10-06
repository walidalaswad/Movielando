package com.example.movielando.ui.screen.movie

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.movielando.data.remote.Item
import com.github.theapache64.twyper.SwipedOutDirection
import com.github.theapache64.twyper.Twyper
import com.github.theapache64.twyper.TwyperController


@Composable
fun Cards(
    items: List<Item>,
    onItemSwipedOut: (Item, SwipedOutDirection) -> Unit,
    onEndReached: () -> Unit,
    twyperController: TwyperController,
    modifier: Modifier = Modifier
) {
    Twyper(
        items = items,
        twyperController = twyperController,
        onItemRemoved = onItemSwipedOut,
        onEmpty = onEndReached,
        modifier = modifier
    ) { item -> Card(item) }
}

@Composable
private fun Card(item: Item) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xffFFEE58),
                            Color(0xffFFF9C4)
                        )
                    )
                )
                .fillMaxWidth()
                .padding(10.dp)
                .weight(1f)
        ) {
            // Repo name
            Image(
                painter = rememberImagePainter(
                    data = item.image,
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )


            // Stars
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 18.sp)) {
                        append(text = "⭐️\n")
                    }
                    withStyle(style = SpanStyle(fontSize = 13.sp)) {
                        append(text = "${item.imdbRating}")
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Blue.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(10.dp)
        ) {

            Text(
                text = item.title,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            )

            Column {

            }
        }
    }
}