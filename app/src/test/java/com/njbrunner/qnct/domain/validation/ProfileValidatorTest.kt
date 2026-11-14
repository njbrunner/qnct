package com.njbrunner.qnct.domain.validation

import com.njbrunner.qnct.data.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileValidatorTest {

    @Test
    fun `validateFullName returns Valid for non-blank name`() {
        val result = ProfileValidator.validateFullName("Jordan Casey")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validateFullName returns Required error for blank name`() {
        val result = ProfileValidator.validateFullName("   ")

        assertEquals(
            ValidationResult.Invalid(ValidationError.FULL_NAME_REQUIRED),
            result
        )
    }

    @Test
    fun `validateFullName returns Required error for empty name`() {
        val result = ProfileValidator.validateFullName("")

        assertEquals(
            ValidationResult.Invalid(ValidationError.FULL_NAME_REQUIRED),
            result
        )
    }

    @Test
    fun `validatePhoneNumber returns Valid for empty phone`() {
        val result = ProfileValidator.validatePhoneNumber("")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validatePhoneNumber returns Valid for blank phone`() {
        val result = ProfileValidator.validatePhoneNumber("   ")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validatePhoneNumber returns Valid for well-formed phone`() {
        val result = ProfileValidator.validatePhoneNumber("+1 (555) 123-4567")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validatePhoneNumber returns Valid for phone without formatting`() {
        val result = ProfileValidator.validatePhoneNumber("5551234567")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validatePhoneNumber returns Invalid for too few digits`() {
        val result = ProfileValidator.validatePhoneNumber("123-456")

        assertEquals(
            ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID),
            result
        )
    }

    @Test
    fun `validatePhoneNumber returns Invalid for invalid characters`() {
        val result = ProfileValidator.validatePhoneNumber("555-abc-1234")

        assertEquals(
            ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID),
            result
        )
    }

    @Test
    fun `validateEmail returns Valid for empty email`() {
        val result = ProfileValidator.validateEmail("")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validateEmail returns Valid for well-formed email`() {
        val result = ProfileValidator.validateEmail("jordan@example.com")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validateEmail returns Invalid for missing at symbol`() {
        val result = ProfileValidator.validateEmail("jordan.example.com")

        assertEquals(
            ValidationResult.Invalid(ValidationError.EMAIL_INVALID),
            result
        )
    }

    @Test
    fun `validateEmail returns Invalid for missing domain`() {
        val result = ProfileValidator.validateEmail("jordan@")

        assertEquals(
            ValidationResult.Invalid(ValidationError.EMAIL_INVALID),
            result
        )
    }

    @Test
    fun `validateInstagramUsername returns Valid for empty username`() {
        val result = ProfileValidator.validateInstagramUsername("")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validateInstagramUsername returns Valid for well-formed username`() {
        val result = ProfileValidator.validateInstagramUsername("jordan.casey")

        assertEquals(ValidationResult.Valid, result)
    }

    @Test
    fun `validateInstagramUsername returns Invalid for leading period`() {
        val result = ProfileValidator.validateInstagramUsername(".jordancasey")

        assertEquals(
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID),
            result
        )
    }

    @Test
    fun `validateInstagramUsername returns Invalid for trailing period`() {
        val result = ProfileValidator.validateInstagramUsername("jordancasey.")

        assertEquals(
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID),
            result
        )
    }

    @Test
    fun `validateInstagramUsername returns Invalid for consecutive periods`() {
        val result = ProfileValidator.validateInstagramUsername("jordan..casey")

        assertEquals(
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID),
            result
        )
    }

    @Test
    fun `validateInstagramUsername returns Invalid for username too long`() {
        val result = ProfileValidator.validateInstagramUsername("a".repeat(31))

        assertEquals(
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID),
            result
        )
    }

    @Test
    fun `validateProfile returns all Valid for empty optional fields`() {
        val profile = Profile(fullName = "Jordan Casey")

        val state = ProfileValidator.validateProfile(profile)

        assertTrue(state.isValid)
        assertEquals(ValidationResult.Valid, state.fullName)
        assertEquals(ValidationResult.Valid, state.phoneNumber)
        assertEquals(ValidationResult.Valid, state.email)
        assertEquals(ValidationResult.Valid, state.instagramUsername)
    }

    @Test
    fun `validateProfile returns Invalid when required full name is blank`() {
        val profile = Profile()

        val state = ProfileValidator.validateProfile(profile)

        assertFalse(state.isValid)
        assertEquals(
            ValidationResult.Invalid(ValidationError.FULL_NAME_REQUIRED),
            state.fullName
        )
    }

    @Test
    fun `validateProfile returns Invalid for any invalid optional field`() {
        val profile = Profile(
            fullName = "Jordan Casey",
            phoneNumber = "abc",
            email = "not-an-email",
            instagramUsername = ".."
        )

        val state = ProfileValidator.validateProfile(profile)

        assertFalse(state.isValid)
        assertEquals(
            ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID),
            state.phoneNumber
        )
        assertEquals(
            ValidationResult.Invalid(ValidationError.EMAIL_INVALID),
            state.email
        )
        assertEquals(
            ValidationResult.Invalid(ValidationError.INSTAGRAM_USERNAME_INVALID),
            state.instagramUsername
        )
    }
}
