package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phone: String, password: String): Boolean {
        return userRepository.login(phone, password)
    }
}
