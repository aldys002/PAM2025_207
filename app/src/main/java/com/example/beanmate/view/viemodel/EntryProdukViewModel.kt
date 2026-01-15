package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DetailBeanMate
import com.example.beanmate2.data.UIStateBeanMate
import com.example.beanmate2.data.UploadsListResponse
import com.example.beanmate2.data.toDataBeanMate
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch

sealed class StatusUiUploads {
    object Idle : StatusUiUploads()
    object Loading : StatusUiUploads()
    data class Success(val files: List<String>) : StatusUiUploads()
    data class Error(val msg: String = "Gagal memuat gambar") : StatusUiUploads()
}

class EntryProdukViewModel(
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var uiStateProduk by mutableStateOf(UIStateBeanMate())
        private set

    var statusUiUploads by mutableStateOf<StatusUiUploads>(StatusUiUploads.Idle)
        private set

    init {
        loadUploads()
    }

    fun loadUploads() {
        statusUiUploads = StatusUiUploads.Loading
        viewModelScope.launch {
            try {
                val res: UploadsListResponse = repositoryBeanMate.getUploads()
                if (res.status.lowercase() == "success") {
                    statusUiUploads = StatusUiUploads.Success(res.data)
                } else {
                    statusUiUploads = StatusUiUploads.Error(res.message.ifBlank { "Gagal memuat gambar" })
                }
            } catch (e: Exception) {
                statusUiUploads = StatusUiUploads.Error(e.message ?: "Gagal memuat gambar")
            }
        }
    }

    private fun validasiInput(uiState: DetailBeanMate = uiStateProduk.detailBeanMate): Boolean {
        return with(uiState) {
            name.isNotBlank() &&
                    description.isNotBlank() &&
                    image.isNotBlank() &&
                    price > 0
        }
    }

    fun updateUiState(detail: DetailBeanMate) {
        uiStateProduk = UIStateBeanMate(
            detailBeanMate = detail,
            isEntryValid = validasiInput(detail)
        )
    }

    fun pilihGambar(filename: String) {
        updateUiState(uiStateProduk.detailBeanMate.copy(image = filename))
    }

    suspend fun addProduk(): Boolean {
        if (!validasiInput()) return false

        return try {
            val res = repositoryBeanMate.postProduk(uiStateProduk.detailBeanMate.toDataBeanMate())
            res.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
