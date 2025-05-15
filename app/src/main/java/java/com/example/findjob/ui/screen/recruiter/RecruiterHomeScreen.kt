package com.example.findjob.ui.screen.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.Preview.Preview
import com.example.findjob.viewmodel.RecruiterHomeViewModel
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.card.RecruiterCard
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.findjob.ui.components.avatar.Avatar
import com.example.findjob.utils.InfoManager
import com.example.findjob.viewmodel.HomeState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun RecruiterHomeScreen(
    navController: NavController,
    viewModel: RecruiterHomeViewModel = hiltViewModel(),
    infoManager: InfoManager
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Avatar(imageUrl = infoManager.getImageUrl(), name = infoManager.getName(), navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Create Your Job",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                color = Color(0xFF23235B)
            )
            Preview()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Recent Job Post",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                color = Color(0xFF23235B)
            )

            when (state) {
                is HomeState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF23235B)
                        )
                    }
                }
                is HomeState.Success -> {
                    val jobs = (state as HomeState.Success).jobs
                    if (jobs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No recent job posts",
                                color = Color(0xFF7C8493)
                            )
                        }
                    } else {
                        jobs.forEach { job ->
                            RecruiterCard(
                                navController = navController,
                                job = job
                            )
                        }
                    }
                }
                is HomeState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (state as HomeState.Error).message,
                            color = Color.Red
                        )
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
        RecruiterBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}