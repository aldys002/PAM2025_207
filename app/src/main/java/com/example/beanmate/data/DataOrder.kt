package com.example.beanmate2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DataOrder(
    val id: Int,
    @SerialName("session_id")
    val sessionId: String,
    val nama: String = "",
    val alamat: String = "",
    val telepon: String = "",
    @SerialName("total_price")
    val totalPrice: Int,
    val status: String,
    @SerialName("created_at")
    val createdAt: String
)


data class DetailOrder(
    val id: Int = 0,
    val sessionId: String = "",
    val totalPrice: Int = 0,
    val status: String = "",
    val createdAt: String = ""
)

@Serializable
data class DataCheckoutResponse(
    val status: String = "",
    val message: String = "",
    val data: CheckoutData? = null
)

@Serializable
data class CheckoutData(
    val order_id: Int = 0,
    val total_price: Int = 0
)

@Serializable
data class DataOrderDetailResponse(
    val status: String = "",
    val message: String = "",
    val data: OrderDetailData? = null
)

@Serializable
data class OrderDetailData(
    val order: DataOrder,
    val items: List<DataOrderItem>
)

@Serializable
data class DataOrderItem(
    @SerialName("product_id") val productId: Int,
    val name: String,
    @SerialName("price_each") val priceEach: Int,
    val qty: Int,
    val subtotal: Int,
    val image: String
)
