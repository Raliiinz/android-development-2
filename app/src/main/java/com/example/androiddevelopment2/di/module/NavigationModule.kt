package com.example.androiddevelopment2.di.module

import com.example.androiddevelopment2.presentation.base.navigation.Nav
import com.example.androiddevelopment2.presentation.base.navigation.impl.NavImpl
import com.example.androiddevelopment2.presentation.base.navigation.NavMain
import com.example.androiddevelopment2.presentation.base.navigation.impl.NavMainImpl
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
