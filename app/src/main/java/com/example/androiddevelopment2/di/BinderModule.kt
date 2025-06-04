package com.example.androiddevelopment2.di

import com.example.androiddevelopment2.FcmRepositoryImpl
import com.example.androiddevelopment2.domain.firebase.fcm.repository.FcmRepository
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
    fun bindFcmRepositoryToImpl(impl: FcmRepositoryImpl): FcmRepository
}