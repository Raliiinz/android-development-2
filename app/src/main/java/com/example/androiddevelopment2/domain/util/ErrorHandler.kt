package com.example.androiddevelopment2.domain.util

import com.example.androiddevelopment2.domain.exception.BadRequestException
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.exception.NotFoundException
import com.example.androiddevelopment2.domain.exception.ServerException
import com.example.androiddevelopment2.domain.exception.UnauthorizedException
import javax.inject.Inject

class ErrorHandler @Inject constructor() {

    companion object {
        private const val HTTP_BAD_REQUEST = 400
        private const val HTTP_UNAUTHORIZED = 401
        private const val HTTP_FORBIDDEN = 403
        private const val HTTP_NOT_FOUND = 404
        private const val HTTP_SERVER_ERROR = 500
    }

    fun handleHttpException(code: Int): Exception {
        return when (code) {
            HTTP_BAD_REQUEST -> BadRequestException()
            HTTP_UNAUTHORIZED -> UnauthorizedException()
            HTTP_FORBIDDEN -> ForbiddenException()
            HTTP_NOT_FOUND -> NotFoundException()
            HTTP_SERVER_ERROR -> ServerException()
            else -> Exception("Ошибка: $code")
        }
    }
}
