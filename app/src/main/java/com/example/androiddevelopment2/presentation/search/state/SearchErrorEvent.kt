package com.example.androiddevelopment2.presentation.search.state

//sealed class SearchErrorEvent {
//    data class Error(val reason: FailureReason) : SearchErrorEvent()
//
//    sealed interface FailureReason {
//        data object BadRequest : FailureReason
//        data object Unauthorized : FailureReason
//        data object Forbidden : FailureReason
//        data object NotFound : FailureReason
//        data object Server : FailureReason
//        data object Network : FailureReason
//        data object Unknown : FailureReason
//    }
//}

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
}