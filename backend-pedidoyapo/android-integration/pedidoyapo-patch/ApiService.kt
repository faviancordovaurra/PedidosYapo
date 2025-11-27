package com.pedidosyapo.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/productos")
    fun getProductos(): Call<List<Producto>>

    @Multipart
    @POST("api/productos/{id}/upload")
    fun uploadImage(@Path("id") id: Long, @Part file: MultipartBody.Part): Call<Void>
}
