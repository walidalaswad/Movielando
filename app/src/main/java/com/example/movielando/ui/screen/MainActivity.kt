package com.example.movielando.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.movielando.navigation.MovieNavigation
import com.example.movielando.ui.theme.MovielandoTheme

import dagger.hilt.android.AndroidEntryPoint

// The one and only activity in this app
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovielandoTheme() {
                MovieNavigation()
            }
        }
    }
}
