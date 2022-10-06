package com.example.movielando.ui.screen.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielando.util.flow.mutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val SPLASH_DURATION_IN_MILLIS = 1500L
    }

    var versionName by mutableStateOf("v0.0.1")
        private set

    private val _isSplashFinished = mutableEventFlow<Boolean>()
    val isSplashFinished: SharedFlow<Boolean> = _isSplashFinished

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION_IN_MILLIS)
            _isSplashFinished.emit(true)
        }
    }
}