package com.example.beanmate2.view.viewmodel

import com.example.beanmate2.data.DataBeanMate

sealed class StatusUiProduk {
    object Loading : StatusUiProduk()
    data class Success(val produk: List<DataBeanMate>) : StatusUiProduk()
    object Error : StatusUiProduk()
}
