package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import coil.compose.rememberAsyncImagePainter
import com.example.findjob.R
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.card.CvCard
import com.example.findjob.viewmodel.JobDescriptionViewModel

@Composable
fun JobDescriptionScreen(navController: NavController, id: Int, viewModel: JobDescriptionViewModel = hiltViewModel()) {
    val jobDetail by viewModel.jobDetail.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Gọi API khi vào màn
    androidx.compose.runtime.LaunchedEffect(id) {
        viewModel.fetchJobDetail(id)
    }

    Box(
        modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))
    ) {
        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Error", color = Color.Red)
                }
            }
            jobDetail != null -> {
                val job = jobDetail!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 8.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { navController.popBackStack() },
                            colorFilter = ColorFilter.tint(Color.Black)
                        )
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD6F3FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(job.imageLink),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Title and info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = job.jobTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF23235B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = job.recruiterName, color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                            Text(text = "  •  ", color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                            Text(text = job.location, color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                            Text(text = "  •  ", color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                            Text(text = "1 day ago", color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = { navController.navigate("companyInfo/${job.recruiterMail}") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEAFF)),
                            shape = CircleShape,
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text(text = "Company Info", color = Color(0xFF6C63FF), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    // Job Description
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Job Description",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                            color = Color(0xFF23235B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = job.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF23235B)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Requirements",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                            color = Color(0xFF23235B)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        (job.requirement ?: "").split("\n").forEach {
                            if (it.isNotBlank()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(text = "• ", color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                                    Text(text = it, color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        // Informations section
                        Text(
                            text = "Informations",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                            color = Color(0xFF23235B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoRow(label = "Position", value = job.position)
                        InfoDivider()
                        InfoRow(label = "Qualification", value = job.qualification)
                        InfoDivider()
                        InfoRow(label = "Experience", value = job.experience)
                        InfoDivider()
                        InfoRow(label = "Job Type", value = job.jobType)
                        InfoDivider()
                        InfoRow(label = "Salary", value = job.salary)
                        InfoDivider()
                        Spacer(modifier = Modifier.height(36.dp))
                        Button(
                            onClick = { navController.navigate("uploadCV/${id}&${job.jobTitle}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23235B))
                        ) {
                            Text(
                                text = "APPLY NOW",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Spacer(modifier = Modifier.height(150.dp))
                }
            }
        }
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// Helper composables
@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            color = Color(0xFF23235B)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6F6F6F)
        )
    }
}

@Composable
private fun InfoDivider() {
    androidx.compose.material3.Divider(
        color = Color(0xFFE5E5E5),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 0.dp)
    )
}