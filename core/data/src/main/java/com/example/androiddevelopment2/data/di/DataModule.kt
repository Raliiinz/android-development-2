package com.example.androiddevelopment2.data.di

import com.example.androiddevelopment2.data.local.cache.RecipeCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideRecipeCache(): RecipeCache = RecipeCache()
}