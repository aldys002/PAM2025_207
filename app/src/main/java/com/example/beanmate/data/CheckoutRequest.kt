package com.example.beanmate2.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    @SerialName("session_id")
    val sessionId: String,

    @SerialName("customer_name")
    val customerName: String,

    @SerialName("customer_address")
    val customerAddress: String,

    @SerialName("customer_phone")
    val customerPhone: String
)
