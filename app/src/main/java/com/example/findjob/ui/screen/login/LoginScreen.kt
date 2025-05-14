package com.example.findjob.ui.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.findjob.utils.InfoManager
import com.example.findjob.viewmodel.LoginState
import com.example.findjob.viewmodel.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    // Handle error message timeout
    LaunchedEffect(showError) {
        if (showError) {
            delay(2000)
            showError = false
        }
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                // Get user role and navigate accordingly
                val role = viewModel.infoManager.getRole()
                when (role) {
                    "ROLE_EMPLOYEE" -> {
                        navController.navigate("employeeHome") {
                            popUpTo("employeeHome") { inclusive = true }
                        }
                    }
                    "ROLE_RECRUITER" -> {
                        navController.navigate("recruiterHome") {
                            popUpTo("recruiterHome") { inclusive = true }
                        }
                    }
                }
            }
            is LoginState.Error -> {
                errorMessage = (loginState as LoginState.Error).message
                showError = true
            }
            else -> {}
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 100.dp, bottom = 100.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1330),
                modifier = Modifier.padding(bottom = 24.dp)
            )

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

            Spacer(modifier = Modifier.height(24.dp))

            // Error Message
            if (showError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Sign In Button
            OutlinedButton(
                onClick = { viewModel.login(email, password) },
                enabled = loginState !is LoginState.Loading,
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF130160))
            ) {
                Text(
                    if (loginState is LoginState.Loading) "Signing in..." else "Sign In",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 18.dp, horizontal = 100.dp)
                )
            }

            // Sign Up Text
            Column {
                Text(text = "Don't have an account? ")
                Text(
                    text = "Sign up",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable(onClick = { navController.navigate("register") })
                        .padding(start = 4.dp),
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}


