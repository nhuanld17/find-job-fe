package com.example.findjob.ui.screen.employee

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.findjob.const.VietnamProvinces
import com.example.findjob.data.model.response.DateOfBirth
import com.example.findjob.data.model.response.EmployeeProfileDTO
import com.example.findjob.utils.InfoManager
import com.example.findjob.viewmodel.EmployeeProfileViewModel
import com.example.findjob.viewmodel.ProfileImageState
import com.example.findjob.viewmodel.ProfileState
import com.example.findjob.viewmodel.UploadState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeProfileScreen(
    navController: NavController,
    viewModel: EmployeeProfileViewModel = hiltViewModel(),
    infoManager: InfoManager,
) {
    // State lưu uri ảnh đại diện
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarUrl by remember { mutableStateOf<String?>(null) }

    // Collect upload state
    val uploadState by viewModel.uploadState.collectAsState()

    // Launcher để chọn ảnh từ gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            avatarUri = uri
            viewModel.uploadImage(uri)
        }
    }

    // State cho ngày sinh
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }
    
    // State cho dropdown
    var expandedDay by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }
    
    // Tạo danh sách ngày, tháng, năm
    val days = (1..31).map { it.toString().padStart(2, '0') }
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val years = (1900..2024).map { it.toString() }
    
    // State cho location
    var location by remember { mutableStateOf("") }
    var expandedLocation by remember { mutableStateOf(false) }
    
    // State cho các trường thông tin
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    
    // State cho mật khẩu
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // State cho dialog
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isPasswordDialog by remember { mutableStateOf(false) }

    // Collect state từ ViewModel
    val profileState by viewModel.state.collectAsState()
    val profileImageState by viewModel.profileImageState.collectAsState()

    // Gọi API khi màn hình được tạo
    LaunchedEffect(Unit) {
        viewModel.getProfile()
        viewModel.getProfileImage()
    }

    // Xử lý state từ API
    LaunchedEffect(profileState) {
        when (profileState) {
            is ProfileState.Success -> {
                val profile = (profileState as ProfileState.Success).profile
                fullName = profile.fullName ?: ""
                email = profile.email ?: ""
                phoneNumber = profile.phoneNumber ?: ""
                gender = when (profile.gender?.uppercase()) {
                    "MALE" -> "Male"
                    "FEMALE" -> "Female"
                    else -> ""
                }
                location = profile.location ?: ""
                
                // Xử lý dateOfBirth an toàn hơn
                profile.dateOfBirth?.let { dob ->
                    day = dob.day?.toString() ?: ""
                    month = dob.month?.toString() ?: ""
                    year = dob.year?.toString() ?: ""
                }

                // Hiển thị dialog thành công cho update profile
                if (showDialog && !isPasswordDialog) {
                    dialogMessage = "Profile updated successfully!"
                    isSuccess = true
                }
            }
            is ProfileState.Error -> {
                // Hiển thị dialog lỗi cho cả profile update và password change
                if (showDialog) {
                    dialogMessage = (profileState as ProfileState.Error).message
                    isSuccess = false
                    // Nếu là lỗi đổi mật khẩu, giữ lại giá trị mật khẩu để người dùng có thể sửa
                    if (isPasswordDialog) {
                        // Không reset các trường mật khẩu khi có lỗi
                    } else {
                        // Reset các trường profile khi có lỗi
                        viewModel.getProfile()
                    }
                }
            }
            is ProfileState.PasswordChanged -> {
                // Hiển thị dialog đổi mật khẩu thành công
                isPasswordDialog = true
                dialogMessage = "Password changed successfully!"
                isSuccess = true
                showDialog = true
                // Reset các trường mật khẩu
                currentPassword = ""
                newPassword = ""
                confirmPassword = ""
            }
            else -> {}
        }
    }

    // Xử lý upload state
    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                avatarUrl = (uploadState as UploadState.Success).imageUrl
                showDialog = true
                dialogMessage = "Avatar updated successfully!"
                isSuccess = true
                // Refresh profile để lấy thông tin mới nhất
                viewModel.getProfile()
            }
            is UploadState.Error -> {
                showDialog = true
                dialogMessage = (uploadState as UploadState.Error).message
                isSuccess = false
            }
            else -> {}
        }
    }

    // Hàm validate date
    fun validateDate(d: String, m: String, y: String): Boolean {
        if (d.isEmpty() || m.isEmpty() || y.isEmpty()) return true
        
        val dayNum = d.toIntOrNull() ?: return false
        val monthNum = m.toIntOrNull() ?: return false
        val yearNum = y.toIntOrNull() ?: return false
        
        if (yearNum < 1900 || yearNum > 2024) {
            dateError = "Năm phải từ 1900 đến 2024"
            return false
        }
        
        if (monthNum < 1 || monthNum > 12) {
            dateError = "Tháng phải từ 1 đến 12"
            return false
        }
        
        val daysInMonth = when (monthNum) {
            2 -> if (yearNum % 4 == 0 && (yearNum % 100 != 0 || yearNum % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        
        if (dayNum < 1 || dayNum > daysInMonth) {
            dateError = "Ngày không hợp lệ"
            return false
        }
        
        dateError = null
        return true
    }

    // Hàm validate password
    fun validatePassword(): Boolean {
        if (newPassword != confirmPassword) {
            passwordError = "Mật khẩu mới và xác nhận mật khẩu không khớp"
            return false
        }
        if (newPassword.length < 6) {
            passwordError = "Mật khẩu mới phải có ít nhất 6 ký tự"
            return false
        }
        passwordError = null
        return true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // Background gradient top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF4B3FAE), Color(0xFF6C63FF))
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {}

        // Hiển thị loading
        if (profileState is ProfileState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        // Hiển thị error
        if (profileState is ProfileState.Error) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (profileState as ProfileState.Error).message,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, bottom = 130.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Logout",
                    tint = Color.White,
                    modifier = Modifier
                        .graphicsLayer(rotationZ = 180f)
                        .clickable {
                            infoManager.clearTokens()
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Avatar
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { launcher.launch("image/*") }
            ) {
                when (profileImageState) {
                    is ProfileImageState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                    is ProfileImageState.Success -> {
                        AsyncImage(
                            model = (profileImageState as ProfileImageState.Success).imageUrl,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    is ProfileImageState.Error -> {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Default Profile",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            infoManager.getName()?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Nút chọn ảnh
            TextButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Text("Change image", color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Form
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Fullname") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Date of birth",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Ngày
                        OutlinedTextField(
                            value = day,
                            onValueChange = { input ->
                                if (input.length <= 2 && input.all { it.isDigit() }) {
                                    day = input
                                    validateDate(day, month, year)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            placeholder = { Text("DD") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4B3FAE),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        
                        // Tháng
                        OutlinedTextField(
                            value = month,
                            onValueChange = { input ->
                                if (input.length <= 2 && input.all { it.isDigit() }) {
                                    month = input
                                    validateDate(day, month, year)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            placeholder = { Text("MM") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4B3FAE),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        
                        // Năm
                        OutlinedTextField(
                            value = year,
                            onValueChange = { input ->
                                if (input.length <= 4 && input.all { it.isDigit() }) {
                                    year = input
                                    validateDate(day, month, year)
                                }
                            },
                            modifier = Modifier.weight(1.5f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            placeholder = { Text("YYYY") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4B3FAE),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                    }
                    if (dateError != null) {
                        Text(
                            text = dateError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Gender", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (gender.equals("Male", ignoreCase = true)) Color(0xFFFFF6E5) else Color(0xFFF3F3F3))
                                .clickable { gender = "Male" }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = gender.equals("Male", ignoreCase = true),
                                onClick = { gender = "Male" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                            )
                            Text("Male", color = Color(0xFF1A1446))
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (gender.equals("Female", ignoreCase = true)) Color(0xFFFFF6E5) else Color(0xFFF3F3F3))
                                .clickable { gender = "Female" }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = gender.equals("Female", ignoreCase = true),
                                onClick = { gender = "Female" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF9900))
                            )
                            Text("Female", color = Color(0xFF1A1446))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Phone number") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedLocation,
                        onExpandedChange = { expandedLocation = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Location") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLocation) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF4B3FAE),
                                unfocusedBorderColor = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedLocation,
                            onDismissRequest = { expandedLocation = false }
                        ) {
                            VietnamProvinces.provinces.forEach { province ->
                                DropdownMenuItem(
                                    text = { Text(province) },
                                    onClick = {
                                        location = province
                                        expandedLocation = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val dateOfBirth = DateOfBirth(
                                day = day.toIntOrNull() ?: 0,
                                month = month.toIntOrNull() ?: 0,
                                year = year.toIntOrNull() ?: 0
                            )
                            
                            val profile = EmployeeProfileDTO(
                                fullName = fullName,
                                email = email,
                                phoneNumber = phoneNumber,
                                gender = gender.uppercase(),
                                location = location,
                                dateOfBirth = dateOfBirth
                            )
                            showDialog = true
                            viewModel.updateProfile(profile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1446))
                    ) {
                        Text("SAVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            // Box mật khẩu
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = Color.White,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Change Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A1446)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        label = { Text("Current Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { 
                            newPassword = it
                            validatePassword()
                        },
                        label = { Text("New Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { 
                            confirmPassword = it
                            validatePassword()
                        },
                        label = { Text("Confirm New Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password"
                            )
                        }
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (validatePassword()) {
                                showDialog = true
                                isPasswordDialog = true
                                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1446)),
                        enabled = currentPassword.isNotEmpty() && 
                                 newPassword.isNotEmpty() && 
                                 confirmPassword.isNotEmpty() && 
                                 passwordError == null
                    ) {
                        Text("CHANGE PASSWORD", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }

        // Dialog hiển thị kết quả
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showDialog = false
                    if (isPasswordDialog) {
                        isPasswordDialog = false
                    }
                },
                title = { Text(if (isSuccess) "Success" else "Error") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            showDialog = false
                            if (isSuccess) {
                                if (!isPasswordDialog) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
