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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.boardgamer.viewmodel.RegistrationState
import com.boardgamer.viewmodel.RegistrationViewModel
import com.boardgamer.R


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
    val selectedFood by viewModel.selectedFood.collectAsState()

    val foodDirections by viewModel.foodDirections.collectAsState()
    var isFoodDropdownExpanded by remember { mutableStateOf(false) }

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
                        text= stringResource(R.string.name)
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
                        text= stringResource(R.string.email)
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
                        text= stringResource(R.string.location)
                    )
                },
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
                    label = {
                        Text(
                        stringResource(id = R.string.favourite_food)
                    )},
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
                                viewModel.onFoodSelected(food)
                                isFoodDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = {
                    Text(
                        text= stringResource(R.string.password)
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
                supportingText = { if (passwordsDoNotMatch)
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
                enabled = registrationState !is RegistrationState.Loading && username.isNotBlank() && location.isNotBlank() && selectedFood != null
            ) {
                if (registrationState is RegistrationState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(
                        text = stringResource(R.string.registration)
                    )
                }
            }
        }
    }
}