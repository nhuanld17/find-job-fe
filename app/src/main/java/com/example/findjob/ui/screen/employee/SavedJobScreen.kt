package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.findjob.data.model.response.NewestJobResponse
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.SavedJobState
import com.example.findjob.viewmodel.SavedJobViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SavedJobScreen(
    navController: NavController,
    viewModel: SavedJobViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSavedJobs()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .padding(bottom = 100.dp)
        ) {
            Text(
                text = "Saved Jobs",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF1A1446)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            when (state) {
                is SavedJobState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SavedJobState.Error -> {
                    Text(
                        text = (state as SavedJobState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                is SavedJobState.Success -> {
                    val jobs = (state as SavedJobState.Success).jobs
                    if (jobs.isEmpty()) {
                        Text(
                            text = "No saved jobs",
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        jobs.forEach { savedJob ->
                            val newestJob = NewestJobResponse(
                                id = savedJob.id,
                                imageUrl = savedJob.imageUrl ?: "",
                                jobTitle = savedJob.jobTitle ?: "No Title",
                                companyName = savedJob.companyName ?: "Unknown Company",
                                location = savedJob.location ?: "Unknown Location",
                                jobPosition = savedJob.jobPosition ?: "Unknown Position",
                                jobType = savedJob.jobType ?: "Unknown Type",
                                salary = savedJob.salary ?: "Not Specified",
                                createdAt = savedJob.createdAt,
                                saved = true
                            )
                            SavedJobCard(job = newestJob, viewModel = viewModel, navController = navController)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                SavedJobState.Idle -> {
                    // Do nothing, waiting for initial load
                }
            }
        }
        
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SavedJobCard(
    job: NewestJobResponse,
    viewModel: SavedJobViewModel,
    navController: NavController
) {
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()

    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                job.id?.let { jobId ->
                    navController.navigate("jobDescription/$jobId")
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE5E1FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (job.imageUrl?.isNotEmpty() == true) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = job.imageUrl)
                                        .build()
                                ),
                                contentDescription = "Company Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Company Logo",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = job.jobTitle ?: "No Title",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1446)
                        )
                        Text(
                            text = "${job.companyName ?: "Unknown"} · ${job.location ?: "Unknown"}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (!isSaving) {
                                job.id?.let { jobId ->
                                    viewModel.saveJob(jobId)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (job.saved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (job.saved) "Unsave Job" else "Save Job",
                            tint = if (job.saved) Color.Red else Color.Gray
                        )
                    }
                }
            }
            if (saveError != null) {
                Text(
                    text = saveError ?: "",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = job.salary ?: "Salary not specified",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1446)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Posted: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(job.createdAt)}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = job.jobPosition ?: "Unknown",
                    modifier = Modifier
                        .background(Color(0xFFF3F3F3), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    color = Color(0xFF1A1446)
                )
                Text(
                    text = job.jobType ?: "Unknown",
                    modifier = Modifier
                        .background(Color(0xFFF3F3F3), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    color = Color(0xFF1A1446)
                )
                Text(
                    text = "Apply",
                    modifier = Modifier
                        .background(Color(0xFFFFE1B8), RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    color = Color(0xFF1A1446),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}