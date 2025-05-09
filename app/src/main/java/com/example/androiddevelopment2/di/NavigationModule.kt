package com.example.androiddevelopment2.di

import com.example.androiddevelopment2.nav.NavImpl
import com.example.androiddevelopment2.nav.NavMainImpl
import com.example.androiddevelopment2.navigation.Nav
import com.example.androiddevelopment2.navigation.NavMain
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    @Singleton
    fun bindNavToImpl(impl: NavImpl): Nav

    @Binds
    @Singleton
    fun bindNavMainToImpl(impl: NavMainImpl): NavMain
}