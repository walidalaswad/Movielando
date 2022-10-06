package com.example.movielando.ui.screen.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movielando.navigation.Screen
import com.example.movielando.ui.screen.movie.SearchInput
import com.example.movielando.ui.screen.movie.MovieViewModel

@Composable
fun SettingScreen(
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

                    Text(text = "Settings")
                }
            }
        }
    ) {
        MainContent(viewModel = viewModel)
    }


}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainContent(viewModel: MovieViewModel)  {
    SearchInput(
        query = viewModel.uiState.query,
        onQueryChanged = { newQuery ->
            viewModel.onQueryChanged(newQuery)
        },
    )
}