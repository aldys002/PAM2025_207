package com.example.beanmate2.repositori

import com.example.beanmate2.apiservice.ServiceApiBeanMate
import com.example.beanmate2.data.*
import retrofit2.Response

interface RepositoryBeanMate {

    suspend fun getProduk(): List<DataBeanMate>
    suspend fun postProduk(data: DataBeanMate): Response<ApiMessage>
    suspend fun getSatuProduk(id: Int): DataBeanMate
    suspend fun editSatuProduk(id: Int, data: DataBeanMate): Response<ApiMessage>
    suspend fun hapusSatuProduk(id: Int): Response<ApiMessage>

    suspend fun getUploads(): UploadsListResponse


    suspend fun loginAdmin(username: String, password: String): ApiLoginResponse

    suspend fun getCart(sessionId: String): List<DataCartItem>
    suspend fun addCart(sessionId: String, productId: Int, qty: Int = 1): Response<ApiMessage>
    suspend fun updateQty(cartId: Int, qty: Int): Response<ApiMessage>
    suspend fun removeCart(cartId: Int): Response<ApiMessage>
    suspend fun clearCart(sessionId: String): Response<ApiMessage>

    suspend fun checkout(
        sessionId: String,
        customerName: String,
        customerAddress: String,
        customerPhone: String
    ): Response<DataCheckoutResponse>

    suspend fun getOrders(): List<DataOrder>
    suspend fun getOrderDetail(orderId: Int): DataOrderDetailResponse
}

class JaringanRepositoryBeanMate(
    private val serviceApi: ServiceApiBeanMate
) : RepositoryBeanMate {

    override suspend fun getProduk(): List<DataBeanMate> =
        serviceApi.getProduk()

    override suspend fun postProduk(data: DataBeanMate): Response<ApiMessage> =
        serviceApi.postProduk(data)

    override suspend fun getSatuProduk(id: Int): DataBeanMate =
        serviceApi.getSatuProduk(id)

    override suspend fun editSatuProduk(id: Int, data: DataBeanMate): Response<ApiMessage> =
        serviceApi.editSatuProduk(id, data)

    override suspend fun hapusSatuProduk(id: Int): Response<ApiMessage> =
        serviceApi.hapusSatuProduk(id)

    override suspend fun getUploads(): UploadsListResponse =
        serviceApi.getUploads()

    override suspend fun loginAdmin(username: String, password: String): ApiLoginResponse =
        serviceApi.loginAdmin(LoginRequest(username, password))

    override suspend fun getCart(sessionId: String): List<DataCartItem> =
        serviceApi.getCart(sessionId)

    override suspend fun addCart(
        sessionId: String,
        productId: Int,
        qty: Int
    ): Response<ApiMessage> =
        serviceApi.addCart(AddCartRequest(sessionId, productId, qty))

    override suspend fun updateQty(cartId: Int, qty: Int): Response<ApiMessage> =
        serviceApi.updateQty(cartId, UpdateQtyRequest(qty))

    override suspend fun removeCart(cartId: Int): Response<ApiMessage> =
        serviceApi.removeCart(cartId)

    override suspend fun clearCart(sessionId: String): Response<ApiMessage> =
        serviceApi.clearCart(sessionId)

    override suspend fun checkout(
        sessionId: String,
        customerName: String,
        customerAddress: String,
        customerPhone: String
    ): Response<DataCheckoutResponse> =
        serviceApi.checkout(
            CheckoutRequest(
                sessionId = sessionId,
                customerName = customerName,
                customerAddress = customerAddress,
                customerPhone = customerPhone
            )
        )

    override suspend fun getOrders(): List<DataOrder> =
        serviceApi.getOrders().data

    override suspend fun getOrderDetail(orderId: Int): DataOrderDetailResponse =
        serviceApi.getOrderDetail(orderId)
}
