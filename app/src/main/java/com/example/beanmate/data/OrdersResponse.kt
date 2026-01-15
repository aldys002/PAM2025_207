package com.example.beanmate2.data


import kotlinx.serialization.Serializable

@Serializable
data class OrdersResponse(
    val status: String,
    val message: String,
    val data: List<DataOrder>
)

