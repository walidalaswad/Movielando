package com.example.movielando.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Search : Screen("search")
    object Favorite : Screen("favorite")
    object Setting : Screen("setting")
}