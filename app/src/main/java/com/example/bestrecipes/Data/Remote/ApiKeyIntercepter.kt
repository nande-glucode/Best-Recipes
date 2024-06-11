package com.example.bestrecipes.Data.Remote

import com.example.bestrecipes.Utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyIntercepter : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("x-api-key", API_KEY)
            .build()
        return chain.proceed(request)
    }
}