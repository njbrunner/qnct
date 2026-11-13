package com.njbrunner.qnct.data.repository

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.njbrunner.qnct.data.model.Profile
import java.io.File
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileRepositoryTest {

    private lateinit var testDataStoreFile: File
    private lateinit var repository: ProfileRepository

    @Before
    fun setUp() {
        testDataStoreFile = File.createTempFile("test_profile", ".preferences_pb")
        val dataStore = PreferenceDataStoreFactory.create(
            produceFile = { testDataStoreFile }
        )
        repository = ProfileRepository(dataStore)
    }

    @After
    fun tearDown() {
        testDataStoreFile.delete()
    }

    @Test
    fun `profileFlow emits default empty profile when nothing is saved`() = runTest {
        val profile = repository.profileFlow.first()

        assertEquals(Profile(), profile)
    }

    @Test
    fun `save persists profile and emits it through profileFlow`() = runTest {
        val expected = Profile(
            fullName = "Jordan Casey",
            phoneNumber = "+1 555-123-4567",
            email = "jordan@example.com",
            instagramUsername = "jordan.casey"
        )

        repository.save(expected)
        val actual = repository.profileFlow.first()

        assertEquals(expected, actual)
    }

    @Test
    fun `save overwrites previous profile`() = runTest {
        val first = Profile(fullName = "First User", phoneNumber = "555-0000")
        val second = Profile(fullName = "Second User", email = "second@example.com")

        repository.save(first)
        repository.save(second)

        val profile = repository.profileFlow.first()

        assertEquals(second, profile)
    }
}
