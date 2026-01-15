package com.example.beanmate2.apiservice

import com.example.beanmate2.data.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ServiceApiBeanMate {
    @GET("products/list.php")
    suspend fun getProduk(): List<DataBeanMate>

    @POST("products/create.php")
    suspend fun postProduk(@Body data: DataBeanMate): Response<ApiMessage>

    @GET("products/detail.php")
    suspend fun getSatuProduk(
        @Query("id") id: Int
    ): DataBeanMate

    @PUT("products/update.php")
    suspend fun editSatuProduk(
        @Query("id") id: Int,
        @Body data: DataBeanMate
    ): Response<ApiMessage>

    @DELETE("products/delete.php")
    suspend fun hapusSatuProduk(
        @Query("id") id: Int
    ): Response<ApiMessage>


    @GET("uploads/list.php")
    suspend fun getUploads(): UploadsListResponse

    @POST("auth/login_admin.php")
    suspend fun loginAdmin(
        @Body data: LoginRequest
    ): ApiLoginResponse


    @GET("cart/list.php")
    suspend fun getCart(
        @Query("session_id") sessionId: String
    ): List<DataCartItem>

    @POST("cart/add.php")
    suspend fun addCart(
        @Body data: AddCartRequest
    ): Response<ApiMessage>

    @PUT("cart/update_qty.php")
    suspend fun updateQty(
        @Query("id") cartId: Int,
        @Body data: UpdateQtyRequest
    ): Response<ApiMessage>

    @DELETE("cart/remove.php")
    suspend fun removeCart(
        @Query("id") cartId: Int
    ): Response<ApiMessage>

    @DELETE("cart/clear.php")
    suspend fun clearCart(
        @Query("session_id") sessionId: String
    ): Response<ApiMessage>


    @POST("orders/checkout.php")
    suspend fun checkout(
        @Body request: CheckoutRequest
    ): Response<DataCheckoutResponse>

    @GET("orders/list.php")
    suspend fun getOrders(): OrdersResponse

    @GET("orders/detail.php")
    suspend fun getOrderDetail(
        @Query("id") orderId: Int
    ): DataOrderDetailResponse
}
