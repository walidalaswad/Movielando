package com.example.movielando.ui.screen.movie

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movielando.navigation.Screen
import com.github.theapache64.twyper.rememberTwyperController




@Composable
fun SearchScreen(
    navController: NavController = rememberNavController(),
    viewModel: MovieViewModel
) {
    var showMenu by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Movielando") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                    }

                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(onClick = { navController.navigate(Screen.Favorite.route) }) {
                            Row{
                                Icon(imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorites",
                                    modifier = Modifier.padding(4.dp))
                                Text(text = "Favorites",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(100.dp))
                            }

                        }
                        DropdownMenuItem(onClick = { navController.navigate(Screen.Setting.route) }) {
                            Row{
                                Icon(imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    modifier = Modifier.padding(4.dp))
                                Text(text = "Settings",
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(100.dp))
                            }

                        }
                    }

                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp),
        ) {
            Spacer(modifier = Modifier.height(1.dp))

            Spacer(modifier = Modifier.height(10.dp))

            viewModel.uiState.subTitle?.let { parallelMsg ->
                Text(
                    text = parallelMsg,
                    modifier = Modifier.align(CenterHorizontally),
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            val twyperController = rememberTwyperController()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
            ) {
                val loadingMessage = viewModel.uiState.loadingMsg
                val errorMessage = viewModel.uiState.blockingMsg
                when {
                    loadingMessage != null -> {
                        Loading(loadingMessage)
                    }
                    errorMessage != null -> {
                        Text(text = errorMessage)
                    }
                    else -> {
                        Cards(
                            items = viewModel.uiState.items,
                            onItemSwipedOut = viewModel::onItemSwipedOut,
                            onEndReached = {
                                viewModel.onPageEndReached()
                            },
                            modifier = Modifier.padding(10.dp),
                            twyperController = twyperController
                        )
                    }
                }
            }

            if (viewModel.uiState.items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(25.dp))
                Controllers(
                    twyperController = twyperController,
                    modifier = Modifier.align(CenterHorizontally)
                )
            }

        }
    }




}

