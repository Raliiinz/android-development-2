package com.example.androiddevelopment2.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevelopment2.domain.usecase.RegisterUseCase
import com.example.androiddevelopment2.navigation.NavRegistration
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

    private val _uiState = MutableStateFlow<RegistrationUiState>(RegistrationUiState.Idle)
    val uiState: StateFlow<RegistrationUiState> = _uiState

    private val _events = MutableSharedFlow<RegistrationEvent>()
    val events: SharedFlow<RegistrationEvent> = _events

    fun register(phone: String, password: String) {
        viewModelScope.launch {
            _uiState.update { RegistrationUiState.Loading }
            delay(2000)
            try {
                val isSuccess = registerUseCase(phone, password)
                if (isSuccess) {
                    navigateToAuthorization()
                }
            } catch (e: Exception) {
                val error = when (e.message) {
                    "User with this phone number already exists" ->
                        RegistrationError.UserAlreadyExists
                    else -> RegistrationError.Unknown
                }
                _events.emit(RegistrationEvent.ShowError(error))
            } finally {
                _uiState.update { RegistrationUiState.Idle }
            }
        }
    }

    fun navigateToAuthorization() {
        viewModelScope.launch {
            navMain.goToAuthPage()
        }
    }

    sealed class RegistrationUiState {
        object Idle : RegistrationUiState()
        object Loading : RegistrationUiState()
        object Success : RegistrationUiState()
    }

    sealed class RegistrationEvent {
        data class ShowError(val error: RegistrationError) : RegistrationEvent()
    }

    enum class RegistrationError {
        UserAlreadyExists,
        Unknown
    }
}
