package com.njbrunner.qnct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    var onboardingComplete by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        profileRepository.onboardingCompleteFlow.collect { onboardingComplete = it }
                    }

                    if (onboardingComplete) {
                        MainScreen()
                    } else {
                        val onboardingViewModel = viewModel(
                            factory = OnboardingViewModel.Factory(profileRepository)
                        )
                        OnboardingScreen(viewModel = onboardingViewModel)
                    }
                }
            }
        }
    }
}
