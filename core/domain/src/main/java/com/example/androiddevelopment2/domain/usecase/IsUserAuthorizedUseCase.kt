package com.example.androiddevelopment2.domain.usecase

import com.example.androiddevelopment2.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class IsUserAuthorizedUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        val (isLoggedIn, _) = userPreferencesRepository.authState.first()
        return isLoggedIn
    }
}