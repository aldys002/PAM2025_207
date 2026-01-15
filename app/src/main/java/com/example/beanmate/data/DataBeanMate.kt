package com.example.beanmate2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataBeanMate(
    val id: Int,
    val name: String,
    val price: Int,
    val description: String,
    val image: String
)

data class UIStateBeanMate(
    val detailBeanMate: DetailBeanMate = DetailBeanMate(),
    val isEntryValid: Boolean = false
)

data class DetailBeanMate(
    val id: Int = 0,
    val name: String = "",
    val price: Int = 0,
    val description: String = "",
    val image: String = ""
)

fun DetailBeanMate.toDataBeanMate(): DataBeanMate = DataBeanMate(
    id = id,
    name = name,
    price = price,
    description = description,
    image = image
)

fun DataBeanMate.toStateBeanMate(isEntryValid: Boolean = false): UIStateBeanMate = UIStateBeanMate(
    detailBeanMate = this.toDetailBeanMate(),
    isEntryValid = isEntryValid
)

fun DataBeanMate.toDetailBeanMate(): DetailBeanMate = DetailBeanMate(
    id = id,
    name = name,
    price = price,
    description = description,
    image = image
)

@Serializable
data class DataCartItem(
    @SerialName("cart_id") val cartId: Int,
    @SerialName("product_id") val productId: Int,
    val name: String,
    val price: Int,
    val qty: Int,
    val subtotal: Int,
    val image: String
)

data class UIStateCart(
    val detailCart: DetailCart = DetailCart(),
    val isEntryValid: Boolean = false
)

data class DetailCart(
    val cartId: Int = 0,
    val productId: Int = 0,
    val qty: Int = 1
)

fun DetailCart.toAddCartRequest(sessionId: String): AddCartRequest = AddCartRequest(
    sessionId = sessionId,
    productId = productId,
    qty = qty
)

fun DataCartItem.toDetailCart(): DetailCart = DetailCart(
    cartId = cartId,
    productId = productId,
    qty = qty
)

fun DataCartItem.toStateCart(isEntryValid: Boolean = false): UIStateCart = UIStateCart(
    detailCart = this.toDetailCart(),
    isEntryValid = isEntryValid
)

// request body untuk API (dipakai ServiceApiBeanMate)
@Serializable
data class AddCartRequest(
    @SerialName("session_id") val sessionId: String,
    @SerialName("product_id") val productId: Int,
    val qty: Int = 1
)

@Serializable
data class UpdateQtyRequest(
    val qty: Int
)
