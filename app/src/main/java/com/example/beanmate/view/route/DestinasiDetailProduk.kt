package com.example.beanmate2.view.route

import com.example.beanmate2.R

object DestinasiDetailProduk : DestinasiNavigasi {
    override val route = "detail_produk"
    override val titleRes = R.string.app_name

    const val routeWithArgs = "detail_produk/{itemId}"
    const val itemIdArg = "itemId"
}
