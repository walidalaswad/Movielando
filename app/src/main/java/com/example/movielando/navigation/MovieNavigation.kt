package com.example.movielando.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movielando.ui.screen.favorite.FavoriteScreen
import com.example.movielando.ui.screen.movie.SearchScreen
import com.example.movielando.ui.screen.movie.MovieViewModel
import com.example.movielando.ui.screen.setting.SettingScreen
import com.example.movielando.ui.screen.splash.SplashScreen


@Composable
fun MovieNavigation(){

    val viewModel: MovieViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route){

        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    val options = NavOptions.Builder()
                        .setPopUpTo(Screen.Splash.route, inclusive = true)
                        .build()
                    navController.navigate(
                        Screen.Search.route,
                        options
                    )
                    viewModel.onQueryChanged("en")// Move to dashboard
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(navController, viewModel)
        }

        composable(Screen.Favorite.route) {
            FavoriteScreen(navController, viewModel)
        }

        composable(Screen.Setting.route) {
            SettingScreen(navController, viewModel)
        }
        //composable(MovieScreens.HomeScreen.name) { HomeScreen(navController = navController, favViewModel) }
        //composable(MovieScreens.FavScreen.name) { FavScreen(navController = navController, favViewModel) }


        // url: www.domain.com/detailscreen/movie=12
        /*
        composable(MovieScreens.DetailScreen.name + "/{movie}",
            arguments = listOf(navArgument("movie") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            DetailScreen(navController = navController, movieId = backStackEntry.arguments?.getString("movie"))
        }
        */
        // add more routes and screens here

    }
}