package com.example.androiddevelopment2.data.di

import android.content.Context
import androidx.room.Room
import com.example.androiddevelopment2.data.local.cache.RecipeCache
import com.example.androiddevelopment2.data.local.database.AppDatabase
import com.example.androiddevelopment2.data.local.database.dao.UserDao
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
    private val databaseName = "app_database"

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

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            databaseName
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}
