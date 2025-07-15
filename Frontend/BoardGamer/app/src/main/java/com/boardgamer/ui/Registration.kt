package com.boardgamer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registration(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("" ) }
    var passwordsDoNotMatch by remember {mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Zurück zum Startbildschirm"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registriere dich für BoardGamer",
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-Mail") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Passwort") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordRepeat,
                onValueChange = { passwordRepeat = it },
                label = { Text("Passwort wiederholen") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch,

                supportingText = {
                    if (passwordsDoNotMatch){
                        Text("Die Passwörter stimmen nicht überein!")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {


                if (password == passwordRepeat){
                    passwordsDoNotMatch = false

                    // Platzhalter für späterenAPI-Code etc.

                } else{
                    passwordsDoNotMatch = true;
                }

            }) {
                Text("Registrieren")
            }
        }
    }
}