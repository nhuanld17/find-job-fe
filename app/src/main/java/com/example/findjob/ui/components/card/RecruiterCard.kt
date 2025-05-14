package com.example.findjob.ui.components.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.findjob.data.model.response.ListJobResponse

@Composable
fun RecruiterCard(
    navController: NavController,
    job: ListJobResponse,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
            .background(Color.White, shape = RoundedCornerShape(30.dp))
            .border(width = 1.dp, color = Color(0xFFE5E6EB), shape = RoundedCornerShape(30.dp)),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFD6D6FF), shape = CircleShape),
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
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.FillBounds
                        )
                    } else {
                        Image(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Company Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = job.title ?: "No Title",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = Color(0xFF1A1D1F),
                    )
                    Text(
                        text = "${job.nameCompany ?: "Unknown Company"} · ${job.location ?: "No Location"}",
                        fontSize = 10.sp,
                        color = Color(0xFF7C8493),
                        modifier = Modifier.padding(top = 1.dp),
                        lineHeight = 1.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = job.salary ?: "Salary not specified",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1D1F)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Tag(text = job.position ?: "No Position", fontSize = 9)
                Spacer(modifier = Modifier.width(6.dp))
                Tag(text = job.type ?: "No Type", fontSize = 9)
                Spacer(modifier = Modifier.width(6.dp))
                CurriculumButton(navController, job.id)
            }
        }
    }
}

@Composable
private fun Tag(text: String, bgColor: Color = Color(0xFFF3F4F6), textColor: Color = Color(0xFF7C8493), fontSize: Int = 14) {
    Box(
        modifier = Modifier
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = textColor, fontSize = fontSize.sp)
    }
}

@Composable
private fun CurriculumButton(navController: NavController, id: Int) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFFE3DD), shape = RoundedCornerShape(8.dp))
            .clickable { navController.navigate("listCurriculum/${id}") }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "See Cv",
            color = Color(0xFFED7457),
            fontSize = 9.sp
        )
    }
}