package com.example.beanmate2.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue



class HomeViewModel(private val repositoryBeanMate: RepositoryBeanMate) : ViewModel() {

    var listProduk: StatusUiProduk by mutableStateOf(StatusUiProduk.Loading)
        private set

    init { loadProduk() }

    fun loadProduk() {
        listProduk = StatusUiProduk.Loading
        viewModelScope.launch {
            try {
                val hasil = repositoryBeanMate.getProduk()
                listProduk = StatusUiProduk.Success(hasil)
            } catch (e: Exception) {
                listProduk = StatusUiProduk.Error
            }
        }
    }
}
