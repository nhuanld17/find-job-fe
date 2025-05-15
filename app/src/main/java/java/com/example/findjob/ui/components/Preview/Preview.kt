package com.example.findjob.ui.components.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Preview() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Remote Job Card
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(170.dp)
                    .background(Color(0xFFB3E6FF), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Icon tạm
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_search),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("44.5k", fontSize = 28.sp, color = Color(0xFF1A1D1F), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text("Remote Job", fontSize = 16.sp, color = Color(0xFF1A1D1F))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                // Full Time Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFFD1C4FD), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("66.8k", fontSize = 24.sp, color = Color(0xFF1A1D1F), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text("Full Time", fontSize = 16.sp, color = Color(0xFF1A1D1F))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Part Time Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFFFFE0B2), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("38.9k", fontSize = 24.sp, color = Color(0xFF1A1D1F), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text("Part Time", fontSize = 16.sp, color = Color(0xFF1A1D1F))
                    }
                }
            }
        }
    }
}