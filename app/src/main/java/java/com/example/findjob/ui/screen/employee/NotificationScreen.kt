package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findjob.R
import com.example.findjob.ui.components.EmployeeBottomBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.findjob.viewmodel.NotificationViewModel
import com.example.findjob.data.model.response.NotificationResponse
import kotlinx.coroutines.delay

@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() },
                    colorFilter = ColorFilter.tint(Color(0xFF23235B))
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Read all",
                    color = Color(0xFFFFB200),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { /* TODO: Read all */ }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF23235B),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading && notifications.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFFFB200))
                    }
                }
                notifications.isEmpty() -> {
                    // No notification
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No notifications",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF23235B),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "You have no notifications at this time\nthank you",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6F6F6F),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Image(
                            painter = painterResource(id = R.drawable.bell),
                            contentDescription = "Bell",
                            modifier = Modifier.size(160.dp)
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                    ) {
                        notifications.forEachIndexed { idx, noti ->
                            NotificationCard(
                                data = noti,
                                onClick = { 
                                    navController.currentBackStackEntry?.savedStateHandle?.set("message", noti.message)
                                    navController.navigate("notificationDetail") 
                                },
                                onDelete = { viewModel.deleteNotification(noti.idNotification) }
                            )
                            if (idx < notifications.size - 1) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(130.dp))
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun NotificationCard(
    data: NotificationResponse,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(data.imageLink),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Application sent",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF23235B)
                )
                Text(
                    text = "Applications for ${data.nameCompany} companies have entered for company review",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF23235B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Delete",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFF4B4B),
                        modifier = Modifier.clickable { onDelete() }
                    )
                }
            }
        }
    }
}
