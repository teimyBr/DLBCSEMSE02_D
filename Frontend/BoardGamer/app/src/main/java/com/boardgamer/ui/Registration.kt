package com.boardgamer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.model.FoodDirection
import com.boardgamer.viewmodel.RegistrationState
import com.boardgamer.viewmodel.RegistrationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registration(navController: NavController) {

    val viewModel: RegistrationViewModel = viewModel()
    val registrationState by viewModel.registrationState.collectAsState()
    val foodDirections = viewModel.foodDirections

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRepeat by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var passwordsDoNotMatch by remember { mutableStateOf(false) }
    var isFoodDropdownExpanded by remember { mutableStateOf(false) }
    var selectedFood by remember { mutableStateOf<FoodDirection?>(null) }

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
                        Icon(Icons.Default.Close, "Zurück zum Startbildschirm")
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
                text = "Registriere dich für BoardGamer",
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Name") },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-Mail") },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Wohnort") },
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = isFoodDropdownExpanded,
                onExpandedChange = { isFoodDropdownExpanded = !isFoodDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedFood?.designation ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Bevorzugtes Essen") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isFoodDropdownExpanded) },
                    modifier = Modifier.menuAnchor(),
                    enabled = registrationState !is RegistrationState.Loading
                )
                ExposedDropdownMenu(
                    expanded = isFoodDropdownExpanded,
                    onDismissRequest = { isFoodDropdownExpanded = false }
                ) {
                    foodDirections.forEach { food ->
                        DropdownMenuItem(
                            text = { Text(food.designation) },
                            onClick = {
                                selectedFood = food
                                isFoodDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Passwort") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch,
                enabled = registrationState !is RegistrationState.Loading
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = passwordRepeat,
                onValueChange = { passwordRepeat = it },
                label = { Text("Passwort wiederholen") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordsDoNotMatch,
                supportingText = { if (passwordsDoNotMatch) Text("Die Passwörter stimmen nicht überein!") },
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
                onClick = {
                    if (password == passwordRepeat) {
                        passwordsDoNotMatch = false
                        selectedFood?.let { food ->
                            viewModel.registerUser(
                                name = username,
                                email = email,
                                password = password,
                                favouriteFoodId = food.id,
                                location = location
                            )
                        }
                    } else {
                        passwordsDoNotMatch = true
                    }
                },
                shape = RectangleShape,
                enabled = registrationState !is RegistrationState.Loading && username.isNotBlank() && location.isNotBlank() && selectedFood != null
            ) {
                if (registrationState is RegistrationState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrieren")
                }
            }
        }
    }
}