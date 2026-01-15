package com.example.beanmate2.view.route

import com.example.beanmate2.R

object DestinasiOrderDetail : DestinasiNavigasi {
    override val route = "order_detail"
    override val titleRes = R.string.app_name

    const val routeWithArgs = "order_detail/{itemId}"
    const val itemIdArg = "itemId"
}
