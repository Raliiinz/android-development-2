package com.example.androiddevelopment2.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.usecase.RegisterUseCase
import com.example.androiddevelopment2.navigation.NavRegistration
import com.example.androiddevelopment2.registration.state.FieldState
import com.example.androiddevelopment2.registration.state.RegistrationError
import com.example.androiddevelopment2.registration.state.RegistrationEvent
import com.example.androiddevelopment2.registration.state.RegistrationUiState
import com.example.androiddevelopment2.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val navMain: NavRegistration
) : ViewModel() {

    private var phoneTouched = false
    private var passwordTouched = false
    private var confirmPasswordTouched = false
    private var submitAttempted = false

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: StateFlow<RegistrationUiState> = _uiState

    private val _phoneState = MutableStateFlow(FieldState.empty())
    val phoneState: StateFlow<FieldState> = _phoneState

    private val _passwordState = MutableStateFlow(FieldState.empty())
    val passwordState: StateFlow<FieldState> = _passwordState

    private val _confirmPasswordState = MutableStateFlow(FieldState.empty())
    val confirmPasswordState: StateFlow<FieldState> = _confirmPasswordState

    private val _events = MutableSharedFlow<RegistrationEvent>()
    val events: SharedFlow<RegistrationEvent> = _events

    fun onPhoneChanged(rawPhone: String) {
        phoneTouched = true
        val isValid = ValidationUtils.isValidPhone(rawPhone)

        _phoneState.value = FieldState(
            value = rawPhone,
            isValid = isValid,
            shouldShowError = (phoneTouched || submitAttempted) && !isValid
        )
    }

    fun onPasswordChanged(password: String) {
        passwordTouched = true
        val isValid = ValidationUtils.isValidPassword(password)
        _passwordState.value = FieldState(
            value = password,
            isValid = isValid,
            shouldShowError = (passwordTouched || submitAttempted) && !isValid
        )
    }

    fun onConfirmPasswordChanged(password: String) {
        confirmPasswordTouched = true
        _confirmPasswordState.value = FieldState(
            value = password,
            isValid = false,
            shouldShowError = (confirmPasswordTouched || submitAttempted)
        )
        validatePasswordMatch(password)
    }

    private fun validatePasswordMatch(password: String) {
        val passwordsMatch = _passwordState.value.value == _confirmPasswordState.value.value
        _confirmPasswordState.update {
            it.copy(
                value = password,
                isValid = passwordsMatch,
                shouldShowError = (confirmPasswordTouched || submitAttempted) && !passwordsMatch && it.value.isNotEmpty()
            )
        }
    }

    fun register() {
        submitAttempted = true

        _phoneState.update {
            it.copy(shouldShowError = !it.isValid)
        }
        _passwordState.update {
            it.copy(shouldShowError = !it.isValid)
        }
        _confirmPasswordState.update {
            it.copy(shouldShowError = !it.isValid)
        }

        if (!_phoneState.value.isValid ||
            !_passwordState.value.isValid ||
            !_confirmPasswordState.value.isValid) {
            return
        }

        viewModelScope.launch {
            _uiState.update { RegistrationUiState.Loading }
            delay(2000)

            runCatching {
                registerUseCase(_phoneState.value.value, _passwordState.value.value)
            }.onSuccess { isSuccess ->
                if (isSuccess) {
                    navigateToAuthorization()
                } else {
                    _events.emit(RegistrationEvent.ShowError(RegistrationError.UserAlreadyExists))
                }
            }.onFailure {
                _events.emit(RegistrationEvent.ShowError(RegistrationError.Unknown))
            }.also {
                _uiState.update { RegistrationUiState.Idle }
            }
        }
    }

    fun navigateToAuthorization() {
        viewModelScope.launch {
            navMain.goToAuthPage()
        }
    }
}
