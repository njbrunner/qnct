package com.njbrunner.qnct.ui.onboarding

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.njbrunner.qnct.data.model.Profile
import com.njbrunner.qnct.data.repository.ProfileRepository
import com.njbrunner.qnct.domain.validation.ValidationError
import com.njbrunner.qnct.domain.validation.ValidationResult
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testDataStoreFile: File
    private lateinit var repository: ProfileRepository
    private lateinit var viewModel: OnboardingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testDataStoreFile = File.createTempFile("test_profile", ".preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = { testDataStoreFile }
        )
        repository = ProfileRepository(dataStore)
        viewModel = OnboardingViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDataStoreFile.delete()
    }

    @Test
    fun `initial state has empty profile and invalid full name`() = runTest(testDispatcher) {
        val state = viewModel.uiState.value

        assertEquals(Profile(), state.profile)
        assertFalse(state.validationState.isValid)
        assertEquals(
            ValidationResult.Invalid(ValidationError.FULL_NAME_REQUIRED),
            state.validationState.fullName
        )
        assertFalse(state.onboardingComplete)
        assertFalse(state.isSaving)
    }

    @Test
    fun `updateFullName updates profile and validation state`() = runTest(testDispatcher) {
        viewModel.updateFullName("Jordan Casey")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Jordan Casey", state.profile.fullName)
        assertEquals(ValidationResult.Valid, state.validationState.fullName)
    }

    @Test
    fun `updatePhoneNumber with invalid input shows validation error`() = runTest(testDispatcher) {
        viewModel.updateFullName("Jordan Casey")
        viewModel.updatePhoneNumber("abc")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(
            ValidationResult.Invalid(ValidationError.PHONE_NUMBER_INVALID),
            state.validationState.phoneNumber
        )
        assertFalse(state.validationState.isValid)
    }

    @Test
    fun `saveProfile does nothing when validation fails`() = runTest(testDispatcher) {
        viewModel.saveProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.onboardingComplete)
        assertFalse(state.isSaving)
    }

    @Test
    fun `saveProfile marks onboarding complete when valid`() = runTest(testDispatcher) {
        viewModel.updateFullName("Jordan Casey")
        viewModel.updatePhoneNumber("+1 555-123-4567")
        viewModel.updateEmail("jordan@example.com")
        viewModel.updateInstagramUsername("jordan.casey")
        advanceUntilIdle()

        viewModel.saveProfile()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.onboardingComplete)
        assertFalse(state.isSaving)
    }
}
