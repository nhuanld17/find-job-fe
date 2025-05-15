package com.example.findjob.ui.components.form

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.findjob.ui.components.bottomSheet.SingleChoiceBottomSheet
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.findjob.viewmodel.ProfileRecruiterState
import com.example.findjob.viewmodel.RecruiterProfileViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalFocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileForm(
    viewModel: RecruiterProfileViewModel = hiltViewModel(),
    onSave: (about: String, website: String) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsState()
    val profile by viewModel.profile.collectAsState()
    
    var about by remember { mutableStateOf(profile?.about ?: "") }
    var website by remember { mutableStateOf(profile?.website ?: "") }
    var industry by remember { mutableStateOf(profile?.industry ?: "") }
    var location by remember { mutableStateOf(profile?.location ?: "") }
    var since by remember { mutableStateOf(profile?.since ?: "") }
    var specialization by remember { mutableStateOf(profile?.specialization ?: "") }


    // 63 tỉnh thành Việt Nam
    val provinces = listOf(
        "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "TP Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
    )
    var showLocationSheet by remember { mutableStateOf(false) }
    val locationSheetState = androidx.compose.material3.rememberModalBottomSheetState()

    // Date picker for Since
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val focusManager = LocalFocusManager.current

    // Update state when profile changes
    LaunchedEffect(profile) {
        profile?.let {
            about = it.about ?: ""
            website = it.website ?: ""
            industry = it.industry ?: ""
            location = it.location ?: ""
            since = it.since ?: ""
            specialization = it.specialization ?: ""
        }
    }

    // Show success dialog
    when (state) {
        is ProfileRecruiterState.UpdateSuccess -> {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                viewModel.getProfile()
            }
            AlertDialog(
                onDismissRequest = { },
                title = { 
                    Text(
                        "Success",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF1A1444)
                    )
                },
                text = { 
                    Text(
                        "Profile updated successfully",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF666666)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {viewModel.getProfile() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1444)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
        is ProfileRecruiterState.Error -> {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                viewModel.getProfile()
            }
            AlertDialog(
                onDismissRequest = { },
                title = { 
                    Text(
                        "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFFE53935)
                    )
                },
                text = { 
                    Text(
                        (state as ProfileRecruiterState.Error).message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF666666)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {viewModel.getProfile() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
        else -> { }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE5E5EF), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // About us section
                Text(
                    text = "About us",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1A1444),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = about,
                    onValueChange = { about = it },
                    placeholder = { Text("Enter about us...", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Website
                Text(
                    text = "Website",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1A1444),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    placeholder = { Text("https://...", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Industry
                Text(
                    text = "Industry",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1A1444),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = industry,
                    onValueChange = { industry = it },
                    placeholder = { Text("Enter Industry", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Location and Since in one row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1A1444),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            placeholder = { Text("Select location", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showLocationSheet = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1A1444),
                                unfocusedBorderColor = Color(0xFFE5E5EF),
                                cursorColor = Color(0xFF1A1444)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            enabled = false,
                            maxLines = 1
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Since",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1A1444),
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        OutlinedTextField(
                            value = since,
                            onValueChange = { since = it },
                            placeholder = { Text("Select date", style = MaterialTheme.typography.bodyMedium) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDatePicker = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF1A1444),
                                unfocusedBorderColor = Color(0xFFE5E5EF),
                                cursorColor = Color(0xFF1A1444)
                            ),
                            textStyle = MaterialTheme.typography.bodyMedium,
                            enabled = false,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Specialization
                Text(
                    text = "Specialization",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1A1444),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    placeholder = { Text("Enter your Specialization", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444)
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Save button
                Button(
                    onClick = {
                        viewModel.updateProfile(
                            about = about,
                            website = website,
                            industry = industry,
                            location = location,
                            since = since,
                            specialization = specialization
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1444))
                ) {
                    if (state is ProfileRecruiterState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "SAVE",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    // Location bottom sheet
    SingleChoiceBottomSheet(
        showSheet = showLocationSheet,
        onDismiss = { showLocationSheet = false },
        sheetState = locationSheetState,
        title = "Choose Location",
        description = "Select your location",
        options = provinces,
        selectedOption = location,
        onOptionSelected = { location = it },
//        modifier = Modifier.heightIn(max = 400.dp)
    )

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            since = dateFormat.format(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = Color(0xFF1A1444))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel", color = Color(0xFF666666))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
