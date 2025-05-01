package com.example.androiddevelopment2.di.module

import com.example.androiddevelopment2.data.repository.RecipeDetailsRepositoryImpl
import com.example.androiddevelopment2.data.repository.RecipesRepositoryImpl
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderRepositoryModule {
    @Binds
    @Singleton
    fun bindRecipesRepositoryToImpl(impl: RecipesRepositoryImpl): RecipesRepository

    @Binds
    @Singleton
    fun bindRecipeDetailsRepositoryToImpl(impl: RecipeDetailsRepositoryImpl): RecipeDetailsRepository
}
