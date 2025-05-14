package com.example.findjob.ui.screen.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.avatar.Profile
import com.example.findjob.ui.components.form.ProfileForm
import com.example.findjob.utils.InfoManager
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.AlertDialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.findjob.ui.components.form.ChangePasswordForm
import com.example.findjob.viewmodel.ProfileRecruiterState
import com.example.findjob.viewmodel.RecruiterProfileViewModel

@Composable
fun RecruiterProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val infoManager = remember { InfoManager(context) }
    val viewModel: RecruiterProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (state) {
            is ProfileRecruiterState.Loading -> isLoading = true
            is ProfileRecruiterState.UpdateSuccess -> {
                isLoading = false
                showSuccess = true
                errorMessage = null
            }
            is ProfileRecruiterState.Error -> {
                isLoading = false
                errorMessage = (state as ProfileRecruiterState.Error).message
            }
            else -> {
                isLoading = false
                errorMessage = null
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getProfileImage()
    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Profile(navController, infoManager = infoManager, viewModel = viewModel)
            ProfileForm()
            Spacer(modifier = Modifier.height(16.dp))
            ChangePasswordForm(
                isLoading = isLoading,
                errorMessage = errorMessage,
                showSuccess = showSuccess,
                onSave = { oldPass, newPass ->
                    viewModel.changePassword(oldPass, newPass, newPass)
                }
            )
            Spacer(modifier = Modifier.height(140.dp))
        }
        RecruiterBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}