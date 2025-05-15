package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findjob.R

@Composable
fun NotificationDetailScreen(
    navController: NavController
) {
    val message = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<String>("message")

    Box(
        Modifier.fillMaxSize().statusBarsPadding().background(Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                    text = "Notification Details",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF23235B)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = message ?: "No message",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6F6F6F),
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}