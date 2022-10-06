package com.example.movielando.ui.screen.favorite

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.example.movielando.data.remote.Item
import com.example.movielando.navigation.Screen
import com.example.movielando.ui.screen.movie.MovieViewModel
import com.github.theapache64.twyper.flip.TwyperFlip
import com.github.theapache64.twyper.flip.rememberTwyperFlipController

@Composable
fun FavoriteScreen(
    navController: NavController = rememberNavController(),
    viewModel: MovieViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = Color.Cyan, elevation = 3.dp) {
                Row {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back",
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Search.route) // go back to last screen
                        })

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(text = "My Favorite Movies")
                }
            }
        }
    ) {
        MainContent(navController = navController, movieViewModel = viewModel)
    }


}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainContent(navController: NavController = rememberNavController(), movieViewModel: MovieViewModel)  {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // var movies: List<Movie> = getMovies()

        // val time = movieViewModel.countDownFlow.collectAsState(initial = 10)

        val items: List<Item> by movieViewModel.movies.collectAsState()
        //val items = movieViewModel.favoriteMovies
        val twyperFlipController = rememberTwyperFlipController()

        val generateBoxModifier: () -> Modifier = {
            Modifier
                .height(550.dp)
                .width(320.dp)
        }

        TwyperFlip(
            items = items,
            twyperFlipController = twyperFlipController,
            onItemRemoved = { item, direction ->
                println("Item removed: $item -> $direction")
                movieViewModel.removeMovie(item)
                if(direction.toString() == "RIGHT")
                {
                    if(movieViewModel.exists(item.id))
                    {

                    }
                    movieViewModel.addMovie(item)
                }
            },
            cardModifier = generateBoxModifier,
            onEmpty = {
                println("End reached")
                // Load new card stack
                navController.navigate(Screen.Favorite.route)
            },
            modifier = Modifier,
            front = { item ->
                // Add Movie poster image
                SubcomposeAsyncImage(
                    model = "${item.image}",
                    loading = {
                        CircularProgressIndicator()
                    },
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxHeight()
                )

            },
            back = { item ->
                // Add Movie details
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Column() {
                        ReverseText(item = "${item.title}")
                        //ReverseText(item = "${item.description}")
                    }
                    ReverseText(item = "Genre: ${item.genres}")
                    ReverseText(item = "Plot: ${item.plot}")
                    ReverseText(item = "Actors: ${item.stars}")
                    ReverseText(item = "Votings: ${item.imdbRatingVotes}")
                    ReverseText(item = "Rating: ${item.imdbRating}")
                }

            })

        Spacer(modifier = Modifier.height(50.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(30.dp),
        ) {

            IconButton(onClick = {
                twyperFlipController.swipeLeft()
            }) {
                Text(text = "🗑", fontSize = 40.sp)
            }

            IconButton(onClick = {
                twyperFlipController.flip()
            }) {
                Text(text = "❔", fontSize = 40.sp)
            }

            IconButton(onClick = {
                twyperFlipController.swipeRight()
                // Add to favorites

            }) {
                Text(text = "✔", fontSize = 50.sp)
            }
        }
    }
}


@Composable
fun FrontText(modifier: Modifier = Modifier, item: String){
    Text(modifier = modifier, text = "$item", fontSize = 28.sp, color = Color.White)
}

@Composable
fun ReverseText(modifier: Modifier = Modifier, item: String){
    Text(modifier = modifier, text = item, fontSize = 22.sp, color = Color.White)
}