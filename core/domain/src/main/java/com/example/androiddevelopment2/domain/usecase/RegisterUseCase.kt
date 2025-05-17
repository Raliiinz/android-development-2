package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.di.qualifies.IoDispatchers
import com.example.androiddevelopment2.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val userRepository: UserRepository,
    @IoDispatchers private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(phone: String, password: String): Boolean {
        return withContext(dispatcher) {
            if (userRepository.isUserExists(phone)) {
                false
            } else {
                userRepository.registerUser(phone, password)
                true
            }
        }
    }
}
