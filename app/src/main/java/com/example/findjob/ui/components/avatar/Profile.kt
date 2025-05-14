package com.example.findjob.ui.components.avatar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findjob.R
import com.example.findjob.utils.InfoManager
import com.example.findjob.viewmodel.RecruiterProfileViewModel
import com.example.findjob.viewmodel.ProfileAvatarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    navController: NavController,
    infoManager: InfoManager,
    viewModel: RecruiterProfileViewModel
) {
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    val avatarState by viewModel.avatarState.collectAsState()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            avatarUri = uri
            viewModel.uploadAndChangeAvatar(uri)
        }
    }
    var showLogoutSheet by remember { mutableStateOf(false) }
    val logoutSheetState = rememberModalBottomSheetState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(top = 30.dp)
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.navigate("recruiterHome")
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Logout",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        showLogoutSheet = true
                    }
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                when {
                    avatarState is ProfileAvatarState.Success -> {
                        val url = (avatarState as ProfileAvatarState.Success).url
                        Image(
                            painter = rememberAsyncImagePainter(url),
                            contentDescription = null,
                            modifier = Modifier.size(74.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    avatarUri != null -> {
                        Image(
                            painter = rememberAsyncImagePainter(avatarUri),
                            contentDescription = null,
                            modifier = Modifier.size(74.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier.size(74.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = "Orlando Diggs",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
            Text(
                text = "California, USA",
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 1.sp
            )
            Spacer(modifier = Modifier.height(1.dp))
            Button(
                onClick = { launcher.launch("image/*") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.18f)),
                modifier = Modifier
                    .height(48.dp)
                    .width(150.dp)
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = "Change image",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    lineHeight = 5.sp
                )
            }
        }
    }

    // Bottom sheet xác nhận logout
    if (showLogoutSheet) {
        ModalBottomSheet(
            onDismissRequest = { showLogoutSheet = false },
            sheetState = logoutSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Do you want to logout?", color = Color(0xFF23235B), fontSize = 15.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showLogoutSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Cancel", color = Color(0xFF23235B))
                    }
                    Button(
                        onClick = {
                            infoManager.clearTokens()
                            showLogoutSheet = false
                            navController.navigate("login") { popUpTo(0) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23235B))
                    ) {
                        Text("Logout", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}