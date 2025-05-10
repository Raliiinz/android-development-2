package com.example.androiddevelopment2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androiddevelopment2.data.local.database.dao.UserDao
import com.example.androiddevelopment2.data.local.database.entities.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val DB_LOG_KEY = "AppDatabase"
    }
}
