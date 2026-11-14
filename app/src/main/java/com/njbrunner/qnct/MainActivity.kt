package com.njbrunner.qnct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.njbrunner.qnct.ui.main.MainScreen
import com.njbrunner.qnct.ui.onboarding.OnboardingViewModel
import com.njbrunner.qnct.ui.onboarding.OnboardingScreen
import com.njbrunner.qnct.ui.theme.QnctTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application = application as QnctApplication
        val profileRepository = application.profileRepository

        setContent {
            QnctTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val onboardingComplete by profileRepository.onboardingCompleteFlow
                        .collectAsState(initial = null)

                    when (onboardingComplete) {
                        true -> MainScreen()
                        false -> {
                            val onboardingViewModel = viewModel(
                                factory = OnboardingViewModel.Factory(profileRepository)
                            )
                            OnboardingScreen(viewModel = onboardingViewModel)
                        }
                        null -> Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
