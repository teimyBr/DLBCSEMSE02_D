package com.boardgamer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.RegistrationState
import com.boardgamer.viewmodel.RegistrationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registration(navController: NavController) {

    val viewModel: RegistrationViewModel = viewModel()

    val registrationState by viewModel.registrationState.collectAsState()
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordRepeat by viewModel.passwordRepeat.collectAsState()
    val location by viewModel.location.collectAsState()
    val passwordsDoNotMatch by viewModel.passwordsDoNotMatch.collectAsState()

    LaunchedEffect(key1 = registrationState) {
        if (registrationState is RegistrationState.Success) {
            navController.popBackStack()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, stringResource(R.string.back_to_home))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.register_for_boardgamer),
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.name)
                    )
                },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.email)
                    )
                },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { viewModel.onLocationChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.location)
                    )
                },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.password)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch,
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordRepeat,
                onValueChange = { viewModel.onPasswordRepeatChange(it) },
                label = {
                    Text(
                        text = stringResource(id = R.string.password_repeat)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch,
                supportingText = {
                    if (passwordsDoNotMatch)
                        Text(
                            text = stringResource(id = R.string.passwordsDoNotMatch)
                        )
                },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (registrationState is RegistrationState.Error) {
                Text(
                    text = (registrationState as RegistrationState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.submitPlayerRegistration() },
                shape = RectangleShape,
                enabled = registrationState !is RegistrationState.Loading && username.isNotBlank() && location.isNotBlank()
            ) {
                if (registrationState is RegistrationState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.registration)
                    )
                }
            }
        }
    }
}