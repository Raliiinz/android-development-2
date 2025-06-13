package com.example.androiddevelopment2.search.state

sealed class SearchErrorEvent {

    data class ValidationError(val reason: ValidationFailureReason) : SearchErrorEvent()

    data class ServerError(val reason: ServerFailureReason) : SearchErrorEvent()

    object ClearValidationError : SearchErrorEvent()

    enum class ValidationFailureReason {
        EmptyInput,
        MinLength,
        InvalidFormat
    }

    enum class ServerFailureReason {
        Unauthorized,
        Forbidden,
        NotFound,
        BadRequest,
        Server,
        Network,
        Unknown
    }

    sealed class ValidationResult {
        data class Valid(val query: String) : ValidationResult()
        data class Invalid(val reason: ValidationFailureReason) : ValidationResult()
    }
}
