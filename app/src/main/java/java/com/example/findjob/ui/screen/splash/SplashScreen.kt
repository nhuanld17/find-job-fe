package com.example.findjob.ui.screen.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.findjob.utils.InfoManager

@Composable
fun SplashScreen(
    navController: NavController,
    infoManager: InfoManager
) {
    LaunchedEffect(Unit) {
        if (infoManager.isLoggedIn()) {
            when (infoManager.getRole()) {
                "ROLE_EMPLOYEE" -> {
                    navController.navigate("employeeHome") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                "ROLE_RECRUITER" -> {
                    navController.navigate("recruiterHome") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                else -> {
                    infoManager.clearTokens()
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        } else {
            infoManager.clearTokens()
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
} 