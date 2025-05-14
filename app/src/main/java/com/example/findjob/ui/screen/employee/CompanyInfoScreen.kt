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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.findjob.R
import com.example.findjob.data.model.common.JobIntroDTO
import com.example.findjob.data.model.response.NewestJobResponse
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.CompanyInfoViewModel
import com.example.findjob.viewmodel.HomeViewModel

@Composable
fun CompanyInfoScreen(
    navController: NavController,
    email: String,
    viewModel: CompanyInfoViewModel = hiltViewModel()
) {
    val companyInfo by viewModel.companyInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(email) {
        viewModel.getCompanyInfo(email)
    }

    var selectedTab by remember { mutableStateOf(0) } // 0: About us, 1: Jobs

    Box(
        modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error ?: "An error occurred", color = Color.Red)
            }
        } else {
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

                Box(
                    modifier = Modifier.fillMaxWidth(),
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
                            painter = rememberAsyncImagePainter(companyInfo?.imageLink ?: ""),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape), // Quan trọng để ảnh tròn luôn
                            contentScale = ContentScale.Crop // Đảm bảo ảnh lấp đầy và không méo
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
                        text = companyInfo?.recruiterName ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = Color(0xFF23235B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = companyInfo?.location ?: "", color = Color(0xFF23235B), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Tab buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TabButton(
                        text = "About us",
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = "Jobs",
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (selectedTab == 0) {
                    // About us content
                    Text(
                        text = "About Company",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = Color(0xFF23235B),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = companyInfo?.about ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF23235B),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    // Company details
                    CompanyDetail(label = "Website", value = companyInfo?.website ?: "", link = true)
                    CompanyDetail(label = "Industry", value = companyInfo?.industry ?: "")
                    CompanyDetail(label = "Location", value = companyInfo?.location ?: "")
                    CompanyDetail(label = "Since", value = companyInfo?.since ?: "")
                    Spacer(modifier = Modifier.height(150.dp))

                } else {
                    // Jobs tab content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        companyInfo?.jobIntroDTOs?.forEach { job ->
                            // Add your job item composable here
                            JobCardInfo(navController, job, companyInfo!!.recruiterName, companyInfo!!.location, companyInfo!!.imageLink)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    androidx.compose.material3.Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFFFFB200) else Color.Transparent,
        shadowElevation = 0.dp,
        modifier = modifier
            .padding(10.dp)
            .height(60.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = if (selected) Color.White else Color(0xFF23235B),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun CompanyDetail(label: String, value: String, link: Boolean = false) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            color = Color(0xFF23235B)
        )
        if (link) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFFB200)
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF23235B)
            )
        }
    }
}

@Composable
fun JobCardInfo(
    navController: NavController,
    job: JobIntroDTO,
    companyName: String,
    location: String,
    imageLink: String,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var isSaved by remember { mutableStateOf(job.isSaved) }
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
                        if (imageLink.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(data = imageLink)
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
                                    .clip(CircleShape) // Tùy chọn, giúp icon đồng nhất hình tròn
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = job.title ?: "No Title",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1446)
                        )
                        Text(
                            text = "${companyName ?: "Unknown"} · ${location ?: "Unknown"}",
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
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = job.position ?: "Unknown",
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
                    onClick = {navController.navigate("jobDescription/${job.id}") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFE1B8)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
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