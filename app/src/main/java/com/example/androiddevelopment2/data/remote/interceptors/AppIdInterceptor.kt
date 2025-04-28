package com.example.androiddevelopment2.data.remote.interceptors

import com.example.androiddevelopment2.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AppIdInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.recipeApiKey)
        val request = chain.request().newBuilder().url(url.build())

        return chain.proceed(request.build())
    }
}