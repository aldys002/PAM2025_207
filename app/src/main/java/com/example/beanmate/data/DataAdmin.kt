package com.example.beanmate2.data

import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    val status: String = "",
    val message: String = "",
    val data: String? = null
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class ApiLoginResponse(
    val status: String = "",
    val message: String = "",
    val data: AdminData? = null
)

@Serializable
data class AdminData(
    val id: Int = 0,
    val username: String = ""
)

data class UIStateAdmin(
    val detailAdmin: DetailAdmin = DetailAdmin(),
    val isEntryValid: Boolean = false
)

data class DetailAdmin(
    val username: String = "",
    val password: String = ""
)

