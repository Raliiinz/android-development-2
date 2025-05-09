package com.example.androiddevelopment2.network.interceptor

import okhttp3.Interceptor
import javax.inject.Inject
import okhttp3.Response
import com.example.androiddevelopment2.network.BuildConfig

class AppIdInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.recipeApiKey)
        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}