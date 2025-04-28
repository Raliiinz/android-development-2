package com.example.androiddevelopment2.domain.util

interface ErrorHandler {
    fun handleError(code: Int): Exception
    fun handleException(e: Exception): Exception
}