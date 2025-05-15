package com.example.findjob.ui.components.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import androidx.compose.foundation.clickable

@Composable
fun Avatar(
    imageUrl: String?,
    name: String?,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Hello $name",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = Color(0xFF140A43),
            modifier = Modifier.padding(end = 24.dp)
        )
        Spacer(modifier = Modifier.weight(2f))
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFD6D6FF), CircleShape)
                .clickable { navController.navigate("recruiterProfile") },
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(
                model = imageUrl ?: "https://randomuser.me/api/portraits/men/1.jpg"
            )
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.matchParentSize().clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}