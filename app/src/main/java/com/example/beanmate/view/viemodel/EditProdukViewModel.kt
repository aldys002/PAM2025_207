package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DetailBeanMate
import com.example.beanmate2.data.UIStateBeanMate
import com.example.beanmate2.data.toDataBeanMate
import com.example.beanmate2.data.toStateBeanMate
import com.example.beanmate2.repositori.RepositoryBeanMate
import com.example.beanmate2.view.route.DestinasiEditProduk
import kotlinx.coroutines.launch

class EditProdukViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var uiStateProduk by mutableStateOf(UIStateBeanMate())
        private set

    private val idProduk: Int = checkNotNull(savedStateHandle[DestinasiEditProduk.itemIdArg])

    init {
        viewModelScope.launch {
            uiStateProduk = repositoryBeanMate.getSatuProduk(idProduk).toStateBeanMate(true)
        }
    }

    fun updateUiState(detail: DetailBeanMate) {
        uiStateProduk = UIStateBeanMate(
            detailBeanMate = detail,
            isEntryValid = validasiInput(detail)
        )
    }

    private fun validasiInput(uiState: DetailBeanMate = uiStateProduk.detailBeanMate): Boolean {
        return with(uiState) {
            name.isNotBlank() &&
                    description.isNotBlank() &&
                    image.isNotBlank() &&
                    price > 0
        }
    }

    suspend fun editProduk(): Boolean {
        if (!validasiInput()) return false

        return try {
            val res = repositoryBeanMate.editSatuProduk(
                idProduk,
                uiStateProduk.detailBeanMate.toDataBeanMate()
            )
            res.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
