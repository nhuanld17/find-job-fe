package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.findjob.const.VietnamProvinces
import com.example.findjob.data.model.request.Filter
import com.example.findjob.data.model.response.JobSearchResponse
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.SaveJobState
import com.example.findjob.viewmodel.SearchJobState
import com.example.findjob.viewmodel.SearchJobViewModel
import java.nio.file.DirectoryStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchJobScreen(
    navController: NavController,
    viewModel: SearchJobViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    var searchTitle by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            EmployeeBottomBar(navController = navController)
        },
        containerColor = Color(0xFFF8F8F8)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchHeader(
                navController = navController,
                onFilterClick = { showFilterSheet = true },
                searchTitle = searchTitle,
                onSearchTitleChange = { searchTitle = it }
            )
            
            when (state) {
                is SearchJobState.Initial -> {
                    // Show initial state UI
                }
                is SearchJobState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SearchJobState.Success -> {
                    val jobs = (state as SearchJobState.Success).jobs
                    if (jobs.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No jobs found",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(jobs) { job ->
                                JobCard(
                                    navController = navController,
                                    job = job,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
                is SearchJobState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (state as SearchJobState.Error).message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        if (showFilterSheet) {
            FilterBottomSheet(
                onDismiss = { showFilterSheet = false },
                onApply = { filter ->
                    viewModel.searchWith(filter.copy(title = searchTitle))
                    showFilterSheet = false
                },
                searchTitle = searchTitle
            )
        }
    }
}

@Composable
fun SearchHeader(
    navController: NavController,
    onFilterClick: () -> Unit,
    searchTitle: String,
    onSearchTitleChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF221A4C), Color(0xFF2C1C66))
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = searchTitle,
                    onValueChange = onSearchTitleChange,
                    placeholder = { Text("Search jobs...", color = Color.Gray) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApply: (Filter) -> Unit,
    searchTitle: String
) {
    var selectedLocation by remember { mutableStateOf("All") }
    val locationOptions = VietnamProvinces.provincesFilter
    var expanded by remember { mutableStateOf(false) }
    var selectedSalary by remember { mutableStateOf("All") }
    val salaryOptions = listOf("All", "< $500", "$500-$1000", "$1000-$2000", "> $2000")
    var selectedPosition by remember { mutableStateOf("All") }
    val positionLevels = listOf("All", "Junior", "Senior", "Leader", "Manager")
    var selectedExperience by remember { mutableStateOf("All") }
    val experienceOptions = listOf(
        "All", "0-1 years", "1-3 years", "3-5 years", "5+ years"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Filter", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Location Section
                Text("Location", fontWeight = FontWeight.SemiBold)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedLocation,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 300.dp)
                    ) {
                        locationOptions.forEach { province ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedLocation = province
                                    expanded = false
                                },
                                text = { Text(province) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Position Section
                Text("Position", fontWeight = FontWeight.SemiBold)
                positionLevels.forEach { position ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPosition = position }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = selectedPosition == position, onClick = { selectedPosition = position })
                        Text(position)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Experience Section
                Text("Experience", fontWeight = FontWeight.SemiBold)
                experienceOptions.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedExperience = it }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = selectedExperience == it, onClick = { selectedExperience = it })
                        Text(it)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Salary Section
                Text("Salary", fontWeight = FontWeight.SemiBold)
                salaryOptions.forEach {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSalary = it }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(selected = selectedSalary == it, onClick = { selectedSalary = it })
                        Text(it)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedLocation = "All"
                        selectedSalary = "All"
                        selectedPosition = "All"
                        selectedExperience = "All"
                        expanded = false
                    },
                    border = BorderStroke(1.dp, Color(0xFFFF9100))
                ) {
                    Text("Reset", color = Color(0xFFFF9100))
                }

                Button(
                    onClick = {
                        val filter = Filter(
                            title = searchTitle,
                            location = selectedLocation,
                            position = selectedPosition,
                            experience = selectedExperience,
                            salary = selectedSalary
                        )
                        onApply(filter)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E0A57))
                ) {
                    Text("SEARCH", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun JobCard(
    navController: NavController,
    job: JobSearchResponse,
    viewModel: SearchJobViewModel
) {
    println("Rendering JobCard for job: ${job.jobTitle} with isSaved: ${job.saved}")
    val saveState by viewModel.saveState.collectAsState()

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                job.id?.let { jobId ->
                    navController.navigate("jobDescription/$jobId")
                }
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = job.jobTitle ?: "No Title",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1446)
                    )
                    Text(
                        text = "${job.companyName ?: "Unknown"} • ${job.location ?: "Unknown"}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (saveState !is SaveJobState.Loading) {
                                job.id?.let { jobId ->
                                    viewModel.saveJob(jobId)
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (saveState) {
                        is SaveJobState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = if (job.saved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (job.saved) "Unsave Job" else "Save Job",
                                tint = if (job.saved) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            }

            if (saveState is SaveJobState.Error) {
                Text(
                    text = (saveState as SaveJobState.Error).message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                job.jobPosition?.let {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(it, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
                job.jobType?.let {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(it, fontSize = 12.sp, color = Color.DarkGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Posted: ${job.createdAt?.let { 
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                    } ?: "Unknown"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = job.salary ?: "Not specified",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1446)
                )
            }
        }
    }
}
