package com.njbrunner.qnct

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.njbrunner.qnct.data.repository.ProfileRepository
import java.io.File

class QnctApplication : Application() {

    val profileRepository by lazy {
        ProfileRepository(
            PreferenceDataStoreFactory.create(
                produceFile = { File(filesDir, PROFILE_PREFERENCES_FILE) }
            )
        )
    }

    companion object {
        private const val PROFILE_PREFERENCES_FILE = "profile.preferences_pb"
    }
}
