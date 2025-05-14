package com.example.findjob.ui.screen.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.card.CvCard
import com.example.findjob.viewmodel.ListCurriculumViewModel

@Composable
fun ListCurriculumSceen(
    navController: NavController,
    id: String,
    viewModel: ListCurriculumViewModel = hiltViewModel()
) {
    val cvList by viewModel.cvList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(id) {
        viewModel.getCvList(id.toInt())
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Curriculum",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                color = Color(0xFF23235B)
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = error ?: "Unknown error", color = Color.Red)
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp)
                    ) {
                        items(cvList) { cv ->
                            CvCard(
                                name = cv.nameEmployee,
                                location = cv.location,
                                cvId = cv.idCV,
                                cvLink = cv.cvLink,
                                navController = navController,
                                onAccept = { /* Handle accept */ },
                                onReject = { /* Handle reject */ }
                            )
                        }
                    }
                }
            }
        }

        RecruiterBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}