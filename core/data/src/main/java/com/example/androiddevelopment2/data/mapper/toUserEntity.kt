package com.example.androiddevelopment2.data.mapper

import com.example.androiddevelopment2.data.local.database.entities.UserEntity

fun toUserEntity(phone: String, password: String): UserEntity {
    return UserEntity(
        phone = phone,
        password = password
    )
}
