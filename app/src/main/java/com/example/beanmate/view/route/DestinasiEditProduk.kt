package com.example.beanmate2.view.route

import com.example.beanmate2.R

object DestinasiEditProduk : DestinasiNavigasi {
    override val route = "edit_produk"
    override val titleRes = R.string.form_title_edit

    const val itemIdArg = "id"
    val routeWithArgs = "$route/{$itemIdArg}"
}
