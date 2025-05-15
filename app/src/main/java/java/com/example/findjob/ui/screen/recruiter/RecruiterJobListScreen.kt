package com.example.findjob.ui.screen.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.card.RecruiterCard
import com.example.findjob.viewmodel.RecruiterJobListViewModel
import com.example.findjob.data.model.response.ListJobResponse
import com.example.findjob.viewmodel.JobListState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecruiterJobListScreen(
    navController: NavController,
    viewModel: RecruiterJobListViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val state by viewModel.state.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "My Job Post",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                color = Color(0xFF23235B)
            )

            // Tab selection buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) Color(0xFF23235B) else Color.White
                    )
                ) {
                    Text(
                        text = "Active Jobs",
                        color = if (selectedTab == 0) Color.White else Color(0xFF23235B)
                    )
                }

                Button(
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) Color(0xFF23235B) else Color.White
                    )
                ) {
                    Text(
                        text = "Expired Jobs",
                        color = if (selectedTab == 1) Color.White else Color(0xFF23235B)
                    )
                }
            }

            when (state) {
                is JobListState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF23235B)
                        )
                    }
                }
                is JobListState.Success -> {
                    val successState = state as JobListState.Success
                    val jobs = if (selectedTab == 0) successState.activeJobs else successState.expiredJobs

                    if (jobs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selectedTab == 0) "No active jobs" else "No expired jobs",
                                color = Color(0xFF7C8493)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                        ) {
                            jobs.forEach { job ->
                                RecruiterCard(
                                    navController = navController,
                                    job = job
                                )
                            }
                        }
                    }
                }
                is JobListState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = (state as JobListState.Error).message,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.getJobPosts() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF23235B)
                                )
                            ) {
                                Text("Retry", color = Color.White)
                            }
                        }
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