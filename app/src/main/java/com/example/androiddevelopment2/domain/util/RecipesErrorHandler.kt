package com.example.androiddevelopment2.domain.util

import android.net.http.NetworkException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.androiddevelopment2.domain.exception.BadRequestException
import com.example.androiddevelopment2.domain.exception.ForbiddenException
import com.example.androiddevelopment2.domain.exception.InvalidApiKeyException
import com.example.androiddevelopment2.domain.exception.RateLimitExceededException
import com.example.androiddevelopment2.domain.exception.RecipeNotFoundException
import com.example.androiddevelopment2.domain.exception.ServerErrorException
import com.example.androiddevelopment2.domain.exception.UnknownApiException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RecipesErrorHandler @Inject constructor() : ErrorHandler {
    override fun handleError(code: Int): Exception {
        return when (code) {
            400 -> BadRequestException()
            401 -> InvalidApiKeyException()
            403 -> ForbiddenException()
            404 -> RecipeNotFoundException()
            429 -> RateLimitExceededException()
            in 500..599 -> ServerErrorException()
            else -> UnknownApiException("API error: $code")
        }
    }

    override fun handleException(e: Exception): Exception {
        TODO("Not yet implemented")
    }

//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    override fun handleException(e: Exception): Exception {
//        return when (e) {
//            is IOException -> NetworkException()
//            is SocketTimeoutException -> NetworkException("Request timeout")
//            is UnknownHostException -> NetworkException("No internet connection")
//            else -> UnknownApiException(e.message ?: "Unknown error")
//        }
//    }
}