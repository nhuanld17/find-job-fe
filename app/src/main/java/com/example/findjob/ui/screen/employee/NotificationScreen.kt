package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.findjob.R
import com.example.findjob.ui.components.EmployeeBottomBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

@Composable
fun NotificationScreen(navController: NavController) {
    // Fake data
    val notifications = remember {
        listOf(
            NotificationData(
                avatar = "https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg",
                title = "Application sent",
                content = "Applications for Google companies have entered for company review",
                time = "25 minutes ago",
                isHighlight = true
            ),
            NotificationData(
                avatar = "https://cdn-icons-png.flaticon.com/512/2111/2111463.png",
                title = "Application sent",
                content = "Applications for Dribbble companies have entered for company review",
                time = "45 minutes ago"
            ),
            NotificationData(
                avatar = "https://cdn-icons-png.flaticon.com/512/733/733579.png",
                title = "Application sent",
                content = "Applications for Twitter companies have entered for company review",
                time = "5 Hours ago"
            ),
            NotificationData(
                avatar = "https://cdn-icons-png.flaticon.com/512/732/732229.png",
                title = "Application sent",
                content = "Applications for Apple companies have entered for company review",
                time = "1 Day ago"
            ),
            NotificationData(
                avatar = "https://cdn-icons-png.flaticon.com/512/733/733547.png",
                title = "Application sent",
                content = "Applications for Facebook companies have entered for company review",
                time = "12 February 2022"
            )
        )
    }
    val hasNotification = notifications.isNotEmpty()
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF9F9F9))) {
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
            if (hasNotification) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp)
                ) {
                    notifications.forEachIndexed { idx, noti ->
                        NotificationCard(
                            data = noti,
                            onClick = { navController.navigate("notificationDetail/1") },
                            onDelete = { /* TODO: handle delete */ }
                        )
                        if (idx < notifications.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            } else {
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
        }
        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// Data class for notification
data class NotificationData(
    val avatar: String,
    val title: String,
    val content: String,
    val time: String,
    val isHighlight: Boolean = false
)

@Composable
fun NotificationCard(
    data: NotificationData,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (data.isHighlight) Color(0xFFEDEAFF) else Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(data.avatar),
                contentDescription = "Avatar",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF23235B)
                )
                Text(
                    text = data.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF23235B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = data.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFBDBDBD),
                        modifier = Modifier.weight(1f)
                    )
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