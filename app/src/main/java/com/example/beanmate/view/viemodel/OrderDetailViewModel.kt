package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DataOrderDetailResponse
import com.example.beanmate2.repositori.RepositoryBeanMate
import com.example.beanmate2.view.route.DestinasiOrderDetail
import kotlinx.coroutines.launch

sealed class StatusUiOrderDetail {
    object Loading : StatusUiOrderDetail()
    data class Success(val detail: DataOrderDetailResponse) : StatusUiOrderDetail()
    object Error : StatusUiOrderDetail()
}

class OrderDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var statusUiOrderDetail: StatusUiOrderDetail by mutableStateOf(StatusUiOrderDetail.Loading)
        private set

    private val orderId: Int = checkNotNull(savedStateHandle[DestinasiOrderDetail.itemIdArg])

    init {
        loadDetail()
    }

    fun loadDetail() {
        statusUiOrderDetail = StatusUiOrderDetail.Loading
        viewModelScope.launch {
            try {
                val response = repositoryBeanMate.getOrderDetail(orderId)
                statusUiOrderDetail = StatusUiOrderDetail.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                statusUiOrderDetail = StatusUiOrderDetail.Error
            }
        }
    }
}
