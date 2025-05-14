package com.example.findjob.ui.screen.employee

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.findjob.R
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.UploadCVState
import com.example.findjob.viewmodel.UploadCVViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadCVScreen(
    navController: NavController,
    viewModel: UploadCVViewModel = hiltViewModel(),
    jobId: Int
) {
    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    var fileSize by remember { mutableStateOf("") }
    var fileDate by remember { mutableStateOf("") }
    val uploadState by viewModel.uploadState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Kiểm tra loại file
            val mimeType = context.contentResolver.getType(uri)
            if (mimeType == "application/pdf") {
                fileUri = uri
                // Lấy thông tin file
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                        if (nameIndex >= 0) fileName = it.getString(nameIndex) ?: ""
                        if (sizeIndex >= 0) {
                            val size = it.getLong(sizeIndex)
                            fileSize = if (size > 1024) "${size / 1024} Kb" else "$size B"
                        }
                        // Lấy ngày giờ hiện tại
                        val sdf = SimpleDateFormat("dd MMM yyyy 'at' HH:mm a", Locale.getDefault())
                        fileDate = sdf.format(Date())
                    }
                }
            }
        }
    }

    // Xử lý trạng thái upload
    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadCVState.Success -> {
                snackbarHostState.showSnackbar(
                    message = "Upload CV successfully!",
                    duration = SnackbarDuration.Short
                )
                navController.popBackStack()
            }
            is UploadCVState.Error -> {
                snackbarHostState.showSnackbar(
                    message = (uploadState as UploadCVState.Error).message,
                    duration = SnackbarDuration.Short
                )
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { navController.popBackStack() },
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD6F3FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg"),
                            contentDescription = "Avatar",
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Title and info
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "UI/UX Designer",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF23235B)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Google",
                            color = Color(0xFF23235B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "  •  ",
                            color = Color(0xFF23235B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "California",
                            color = Color(0xFF23235B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "  •  ",
                            color = Color(0xFF23235B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "1 day ago",
                            color = Color(0xFF23235B),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                // Upload CV section
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Upload CV",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF23235B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Add your CV/Resume to apply for a job",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6F6F6F)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    if (fileUri == null) {
                        // Chưa có file
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFBDBDBD),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                )
                                .clickable { launcher.launch("application/pdf") },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_upload),
                                    contentDescription = "Upload",
                                    modifier = Modifier.size(28.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFF6C63FF))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Upload CV/Resume (PDF only)",
                                    color = Color(0xFF6C63FF),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Only PDF files are allowed",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6F6F6F)
                        )
                    } else {
                        // Đã có file
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9F9F9), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFBDBDBD),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_pdf),
                                    contentDescription = "PDF",
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = fileName,
                                        color = Color(0xFF23235B),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "$fileSize · $fileDate",
                                        color = Color(0xFF6F6F6F),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    fileUri = null
                                    fileName = ""
                                    fileSize = ""
                                    fileDate = ""
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_delete),
                                    contentDescription = "Remove",
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(Color(0xFFFF4B4B))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Remove file",
                                    color = Color(0xFFFF4B4B),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
                // APPLY NOW button
                Button(
                    onClick = { 
                        fileUri?.let { uri ->
                            viewModel.uploadCV(uri, jobId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(56.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23235B)),
                    enabled = fileUri != null && uploadState !is UploadCVState.Loading
                ) {
                    if (uploadState is UploadCVState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "APPLY NOW",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            EmployeeBottomBar(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}