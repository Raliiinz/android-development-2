package com.example.androiddevelopment2.registration

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.registration.databinding.FragmentRegistrationBinding
import com.example.androiddevelopment2.registration.state.RegistrationError
import com.example.androiddevelopment2.registration.state.RegistrationEvent
import com.example.androiddevelopment2.registration.state.RegistrationUiState
import com.example.androiddevelopment2.utils.hideKeyboard
import com.example.androiddevelopment2.utils.setupPasswordToggle
import com.example.base_feature.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.example.androiddevelopment2.registration.R as registerR


@AndroidEntryPoint
class RegistrationFragment : BaseFragment(registerR.layout.fragment_registration) {
    private val viewBinding: FragmentRegistrationBinding by viewBinding(FragmentRegistrationBinding::bind)
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPasswordToggle()
        setupListeners()
        setupObservers()
    }

    private fun setupPasswordToggle() {
        viewBinding.textInputLayoutPassword.setupPasswordToggle(viewBinding.etPassword)
        viewBinding.textInputLayoutPasswordRepeat.setupPasswordToggle(viewBinding.etPasswordRepeat)
    }

    private fun setupListeners() {
        viewBinding.etPhone.doOnTextChanged { text, _, _, _ ->
            viewModel.onPhoneChanged(text.toString())
        }

        viewBinding.etPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        viewBinding.etPasswordRepeat.doOnTextChanged { text, _, _, _ ->
            viewModel.onConfirmPasswordChanged(text.toString())
        }

        viewBinding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.register()
                return@setOnEditorActionListener true
            }
            false
        }

        viewBinding.ivBackIcon.setOnClickListener {
            hideKeyboard()
            viewModel.navigateToAuthorization()
        }

        viewBinding.btnLogin.setOnClickListener {
            viewModel.register()
        }
    }

    private fun setupObservers() {
        viewModel.uiState
            .onEach { state ->
                when (state) {
                    RegistrationUiState.Loading -> showProgress()
                    RegistrationUiState.Idle -> hideProgress()
                    RegistrationUiState.Success -> hideProgress()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.phoneState
            .onEach { state ->
                if (viewBinding.etPhone.text.toString() != state.value) {
                    viewBinding.etPhone.setText(state.value)
                    viewBinding.etPhone.setSelection(state.value.length)
                }
                viewBinding.textInputLayoutPhone.error =
                    if (state.shouldShowError) getString(R.string.error_invalid_phone) else null
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.passwordState
            .onEach { state ->
                viewBinding.textInputLayoutPassword.error =
                    if (state.shouldShowError) getString(R.string.error_invalid_password) else null
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.confirmPasswordState
            .onEach { state ->
                viewBinding.textInputLayoutPasswordRepeat.error =
                    if (state.shouldShowError) getString(R.string.error_password_mismatch) else null
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.events
            .onEach { event ->
                when (event) {
                    is RegistrationEvent.ShowError -> {
                        handleErrors(event.error)
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleErrors(error: RegistrationError) {
        val message = when (error) {
            RegistrationError.UserAlreadyExists -> R.string.error_user_already_exists
            RegistrationError.Unknown -> R.string.error_unknown
        }
        showToast(getString(message))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
