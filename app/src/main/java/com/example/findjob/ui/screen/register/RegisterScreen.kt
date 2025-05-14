package com.example.findjob.ui.screen.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.AppTopBar
import com.example.findjob.viewmodel.RegisterUiState
import com.example.findjob.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Employee") }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val registerState by viewModel.uiState.collectAsState()

    val roleOptions = listOf("Employee", "Recruiter")

    // Handle error message timeout
    LaunchedEffect(showError) {
        if (showError) {
            delay(2000)
            showError = false
        }
    }

    fun validateInputs(): Boolean {
        // Email validation
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        if (!emailRegex.matches(email)) {
            errorMessage = "Invalid email format"
            showError = true
            return false
        }

        // Password validation
        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            showError = true
            return false
        }

        return true
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                // Clear all fields
                name = ""
                email = ""
                password = ""
                selectedRole = "Employee"
                
                // Show success message
                showSuccess = true
                
                // Hide success message after 2 seconds
                delay(2000)
                showSuccess = false
            }
            is RegisterUiState.Error -> {
                errorMessage = (registerState as RegisterUiState.Error).message
                showError = true
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Register")
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(top = 50.dp, bottom = 100.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Role Selection
                Text(
                    text = "Role",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Start),
                    color = Color(0xFF1A1330)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    roleOptions.forEach { role ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { selectedRole = role }
                        ) {
                            RadioButton(
                                selected = selectedRole == role,
                                onClick = { selectedRole = role },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF1A1330)
                                )
                            )
                            Text(
                                text = role.uppercase(),
                                color = Color(0xFF1A1330),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Error Message
                if (showError) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Success Message
                if (showSuccess) {
                    Text(
                        text = "Registration successful!",
                        color = Color(0xFF4CAF50), // Green color for success
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Sign Up Button
                OutlinedButton(
                    onClick = { 
                        if (validateInputs()) {
                            val role = when (selectedRole) {
                                "Employee" -> "ROLE_EMPLOYEE"
                                "Recruiter" -> "ROLE_RECRUITER"
                                else -> "ROLE_EMPLOYEE"
                            }
                            viewModel.register(role, name, email, password)
                        }
                    },
                    enabled = registerState !is RegisterUiState.Loading,
                    border = BorderStroke(0.dp, Color.Transparent),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF130160))
                ) {
                    Text(
                        if (registerState is RegisterUiState.Loading) "Signing up..." else "Sign Up",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 18.dp, horizontal = 100.dp)
                    )
                }

                // Sign In Text
                Column {
                    Text(text = "You don't have an account yet? ")
                    Text(
                        text = "Sign in",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable(onClick = { navController.navigate("login") })
                            .padding(start = 4.dp),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}



