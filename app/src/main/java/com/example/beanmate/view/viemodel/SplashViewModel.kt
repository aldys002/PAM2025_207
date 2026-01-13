package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    var selesai by mutableStateOf(false)
        private set

    fun mulaiSplash() {
        viewModelScope.launch {
            delay(2000)
            selesai = true
        }
    }
}
