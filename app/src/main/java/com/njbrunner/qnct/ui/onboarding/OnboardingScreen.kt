package com.njbrunner.qnct.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.njbrunner.qnct.R
import com.njbrunner.qnct.data.model.Profile
import com.njbrunner.qnct.domain.validation.ProfileValidator
import com.njbrunner.qnct.ui.components.ProfileForm
import com.njbrunner.qnct.ui.theme.QnctTheme

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OnboardingContent(
        uiState = uiState,
        onFullNameChange = viewModel::updateFullName,
        onPhoneNumberChange = viewModel::updatePhoneNumber,
        onEmailChange = viewModel::updateEmail,
        onInstagramUsernameChange = viewModel::updateInstagramUsername,
        onSaveClick = viewModel::saveProfile
    )
}

@Composable
private fun OnboardingContent(
    uiState: OnboardingUiState,
    onFullNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onInstagramUsernameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.onboarding_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            ProfileForm(
                profile = uiState.profile,
                validationState = uiState.validationState,
                onProfileChange = { profile ->
                    if (profile.fullName != uiState.profile.fullName) {
                        onFullNameChange(profile.fullName)
                    }
                    if (profile.phoneNumber != uiState.profile.phoneNumber) {
                        onPhoneNumberChange(profile.phoneNumber)
                    }
                    if (profile.email != uiState.profile.email) {
                        onEmailChange(profile.email)
                    }
                    if (profile.instagramUsername != uiState.profile.instagramUsername) {
                        onInstagramUsernameChange(profile.instagramUsername)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSaveClick,
                enabled = uiState.validationState.isValid && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_profile_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    QnctTheme {
        OnboardingContent(
            uiState = OnboardingUiState(
                profile = Profile(fullName = "Jordan Casey"),
                validationState = ProfileValidator.validateProfile(
                    Profile(fullName = "Jordan Casey")
                )
            ),
            onFullNameChange = {},
            onPhoneNumberChange = {},
            onEmailChange = {},
            onInstagramUsernameChange = {},
            onSaveClick = {}
        )
    }
}
