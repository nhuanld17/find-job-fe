package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.HomeViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.findjob.data.model.response.NewestJobResponse
import com.example.findjob.viewmodel.SavedJobViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EmployeeHomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val newestJobs by viewModel.newestJobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNewestJobsPosts()
    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 130.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1446)
                    )
                    Text(
                        text = viewModel.infoManager.getName() ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1446)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Find Your Job",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1A1446)
                    )
                }
                // Avatar
                if (viewModel.infoManager.getImageUrl() != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = viewModel.infoManager.getImageUrl())
                                .build()
                        ),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate("employeeProfile")
                            },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate("employeeProfile")
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFB8F1FF), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Image(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Remote Job",
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "44.5k",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1446)
                    )
                    Text(
                        text = "Remote Job",
                        fontSize = 14.sp,
                        color = Color(0xFF1A1446)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFD6C7FF), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "66.8k",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1446)
                    )
                    Text(
                        text = "Full Time",
                        fontSize = 14.sp,
                        color = Color(0xFF1A1446)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFFE1B8), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "38.9k",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1446)
                    )
                    Text(
                        text = "Part Time",
                        fontSize = 14.sp,
                        color = Color(0xFF1A1446)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Most apply jobs
            Text(
                text = "Recent Job",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1446)
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Text(
                        text = error ?: "An error occurred",
                        color = Color.Red,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                newestJobs.isEmpty() -> {
                    Text(
                        text = "No jobs available",
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    newestJobs.forEach { job ->
                        JobCard(navController,job = job, viewModel = viewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        // Bottom bar
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun JobCard(
    navController: NavController,
    job: NewestJobResponse,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var isSaved by remember { mutableStateOf(job.saved) }
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("jobDescription/${job.id}")  },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE5E1FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!job.imageUrl.isNullOrEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = job.imageUrl)
                                        .build()
                                ),
                                contentDescription = "Company Logo",
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape), // Bo tròn ảnh
                                contentScale = ContentScale.Crop // Lấp đầy vùng hiển thị
                            )
                        } else {
                            Image(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Company Logo",
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape),
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
                                isSaved = !isSaved
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
                            imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isSaved) "Unsave Job" else "Save Job",
                            tint = if (isSaved) Color.Red else Color.Gray
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
                Button(
                    onClick = { navController.navigate("jobDescription/${job.id}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFE1B8)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                        .height(32.dp)
                ) {
                    Text(
                        text = "Apply",
                        fontSize = 12.sp,
                        color = Color(0xFF1A1446),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
