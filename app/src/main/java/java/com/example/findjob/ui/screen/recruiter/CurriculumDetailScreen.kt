package com.example.findjob.ui.screen.recruiter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.R
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.viewmodel.CurriculumDetailViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumDetailScreen(
    navController: NavController,
    cvId: Int,
    cvLink: String,
    viewModel: CurriculumDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val encodedUrl = URLEncoder.encode(cvLink, StandardCharsets.UTF_8.toString())
    val finalUrl = "https://docs.google.com/gview?embedded=true&url=$encodedUrl"
    val downloadUrl = cvLink.replace("/upload/", "/upload/fl_attachment/")
    
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()

    LaunchedEffect(isSuccess) {
        if (isSuccess == true) {
            delay(1500) // Hiển thị thông báo thành công trong 1.5 giây
            viewModel.resetState()
            if (error == null) { // Nếu là reject thành công
                navController.popBackStack()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("CV Detail") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                                context.startActivity(intent)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.download),
                                contentDescription = "Download CV",
                                tint = Color(0xFF23235B)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFF9F9F9),
                        titleContentColor = Color(0xFF23235B)
                    )
                )
            },
            bottomBar = {
                RecruiterBottomBar(navController = navController)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9F9F9))
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        AndroidView(
                            factory = { context ->
                                WebView(context).apply {
                                    settings.apply {
                                        javaScriptEnabled = true
                                        loadWithOverviewMode = true
                                        useWideViewPort = true
                                        builtInZoomControls = true
                                        displayZoomControls = false
                                    }
                                    webViewClient = WebViewClient()
                                    loadUrl(finalUrl)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.acceptCv(cvId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF57ED74)
                            ),
                            enabled = !isLoading
                        ) {
                            Text("Accept")
                        }
                        Button(
                            onClick = { viewModel.rejectCv(cvId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFED7457)
                            ),
                            enabled = !isLoading
                        ) {
                            Text("Reject")
                        }
                    }
                }
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            // Success overlay
            if (isSuccess == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(280.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Success!",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF23235B)
                            )
                        }
                    }
                }
            }

            // Error overlay
            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .width(280.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = error ?: "Error occurred",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF23235B),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.resetState() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF23235B)
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
} 