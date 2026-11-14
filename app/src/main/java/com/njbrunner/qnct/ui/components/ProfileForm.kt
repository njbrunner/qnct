package com.njbrunner.qnct.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.njbrunner.qnct.R
import com.njbrunner.qnct.data.model.Profile
import com.njbrunner.qnct.domain.validation.ProfileValidationState

@Composable
fun ProfileForm(
    profile: Profile,
    validationState: ProfileValidationState,
    onProfileChange: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = profile.fullName,
            onValueChange = { onProfileChange(profile.copy(fullName = it)) },
            label = { Text(stringResource(R.string.full_name_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = profile.phoneNumber,
            onValueChange = { onProfileChange(profile.copy(phoneNumber = it)) },
            label = { Text(stringResource(R.string.phone_number_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = profile.email,
            onValueChange = { onProfileChange(profile.copy(email = it)) },
            label = { Text(stringResource(R.string.email_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = profile.instagramUsername,
            onValueChange = { onProfileChange(profile.copy(instagramUsername = it)) },
            label = { Text(stringResource(R.string.instagram_username_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
