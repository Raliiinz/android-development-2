package com.example.androiddevelopment2.domain.util

import com.example.androiddevelopment2.domain.exception.*
import javax.inject.Inject

class ErrorHandler @Inject constructor() {
    fun handleHttpException(code: Int): Exception {
        return when (code) {
            400 -> BadRequestException("Неверный запрос")
            401 -> UnauthorizedException("Пользователь не авторизован")
            403 -> ForbiddenException("Доступ запрещен")
            404 -> NotFoundException("Данные не найдены")
            500 -> ServerException("Ошибка сервера")
            else -> Exception("Ошибка: $code")
        }
    }
}
