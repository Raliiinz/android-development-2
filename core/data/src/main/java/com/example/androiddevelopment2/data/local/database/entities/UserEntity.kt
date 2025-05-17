package com.example.androiddevelopment2.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "phone")
    val phone: String,
    @ColumnInfo(name = "password")
    val password: String
)
