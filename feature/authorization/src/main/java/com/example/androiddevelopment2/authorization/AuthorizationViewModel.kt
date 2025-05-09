package com.example.androiddevelopment2.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val navAuth: NavAuthorization
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthorizationUiState>(AuthorizationUiState.Idle)
    val uiState: StateFlow<AuthorizationUiState> = _uiState

    private val _events = MutableSharedFlow<AuthorizationEvent>()
    val events: SharedFlow<AuthorizationEvent> = _events

    val authState: StateFlow<AuthState?> = userPreferencesRepository.authState
        .map { (isLoggedIn, phone) -> AuthState(isLoggedIn, phone) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _uiState.update { AuthorizationUiState.Idle }
            try {
                val isSuccess = loginUseCase(phone, password)
                if (isSuccess) {
                    userPreferencesRepository.saveLoginState(true, phone)
                    navAuth.goToMainPage(phone)
                } else {
                    _events.emit(AuthorizationEvent.ShowError("Неверный номер телефона или пароль"))
                }
            } catch (e: Exception) {
                _events.emit(AuthorizationEvent.ShowError(e.message ?: "Ошибка"))
            } finally {
                _uiState.update { AuthorizationUiState.Idle }
            }
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

    sealed class AuthorizationUiState {
        object Idle : AuthorizationUiState()
    }

    sealed class AuthorizationEvent {
        data class ShowError(val message: String) : AuthorizationEvent()
    }

    data class AuthState(
        val isLoggedIn: Boolean = false,
        val userPhone: String? = null
    )
}