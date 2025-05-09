package com.example.androiddevelopment2.data.di

import android.content.Context
import com.example.androiddevelopment2.data.local.cache.RecipeCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.androiddevelopment2.data.local.datasource.UserPreferencesDataSource

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideRecipeCache(): RecipeCache = RecipeCache()

    @Provides
    @Singleton
    fun provideUserPreferencesDataSource(
        @ApplicationContext context: Context
    ): UserPreferencesDataSource {
        return UserPreferencesDataSource(context)
    }
}