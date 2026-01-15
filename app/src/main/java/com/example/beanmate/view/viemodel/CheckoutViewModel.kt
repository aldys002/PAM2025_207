package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DataCheckoutResponse
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch

sealed class StatusUiCheckout {
    object Idle : StatusUiCheckout()
    object Loading : StatusUiCheckout()
    data class Success(val data: DataCheckoutResponse) : StatusUiCheckout()
    object Error : StatusUiCheckout()
}

class CheckoutViewModel(
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    private val sessionId = "guest_123"

    var totalHarga by mutableStateOf(0)
        private set

    var statusUiCheckout by mutableStateOf<StatusUiCheckout>(StatusUiCheckout.Idle)
        private set

    init {
        loadTotalFromCart()
    }

    private fun loadTotalFromCart() {
        viewModelScope.launch {
            try {
                val cart = repositoryBeanMate.getCart(sessionId)
                totalHarga = cart.sumOf { it.price * it.qty }
            } catch (e: Exception) {
                totalHarga = 0
            }
        }
    }

    fun checkout(nama: String, alamat: String, telepon: String) {
        statusUiCheckout = StatusUiCheckout.Loading

        viewModelScope.launch {
            try {
                val response = repositoryBeanMate.checkout(
                    sessionId,
                    nama,
                    alamat,
                    telepon
                )

                if (response.isSuccessful && response.body() != null) {
                    statusUiCheckout =
                        StatusUiCheckout.Success(response.body()!!)
                } else {
                    statusUiCheckout = StatusUiCheckout.Error
                }
            } catch (e: Exception) {
                statusUiCheckout = StatusUiCheckout.Error
            }
        }
    }

    fun reset() {
        statusUiCheckout = StatusUiCheckout.Idle
    }
}

