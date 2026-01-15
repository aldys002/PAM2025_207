package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch

class KelolaProdukViewModel(
    private val repository: RepositoryBeanMate
) : ViewModel() {

    var statusUiProduk by mutableStateOf<StatusUiProduk>(StatusUiProduk.Loading)
        private set

    init {
        loadProduk()
    }

    fun loadProduk() {
        viewModelScope.launch {
            statusUiProduk = StatusUiProduk.Loading
            try {
                val data = repository.getProduk()
                statusUiProduk = StatusUiProduk.Success(data)
            } catch (e: Exception) {
                statusUiProduk = StatusUiProduk.Error
            }
        }
    }

    fun hapusProduk(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.hapusSatuProduk(id)
                if (response.isSuccessful) {
                    loadProduk()
                } else {
                    statusUiProduk = StatusUiProduk.Error
                }
            } catch (e: Exception) {
                loadProduk()
            }
        }
    }
}
