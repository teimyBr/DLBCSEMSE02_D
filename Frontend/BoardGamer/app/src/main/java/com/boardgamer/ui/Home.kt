package com.boardgamer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.R
import com.boardgamer.viewmodel.CurrentEventsViewModel
import com.boardgamer.viewmodel.HomeViewModel
import com.boardgamer.viewmodel.LoginState
import com.boardgamer.viewmodel.RegistrationViewModel

@Composable
fun Home(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()

    val loginState by viewModel.loginState.collectAsState()
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val isButtonEnabled by viewModel.isLoginButtonEnabled.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(CurrentEventsViewModel.SCREEN_NAME) {
                popUpTo("Home") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.app_welcome),
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(id = R.string.login_now),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { viewModel.onUsernameChange(it) },
                label = {
                    Text(
                        text = stringResource(R.string.name)
                    )
                },
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
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loginPlayer() },
                shape = RectangleShape,
                enabled = isButtonEnabled
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.login)
                    )
                }
            }


            if (loginState is LoginState.Error) {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(id = R.string.not_registered_yet),
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { navController.navigate(RegistrationViewModel.SCREEN_NAME) },
                shape = RectangleShape
            ) {
                Text(
                    text = stringResource(id = R.string.go_to_registration)
                )
            }
        }
    }
}