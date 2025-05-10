package com.example.androiddevelopment2.authorization

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddevelopment2.authorization.databinding.FragmentAuthorizationBinding
import com.example.androiddevelopment2.authorization.state.AuthorizationError
import com.example.androiddevelopment2.authorization.state.AuthorizationEvent
import com.example.androiddevelopment2.authorization.state.AuthorizationUiState
import com.example.androiddevelopment2.base.R
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.androiddevelopment2.utils.hideKeyboard
import com.example.androiddevelopment2.utils.setupPasswordToggle
import com.example.androiddevelopment2.utils.setupValidationOfNull
import com.example.base_feature.BaseFragment
import com.example.androiddevelopment2.authorization.R as authR

@AndroidEntryPoint
class AuthorizationFragment : BaseFragment(authR.layout.fragment_authorization) {
    private val viewBinding: FragmentAuthorizationBinding by viewBinding(FragmentAuthorizationBinding::bind)
    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
        setupRealTimeValidation()
        setupPasswordToggle()
    }

    private fun setupObservers() {
        viewModel.uiState
            .onEach { state ->
                when (state) {
                    AuthorizationUiState.Loading -> showProgress()
                    AuthorizationUiState.Idle -> hideProgress()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.events
            .onEach { event ->
                when (event) {
                    is AuthorizationEvent.ShowError -> {
                        handleErrors(event.message)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleErrors(error: AuthorizationError) {
        val message = when (error) {
            AuthorizationError.InvalidCredentials -> R.string.error_invalid_credentials
                AuthorizationError.Unknown -> R.string.error_unknown
        }
        showToast(getString(message))
    }

    private fun setupListeners() {
        with(viewBinding) {
            etPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin()
                    return@setOnEditorActionListener true
                }
                false
            }
            tvRegisterLink.setOnClickListener {
                hideKeyboard()
                viewModel.navigateToRegistration()
            }
            btnLogin.setOnClickListener {
                attemptLogin()
            }
        }
    }

    private fun attemptLogin() {
        val phone = viewBinding.etPhone.text.toString()
        val password = viewBinding.etPassword.text.toString().trim()

        val isPhoneValid = validateField(
            value = phone,
            errorMessage = getString(R.string.error_phone_empty),
            errorTarget = viewBinding.textInputLayoutPhone
        )

        val isPasswordValid = validateField(
            value = password,
            errorMessage = getString(R.string.error_password_empty),
            errorTarget = viewBinding.textInputLayoutPassword
        )

        if (isPhoneValid && isPasswordValid) {
            viewModel.login(phone, password)
        }
    }

    private fun validateField(
        value: String,
        errorMessage: String,
        errorTarget: TextInputLayout?
    ): Boolean {
        return if (value.isEmpty()) {
            errorTarget?.error = errorMessage
            false
        } else {
            errorTarget?.error = null
            true
        }
    }

    private fun setupRealTimeValidation() {
        viewBinding.apply {
            etPhone.setupValidationOfNull(
                errorMessage = getString(R.string.error_phone_empty),
                errorTarget = textInputLayoutPhone
            )

            etPassword.setupValidationOfNull(
                errorMessage = getString(R.string.error_password_empty),
                errorTarget = textInputLayoutPassword
            )
        }
    }

    private fun setupPasswordToggle() {
        viewBinding.textInputLayoutPassword.setupPasswordToggle()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
