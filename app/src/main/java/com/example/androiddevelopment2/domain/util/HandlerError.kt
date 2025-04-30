package com.example.androiddevelopment2.domain.util

import com.example.androiddevelopment2.domain.exception.ForbiddenException
import javax.inject.Inject

//interface ErrorHandler {
//    fun handleError(code: Int): Exception
//    fun handleException(e: Exception): Exception
//}

class ErrorHandler @Inject constructor(){
    fun handleHttpException(code: Int): Exception {
        return when (code) {
            403 -> ForbiddenException()
            else -> kotlin.Exception("Server error")
        }
    }
}