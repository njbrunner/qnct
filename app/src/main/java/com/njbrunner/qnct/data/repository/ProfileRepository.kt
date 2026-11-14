package com.njbrunner.qnct.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.njbrunner.qnct.data.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ProfileRepository(private val dataStore: DataStore<Preferences>) {

    val profileFlow: Flow<Profile> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            Profile(
                fullName = preferences[KEY_FULL_NAME] ?: "",
                phoneNumber = preferences[KEY_PHONE_NUMBER] ?: "",
                email = preferences[KEY_EMAIL] ?: "",
                instagramUsername = preferences[KEY_INSTAGRAM_USERNAME] ?: ""
            )
        }

    val onboardingCompleteFlow: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[KEY_ONBOARDING_COMPLETE] ?: false
        }

    suspend fun save(profile: Profile) {
        dataStore.edit { preferences ->
            preferences[KEY_FULL_NAME] = profile.fullName
            preferences[KEY_PHONE_NUMBER] = profile.phoneNumber
            preferences[KEY_EMAIL] = profile.email
            preferences[KEY_INSTAGRAM_USERNAME] = profile.instagramUsername
        }
    }

    suspend fun completeOnboarding(profile: Profile) {
        dataStore.edit { preferences ->
            preferences[KEY_FULL_NAME] = profile.fullName
            preferences[KEY_PHONE_NUMBER] = profile.phoneNumber
            preferences[KEY_EMAIL] = profile.email
            preferences[KEY_INSTAGRAM_USERNAME] = profile.instagramUsername
            preferences[KEY_ONBOARDING_COMPLETE] = true
        }
    }

    companion object {
        private val KEY_FULL_NAME = stringPreferencesKey("full_name")
        private val KEY_PHONE_NUMBER = stringPreferencesKey("phone_number")
        private val KEY_EMAIL = stringPreferencesKey("email")
        private val KEY_INSTAGRAM_USERNAME = stringPreferencesKey("instagram_username")
        private val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }
}