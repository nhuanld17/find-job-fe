package com.example.findjob.ui.screen.recruiter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.RecruiterBottomBar
import com.example.findjob.ui.components.bottomSheet.SingleChoiceBottomSheet
import com.example.findjob.viewmodel.JobPostState
import com.example.findjob.viewmodel.JobPostViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateJobScreen(
    navController: NavController,
    viewModel: JobPostViewModel = hiltViewModel()
) {
    var jobTitle by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    // Dropdown states
    val positionOptions = listOf("Junior", "Senior", "Leader", "Manager")
    var position by remember { mutableStateOf(positionOptions[0]) }
    var positionExpanded by remember { mutableStateOf(false) }
    val qualificationOptions = listOf("Bachelor", "Master", "PhD", "Other")
    var qualification by remember { mutableStateOf(qualificationOptions[0]) }
    var qualificationExpanded by remember { mutableStateOf(false) }
    val experienceOptions = listOf("0-1 years", "1-3 years", "3-5 years", "5+ years")
    var experience by remember { mutableStateOf(experienceOptions[0]) }
    var experienceExpanded by remember { mutableStateOf(false) }
    // Job Type Bottom Sheet
    val jobTypeOptions = listOf("Full time", "Part time", "Remote")
    var jobType by remember { mutableStateOf(jobTypeOptions[0]) }
    var showJobTypeSheet by remember { mutableStateOf(false) }
    val jobTypeSheetState = rememberModalBottomSheetState()
    // Work Type Bottom Sheet
    val workTypeOptions = listOf("On-site", "Hybrid", "Remote")
    var workType by remember { mutableStateOf(workTypeOptions[0]) }
    var showWorkTypeSheet by remember { mutableStateOf(false) }
    val workTypeSheetState = rememberModalBottomSheetState()
    val salaryOptions = listOf("< $500", "$500-$1000", "$1000-$2000", "> $2000")
    var salary by remember { mutableStateOf(salaryOptions[0]) }
    var salaryExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    // Position Bottom Sheet
    var showPositionSheet by remember { mutableStateOf(false) }
    val positionSheetState = rememberModalBottomSheetState()
    // Qualification Bottom Sheet
    var showQualificationSheet by remember { mutableStateOf(false) }
    val qualificationSheetState = rememberModalBottomSheetState()
    // Experience Bottom Sheet
    var showExperienceSheet by remember { mutableStateOf(false) }
    val experienceSheetState = rememberModalBottomSheetState()
    // Salary Bottom Sheet
    var showSalarySheet by remember { mutableStateOf(false) }
    val salarySheetState = rememberModalBottomSheetState()
    // Error states for validation
    var jobTitleError by remember { mutableStateOf<String?>(null) }
    var jobDescriptionError by remember { mutableStateOf<String?>(null) }
    var requirementsError by remember { mutableStateOf<String?>(null) }
    var positionError by remember { mutableStateOf<String?>(null) }
    var qualificationError by remember { mutableStateOf<String?>(null) }
    var experienceError by remember { mutableStateOf<String?>(null) }
    var salaryError by remember { mutableStateOf<String?>(null) }
    var jobTypeError by remember { mutableStateOf<String?>(null) }
    var workTypeError by remember { mutableStateOf<String?>(null) }
    // FocusRequester for each field
    val jobTitleFocusRequester = remember { FocusRequester() }
    val jobDescriptionFocusRequester = remember { FocusRequester() }
    val requirementsFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Thêm state cho ViewModel
    val jobPostState by viewModel.state.collectAsState()

    // Add expiration date state
    var expirationDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var expirationDateError by remember { mutableStateOf<String?>(null) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .background(Color(0xFFF9F9F9))
        .pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Your Job",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    fontSize = 24.sp
                ),
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 16.dp),
                color = Color(0xFF23235B)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Job title field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Job title", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    BasicTextField(
                        value = jobTitle,
                        onValueChange = {
                            jobTitle = it
                            if (jobTitleError != null && it.isNotBlank()) jobTitleError = null
                        },
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color(0xFF23235B), fontSize = 15.sp),
                        cursorBrush = SolidColor(Color(0xFF23235B)),
                        modifier = Modifier.fillMaxWidth().focusRequester(jobTitleFocusRequester)
                    )
                    if (jobTitleError != null) {
                        Text(jobTitleError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Job Description field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Job Description", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    BasicTextField(
                        value = jobDescription,
                        onValueChange = {
                            jobDescription = it
                            if (jobDescriptionError != null && it.isNotBlank()) jobDescriptionError = null
                        },
                        singleLine = false,
                        textStyle = LocalTextStyle.current.copy(color = Color(0xFF23235B), fontSize = 15.sp),
                        cursorBrush = SolidColor(Color(0xFF23235B)),
                        modifier = Modifier.fillMaxWidth().focusRequester(jobDescriptionFocusRequester)
                    )
                    if (jobDescriptionError != null) {
                        Text(jobDescriptionError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Requirements field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Requirements", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    BasicTextField(
                        value = requirements,
                        onValueChange = {
                            requirements = it
                            if (requirementsError != null && it.isNotBlank()) requirementsError = null
                        },
                        singleLine = false,
                        textStyle = LocalTextStyle.current.copy(color = Color(0xFF23235B), fontSize = 15.sp),
                        cursorBrush = SolidColor(Color(0xFF23235B)),
                        modifier = Modifier.fillMaxWidth().focusRequester(requirementsFocusRequester)
                    )
                    if (requirementsError != null) {
                        Text(requirementsError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Position field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showPositionSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Job position", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(position, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (positionError != null) {
                        Text(positionError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Qualification field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showQualificationSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Qualification", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(qualification, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (qualificationError != null) {
                        Text(qualificationError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Experience field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showExperienceSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Experience", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(experience, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (experienceError != null) {
                        Text(experienceError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Salary field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showSalarySheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Salary", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(salary, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (salaryError != null) {
                        Text(salaryError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Work Type field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showWorkTypeSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Type of workplace", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(workType, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (workTypeError != null) {
                        Text(workTypeError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }
            // Job Type field (Card style)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showJobTypeSheet = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Job Type", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(jobType, color = Color(0xFF23235B), fontSize = 15.sp)
                    if (jobTypeError != null) {
                        Text(jobTypeError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            // Card lay ngay het han
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Expiration Date", color = Color(0xFF23235B), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.weight(1f))
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = Color(0xFFFF9800))
                    }
                    Text(
                        text = expirationDate?.let { dateFormatter.format(it) } ?: "Select expiration date",
                        color = if (expirationDate == null) Color.Gray else Color(0xFF23235B),
                        fontSize = 15.sp
                    )
                    if (expirationDateError != null) {
                        Text(expirationDateError ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cập nhật phần hiển thị dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Job Post Status") },
                    text = {
                        when (jobPostState) {
                            is JobPostState.Success -> {
                                Text((jobPostState as JobPostState.Success).message)
                            }
                            is JobPostState.Error -> {
                                Text((jobPostState as JobPostState.Error).message)
                            }
                            is JobPostState.Loading -> {
                                CircularProgressIndicator()
                            }
                            else -> {
                                Column {
                                    Text("Job title: $jobTitle")
                                    Text("Job Description: $jobDescription")
                                    Text("Requirements: $requirements")
                                    Text("Position: $position")
                                    Text("Qualification: $qualification")
                                    Text("Experience: $experience")
                                    Text("Work Type: $workType")
                                    Text("Job Type: $jobType")
                                    Text("Salary: $salary")
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { 
                                showDialog = false
                                if (jobPostState is JobPostState.Success) {
                                    navController.navigateUp()
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            // Cập nhật phần Button CREATE JOB POST
            Button(
                onClick = {
                    // Validate all fields
                    var hasError = false
                    if (jobTitle.isBlank()) {
                        jobTitleError = "Please enter job title"
                        hasError = true
                        jobTitleFocusRequester.requestFocus()
                    }
                    if (jobDescription.isBlank()) {
                        jobDescriptionError = "Please enter job description"
                        if (!hasError) jobDescriptionFocusRequester.requestFocus()
                        hasError = true
                    }
                    if (requirements.isBlank()) {
                        requirementsError = "Please enter requirements"
                        if (!hasError) requirementsFocusRequester.requestFocus()
                        hasError = true
                    }
                    if (position.isBlank()) {
                        positionError = "Please select position"
                        hasError = true
                    }
                    if (qualification.isBlank()) {
                        qualificationError = "Please select qualification"
                        hasError = true
                    }
                    if (experience.isBlank()) {
                        experienceError = "Please select experience"
                        hasError = true
                    }
                    if (salary.isBlank()) {
                        salaryError = "Please select salary"
                        hasError = true
                    }
                    if (jobType.isBlank()) {
                        jobTypeError = "Please select job type"
                        hasError = true
                    }
                    if (workType.isBlank()) {
                        workTypeError = "Please select work type"
                        hasError = true
                    }
                    if (expirationDate == null) {
                        expirationDateError = "Please select expiration date"
                        hasError = true
                    }
                    if (!hasError) {
                        showDialog = true
                        viewModel.createJobPost(
                            title = jobTitle,
                            description = jobDescription,
                            requirement = requirements,
                            position = position,
                            qualification = qualification,
                            experience = experience,
                            type = jobType,
                            salary = salary,
                            workType = workType,
                            expirationDate = expirationDate?.let { dateFormatter.format(it) }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23235B))
            ) {
                Text("CREATE JOB POST", color = Color.White, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
        RecruiterBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Bottom Sheet for Position
        SingleChoiceBottomSheet(
            showSheet = showPositionSheet,
            onDismiss = { showPositionSheet = false },
            sheetState = positionSheetState,
            title = "Choose Job Position",
            description = "Select the job position you want",
            options = positionOptions,
            selectedOption = position,
            onOptionSelected = { position = it }
        )
        // Bottom Sheet for Job Type
        SingleChoiceBottomSheet(
            showSheet = showJobTypeSheet,
            onDismiss = { showJobTypeSheet = false },
            sheetState = jobTypeSheetState,
            title = "Choose Job Type",
            description = "Determine and choose the type of work according to what you want",
            options = jobTypeOptions,
            selectedOption = jobType,
            onOptionSelected = { jobType = it }
        )
        // Bottom Sheet for Qualification
        SingleChoiceBottomSheet(
            showSheet = showQualificationSheet,
            onDismiss = { showQualificationSheet = false },
            sheetState = qualificationSheetState,
            title = "Choose Qualification",
            description = "Select the qualification you want",
            options = qualificationOptions,
            selectedOption = qualification,
            onOptionSelected = { qualification = it }
        )
        // Bottom Sheet for Experience
        SingleChoiceBottomSheet(
            showSheet = showExperienceSheet,
            onDismiss = { showExperienceSheet = false },
            sheetState = experienceSheetState,
            title = "Choose Experience",
            description = "Select the experience you want",
            options = experienceOptions,
            selectedOption = experience,
            onOptionSelected = { experience = it }
        )
        // Bottom Sheet for Salary
        SingleChoiceBottomSheet(
            showSheet = showSalarySheet,
            onDismiss = { showSalarySheet = false },
            sheetState = salarySheetState,
            title = "Choose Salary",
            description = "Select the salary you want",
            options = salaryOptions,
            selectedOption = salary,
            onOptionSelected = { salary = it }
        )
        // Bottom Sheet for Work Type
        SingleChoiceBottomSheet(
            showSheet = showWorkTypeSheet,
            onDismiss = { showWorkTypeSheet = false },
            sheetState = workTypeSheetState,
            title = "Choose type workplace",
            description = "Type of place to work according to what you want",
            options = workTypeOptions,
            selectedOption = workType,
            onOptionSelected = { workType = it }
        )

        // Add DatePicker Dialog
        if (showDatePicker) {
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)
                    expirationDate = calendar.time
                    expirationDateError = null
                    showDatePicker = false
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            
            // Set minimum date to today
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            
            // Show the dialog
            LaunchedEffect(Unit) {
                datePickerDialog.show()
            }
        }
    }
}

