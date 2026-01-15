package com.example.beanmate2.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DataOrder
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

sealed class StatusUiOrders {
    object Loading : StatusUiOrders()
    data class Success(val orders: List<DataOrder>) : StatusUiOrders()
    object Error : StatusUiOrders()
}

class OrdersViewModel(
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var statusUiOrders by mutableStateOf<StatusUiOrders>(StatusUiOrders.Loading)
        private set

    init {
        loadOrders()
    }

    fun loadOrders() {
        statusUiOrders = StatusUiOrders.Loading
        viewModelScope.launch {
            try {
                val orders = repositoryBeanMate.getOrders()
                statusUiOrders = StatusUiOrders.Success(orders)
            } catch (e: Exception) {
                statusUiOrders = StatusUiOrders.Error
            }
        }
    }
}
