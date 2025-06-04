package com.example.androiddevelopment2.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.authorization.state.AuthState
import com.example.androiddevelopment2.authorization.state.AuthorizationError
import com.example.androiddevelopment2.authorization.state.AuthorizationEvent
import com.example.androiddevelopment2.authorization.state.AuthorizationUiState
import com.example.androiddevelopment2.authorization.state.FieldState
import com.example.androiddevelopment2.domain.firebase.crashlytics.CrashlyticsTracker
import com.example.androiddevelopment2.domain.repository.UserPreferencesRepository
import com.example.androiddevelopment2.domain.usecase.LoginUseCase
import com.example.androiddevelopment2.navigation.NavAuthorization
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val loginUseCase: LoginUseCase,
    private val navAuth: NavAuthorization,
    private val crashlyticsTracker: CrashlyticsTracker
) : ViewModel() {

    private var phoneTouched = false
    private var passwordTouched = false
    private var submitAttempted = false

    private val _uiState = MutableStateFlow<AuthorizationUiState>(AuthorizationUiState.Idle)
    val uiState: StateFlow<AuthorizationUiState> = _uiState

    private val _phoneState = MutableStateFlow(FieldState.empty())
    val phoneState: StateFlow<FieldState> = _phoneState

    private val _passwordState = MutableStateFlow(FieldState.empty())
    val passwordState: StateFlow<FieldState> = _passwordState

    private val _events = MutableSharedFlow<AuthorizationEvent>()
    val events: SharedFlow<AuthorizationEvent> = _events

    val authState: StateFlow<AuthState?> = userPreferencesRepository.authState
        .map { (isLoggedIn, phone) -> AuthState(isLoggedIn, phone) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun onPhoneChanged(rawPhone: String) {
        phoneTouched = true
        val isValid = rawPhone.isNotBlank()

        _phoneState.value = FieldState(
            value = rawPhone,
            isValid = isValid,
            shouldShowError = (phoneTouched || submitAttempted) && !isValid
        )
    }

    fun onPasswordChanged(password: String) {
        passwordTouched = true
        val isValid = password.isNotBlank()

        _passwordState.value = FieldState(
            value = password,
            isValid = isValid,
            shouldShowError = (passwordTouched || submitAttempted) && !isValid
        )
    }

    fun login() {

        submitAttempted = true

        crashlyticsTracker.logEvent("Login attempt started")
        crashlyticsTracker.setCustomKey("login_state", "started")

        _phoneState.update {
            it.copy(shouldShowError = !it.isValid)
        }
        _passwordState.update {
            it.copy(shouldShowError = !it.isValid)
        }

        if (!_phoneState.value.isValid || !_passwordState.value.isValid) {
            crashlyticsTracker.logEvent("Validation failed")
            crashlyticsTracker.setCustomKey("phone_valid", _phoneState.value.isValid)
            crashlyticsTracker.setCustomKey("password_valid", _passwordState.value.isValid)
            return
        }

        viewModelScope.launch {
            _uiState.update { AuthorizationUiState.Loading }
            crashlyticsTracker.setCustomKey("login_state", "loading")

            runCatching {
                crashlyticsTracker.logEvent("Trying to login")
                crashlyticsTracker.setCustomKey("user_phone",_phoneState.value.value.toString())

                loginUseCase(_phoneState.value.value, _passwordState.value.value)
            }.onSuccess { isSuccess ->
                if (isSuccess) {
                    crashlyticsTracker.logEvent("Login success")
                    crashlyticsTracker.setCustomKey("login_state", "success")

                    userPreferencesRepository.saveLoginState(true, _phoneState.value.value)
                    navAuth.goToMainPage(_phoneState.value.value)
                } else {
                    crashlyticsTracker.logEvent("Invalid credentials")
                    crashlyticsTracker.setCustomKey("login_state", "invalid_credentials")

//                    triggerTestCrash()
                    _events.emit(AuthorizationEvent.ShowError(AuthorizationError.InvalidCredentials))
                }
            }.onFailure { e ->
                crashlyticsTracker.logError(e)
                crashlyticsTracker.setCustomKey("login_state", "failure")
                crashlyticsTracker.setCustomKey("error_message", e.message ?: "unknown")
                _events.emit(AuthorizationEvent.ShowError(AuthorizationError.Unknown))
            }.also {
                _uiState.update { AuthorizationUiState.Idle }
                crashlyticsTracker.setCustomKey("login_state", "idle")
            }
        }
    }

    fun triggerTestCrash() {
        viewModelScope.launch {
            crashlyticsTracker.logEvent("Triggering test crash")
            throw RuntimeException("Тестовый краш для Firebase Crashlytics!")
        }
    }

    fun navigateToRegistration() {
        viewModelScope.launch {
            navAuth.goToRegisterPage()
        }
    }


    fun navigateBasedOnAuthState() {
        viewModelScope.launch {
            authState
                .filterNotNull()
                .first()
                .let { state ->
                    if (!state.isLoggedIn) {
                        navAuth.goToAuthPage()
                    } else {
                        requireNotNull(state.userPhone)
                        navAuth.goToMainPage(state.userPhone)
                    }
                }
        }
    }
}