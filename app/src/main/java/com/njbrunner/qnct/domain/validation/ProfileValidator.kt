package com.njbrunner.qnct.domain.validation

import com.njbrunner.qnct.data.model.Profile

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: ValidationError) : ValidationResult()
}

enum class ValidationError {
    FULL_NAME_REQUIRED,
    PHONE_NUMBER_INVALID,
    EMAIL_INVALID,
    INSTAGRAM_USERNAME_INVALID
}

data class ProfileValidationState(
    val fullName: ValidationResult,
    val phoneNumber: ValidationResult,
    val email: ValidationResult,
    val instagramUsername: ValidationResult
) {
    val isValid: Boolean
        get() = fullName is ValidationResult.Valid &&
                phoneNumber is ValidationResult.Valid &&
                email is ValidationResult.Valid &&
                instagramUsername is ValidationResult.Valid
}

object ProfileValidator {

    private val phoneCharactersRegex = Regex("^\\+?[\\d\\s\\-()]+$")
    private val emailRegex = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )
    private val instagramUsernameRegex = Regex(
        "^(?!.*\\.\\.)[a-zA-Z0-9_](?:[a-zA-Z0-9_.]{0,28}[a-zA-Z0-9_])?$"
    )

    fun validateFullName(fullName: String): ValidationResult {
        return if (fullName.trim().isNotEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(ValidationError.FULL_NAME_REQUIRED)
        }
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult {
        if (phoneNumber.isBlank()) {
            return ValidationResult.Valid
        }

        val trimmed = phoneNumber.trim()
        if (!phoneCharactersRegex.matches(trimmed)) {
            return ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID)
        }

        val digitCount = trimmed.count { it.isDigit() }
        return if (digitCount >= 7) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Valid
        }

        return if (emailRegex.matches(email.trim())) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(ValidationError.EMAIL_INVALID)
        }
    }

    fun validateInstagramUsername(instagramUsername: String): ValidationResult {
        if (instagramUsername.isBlank()) {
            return ValidationResult.Valid
        }

        return if (instagramUsernameRegex.matches(instagramUsername.trim())) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID)
        }
    }

    fun validateProfile(profile: Profile): ProfileValidationState {
        return ProfileValidationState(
            fullName = validateFullName(profile.fullName),
            phoneNumber = validatePhoneNumber(profile.phoneNumber),
            email = validateEmail(profile.email),
            instagramUsername = validateInstagramUsername(profile.instagramUsername)
        )
    }
}
