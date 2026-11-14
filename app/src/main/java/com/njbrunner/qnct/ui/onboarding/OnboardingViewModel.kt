package com.njbrunner.qnct.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.njbrunner.qnct.data.model.Profile
import com.njbrunner.qnct.data.repository.ProfileRepository
import com.njbrunner.qnct.domain.validation.ProfileValidationState
import com.njbrunner.qnct.domain.validation.ProfileValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateFullName(fullName: String) {
        updateProfile(_uiState.value.profile.copy(fullName = fullName))
    }

    fun updatePhoneNumber(phoneNumber: String) {
        updateProfile(_uiState.value.profile.copy(phoneNumber = phoneNumber))
    }

    fun updateEmail(email: String) {
        updateProfile(_uiState.value.profile.copy(email = email))
    }

    fun updateInstagramUsername(instagramUsername: String) {
        updateProfile(_uiState.value.profile.copy(instagramUsername = instagramUsername))
    }

    fun saveProfile() {
        val currentState = _uiState.value
        val validationState = ProfileValidator.validateProfile(currentState.profile)

        if (!validationState.isValid) {
            _uiState.update { it.copy(validationState = validationState) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, validationState = validationState) }
            repository.completeOnboarding(currentState.profile)
            _uiState.update { it.copy(isSaving = false, onboardingComplete = true) }
        }
    }

    private fun updateProfile(profile: Profile) {
        _uiState.update { currentState ->
            currentState.copy(
                profile = profile,
                validationState = ProfileValidator.validateProfile(profile)
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: ProfileRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
                return OnboardingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}

data class OnboardingUiState(
    val profile: Profile = Profile(),
    val validationState: ProfileValidationState = ProfileValidator.validateProfile(Profile()),
    val isSaving: Boolean = false,
    val onboardingComplete: Boolean = false
)
