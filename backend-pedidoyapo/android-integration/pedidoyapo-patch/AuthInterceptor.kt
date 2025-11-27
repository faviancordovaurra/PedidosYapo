package com.pedidosyapo.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

import android.content.Context

class AuthInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        // Example: get token from shared prefs or secure storage
        val token = TokenStorage.getToken(context) // Implement TokenStorage class or use any secure store
        if (!token.isNullOrBlank()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
