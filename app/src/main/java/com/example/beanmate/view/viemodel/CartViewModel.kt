package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beanmate2.data.DataCartItem
import com.example.beanmate2.repositori.RepositoryBeanMate
import kotlinx.coroutines.launch

sealed class StatusUiCart {
    object Loading : StatusUiCart()
    data class Success(val cart: List<DataCartItem>) : StatusUiCart()
    data class Error(val msg: String = "Gagal memuat cart") : StatusUiCart()
}

sealed class UiEventCart {
    object Idle : UiEventCart()
    data class Message(val text: String) : UiEventCart()
}

class CartViewModel(
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var statusUiCart by mutableStateOf<StatusUiCart>(StatusUiCart.Loading)
        private set

    var uiEvent by mutableStateOf<UiEventCart>(UiEventCart.Idle)
        private set

    private val sessionId = "guest_123"

    init {
        loadCart()
    }

    fun clearEvent() {
        uiEvent = UiEventCart.Idle
    }

    fun loadCart() {
        statusUiCart = StatusUiCart.Loading
        viewModelScope.launch {
            try {
                val cart = repositoryBeanMate.getCart(sessionId)
                statusUiCart = StatusUiCart.Success(cart)
            } catch (e: Exception) {
                statusUiCart = StatusUiCart.Error(e.message ?: "Gagal memuat cart")
            }
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            try {
                val res = repositoryBeanMate.addCart(
                    sessionId = sessionId,
                    productId = productId,
                    qty = 1
                )

                val sukses = try {
                    res.isSuccessful
                } catch (_: Exception) {
                    true
                }

                if (sukses) {
                    loadCart()
                    uiEvent = UiEventCart.Message("Berhasil ditambahkan ke keranjang")
                } else {
                    uiEvent = UiEventCart.Message("Gagal tambah ke keranjang")
                }
            } catch (e: Exception) {
                uiEvent = UiEventCart.Message(e.message ?: "Gagal tambah ke keranjang")
            }
        }
    }

    fun updateQty(cartId: Int, qty: Int) {
        if (qty <= 0) {
            removeCart(cartId)
            return
        }

        viewModelScope.launch {
            try {
                val res = repositoryBeanMate.updateQty(cartId, qty)

                val sukses = try {
                    res.isSuccessful
                } catch (_: Exception) {
                    true
                }

                if (sukses) {
                    loadCart()
                } else {
                    uiEvent = UiEventCart.Message("Gagal update qty")
                }
            } catch (e: Exception) {
                uiEvent = UiEventCart.Message(e.message ?: "Gagal update qty")
            }
        }
    }

    fun removeCart(cartId: Int) {
        viewModelScope.launch {
            try {
                val res = repositoryBeanMate.removeCart(cartId)

                val sukses = try {
                    res.isSuccessful
                } catch (_: Exception) {
                    true
                }

                if (sukses) {
                    loadCart()
                    uiEvent = UiEventCart.Message("Item dihapus dari keranjang")
                } else {
                    uiEvent = UiEventCart.Message("Gagal hapus item")
                }
            } catch (e: Exception) {
                uiEvent = UiEventCart.Message(e.message ?: "Gagal hapus item")
            }
        }
    }
}
