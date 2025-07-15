package com.boardgamer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember {mutableStateOf(false)}

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
                label = { Text("Name") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Passwort") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                /*
                    Hier kommt dann später der Code für die Authentifizierung rein
                */
                if(username.isEmpty() || password.isEmpty()){
                    showError = true;
                } else {
                    showError = false;
                }

            },
                shape = RectangleShape
            ) {
                Text(
                    text = "Anmelden"
                )
            }

            if(showError){
                Text(
                    text = "Benutzername und Passwort eingeben!",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Noch nicht registriert?",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(onClick = {
                navController.navigate("registration")
            },
                shape = RectangleShape
            ) {
                Text("Zur Registrierung")
            }
        }
    }
}