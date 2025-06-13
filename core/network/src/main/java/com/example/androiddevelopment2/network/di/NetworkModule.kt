package com.example.androiddevelopment2.network.di

import com.example.androiddevelopment2.network.RecipeApi
import com.example.androiddevelopment2.network.interceptor.AppIdInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.androiddevelopment2.network.BuildConfig.RECIPE_API_URL


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AppIdInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipesApi(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ): RecipeApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(RECIPE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
        return retrofit.create(RecipeApi::class.java)
    }
}
