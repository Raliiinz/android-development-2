package com.example.androiddevelopment2.data.local.repository

import com.example.androiddevelopment2.data.local.database.dao.UserDao
import com.example.androiddevelopment2.data.local.database.entities.UserEntity
import com.example.androiddevelopment2.data.mapper.toUserEntity
import com.example.androiddevelopment2.domain.repository.UserRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun registerUser(phone: String, password: String) {
        val userEntity = toUserEntity(phone, password)
        userDao.insertUser(userEntity)
    }

    override suspend fun isUserExists(phone: String): Boolean {
        return userDao.getUserByPhone(phone) != null
    }

    override suspend fun login(phone: String, password: String): Boolean {
        val user = userDao.getUserByPhone(phone)
        return user?.password == password
    }
}
