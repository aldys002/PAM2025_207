package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DataBeanMate
import com.example.beanmate2.repositori.RepositoryBeanMate
import com.example.beanmate2.view.route.DestinasiDetailProduk
import kotlinx.coroutines.launch

sealed class StatusUiDetailProduk {
    object Loading : StatusUiDetailProduk()
    data class Success(val produk: DataBeanMate) : StatusUiDetailProduk()
    object Error : StatusUiDetailProduk()
}

class DetailProdukViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var statusUiDetailProduk by mutableStateOf<StatusUiDetailProduk>(StatusUiDetailProduk.Loading)
        private set

    private val idProduk: Int? = savedStateHandle[DestinasiDetailProduk.itemIdArg]

    init {
        if (idProduk != null) {
            loadDetailProduk(idProduk)
        } else {
            statusUiDetailProduk = StatusUiDetailProduk.Error
        }
    }

    private fun loadDetailProduk(id: Int) {
        statusUiDetailProduk = StatusUiDetailProduk.Loading
        viewModelScope.launch {
            try {
                val produk = repositoryBeanMate.getSatuProduk(id)
                statusUiDetailProduk = StatusUiDetailProduk.Success(produk)
            } catch (e: Exception) {
                statusUiDetailProduk = StatusUiDetailProduk.Error
            }
        }
    }
}