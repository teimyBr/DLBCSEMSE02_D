package com.boardgamer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.viewmodel.LoginState
import com.boardgamer.viewmodel.LoginViewModel

@Composable
fun Home(navController: NavController) {
    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {

            //Sobald die Anmeldung erfolgreich war, geht es ab hier weiter zur App
            //z.B. zum "Aktuelles"-Screen

            navController.navigate("Aktuelles") {
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
                text = "Willkommen bei BoardGamer",
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Melde dich direkt an",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Name") },
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Passwort") },
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loginUser(username, password) },
                shape = RectangleShape,
                enabled = loginState !is LoginState.Loading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Anmelden")
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
                text = "Noch nicht registriert?",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { navController.navigate("registration") },
                shape = RectangleShape
            ) {
                Text(
                    text ="Zur Registrierung"
                )
            }
        }
    }
}