package com.example.androiddevelopment2.data.di

import com.example.androiddevelopment2.data.firebase.crashlytics.FirebaseCrashlyticsTracker
import com.example.androiddevelopment2.data.firebase.remoteconfig.FeatureFlagsRepositoryImpl
import com.example.androiddevelopment2.data.firebase.remoteconfig.FirebaseRemoteConfigDataSourceImpl
import com.example.androiddevelopment2.data.remote.repository.RecipeDetailsRepositoryImpl
import com.example.androiddevelopment2.data.remote.repository.RecipesRepositoryImpl
import com.example.androiddevelopment2.data.local.repository.UserPreferencesRepositoryImpl
import com.example.androiddevelopment2.data.local.repository.UserRepositoryImpl
import com.example.androiddevelopment2.domain.firebase.crashlytics.CrashlyticsTracker
import com.example.androiddevelopment2.domain.firebase.remoteconfig.FeatureFlagsRepository
import com.example.androiddevelopment2.domain.firebase.remoteconfig.FirebaseRemoteConfigDataSource
import com.example.androiddevelopment2.domain.repository.RecipeDetailsRepository
import com.example.androiddevelopment2.domain.repository.RecipesRepository
import com.example.androiddevelopment2.domain.repository.UserPreferencesRepository
import com.example.androiddevelopment2.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataBinderModule {
    @Binds
    @Singleton
    fun bindRecipesRepositoryToImpl(impl: RecipesRepositoryImpl): RecipesRepository

    @Binds
    @Singleton
    fun bindRecipeDetailsRepositoryToImpl(impl: RecipeDetailsRepositoryImpl): RecipeDetailsRepository

    @Binds
    @Singleton
    fun bindUserPrefRepositoryToImpl(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Binds
    @Singleton
    fun bindUserRepositoryToImpl(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    fun bindCrashlyticsToImpl(impl: FirebaseCrashlyticsTracker): CrashlyticsTracker

    @Binds
    @Singleton
    fun bindRemoteConfigDataSourceToImpl(impl: FirebaseRemoteConfigDataSourceImpl): FirebaseRemoteConfigDataSource

    @Binds
    @Singleton
    fun bindFeatureFlagsRepositoryToImpl(impl: FeatureFlagsRepositoryImpl): FeatureFlagsRepository
}
