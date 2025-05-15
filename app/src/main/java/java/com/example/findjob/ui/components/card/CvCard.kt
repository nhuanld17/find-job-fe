package com.example.findjob.ui.components.card

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun CvCard(
    name: String,
    location: String,
    cvId: Int,
    cvLink: String,
    navController: NavController,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(30.dp))
            .border(width = 1.dp, color = Color(0xFFE5E6EB), shape = RoundedCornerShape(30.dp))
            .clickable {
                val encodedUrl = URLEncoder.encode(cvLink, StandardCharsets.UTF_8.toString())
                navController.navigate("curriculumDetail/$cvId/$encodedUrl")
            }
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFD6D6FF), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color(0xFF6B6BFF),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = name,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = Color(0xFF1A1D1F),
                        )
                        Text(
                            text = location,
                            fontSize = 10.sp,
                            color = Color(0xFF7C8493),
                            modifier = Modifier.padding(top = 1.dp),
                            lineHeight = 1.sp
                        )
                    }
                }
            }
        }
    }
}