package com.example.findjob.ui.screen.employee

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FilterScreen(navController: NavController) {
    var selectedLocation by remember { mutableStateOf("California") }
    val locationOptions = listOf("California", "New York", "Texas", "Remote")

    var expanded by remember { mutableStateOf(false) }

    var selectedSalary by remember { mutableStateOf("$13K") }
    val salaryOptions = listOf("$13K", "$18K", "$25K")

    var selectedJobType by remember { mutableStateOf("Full time") }
    val jobTypes = listOf("Full time", "Part time", "Remote")

    var selectedWorkplace by remember { mutableStateOf("On-site") }
    val workplaces = listOf("On-site", "Hybrid", "Remote")

    var selectedPosition by remember { mutableStateOf("Senior") }
    val positionLevels = listOf("Junior", "Senior", "Leader", "Manager")

    var selectedExperience by remember { mutableStateOf("5–10 years") }
    val experienceOptions = listOf(
        "No experience", "Less than a year", "1–3 years",
        "3–5 years", "5–10 years", "More than 10 years"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .size(28.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Filter", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Location", fontWeight = FontWeight.SemiBold)
            Box {
                OutlinedTextField(
                    value = selectedLocation,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                        .clickable { expanded = true },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    locationOptions.forEach {
                        DropdownMenuItem(onClick = {
                            selectedLocation = it
                            expanded = false
                        }, text = { Text(it) })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Salary", fontWeight = FontWeight.SemiBold)
            salaryOptions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedSalary = it }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = selectedSalary == it, onClick = { selectedSalary = it })
                    Text(it)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Job Type", fontWeight = FontWeight.SemiBold)
            jobTypes.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedJobType = it }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = selectedJobType == it, onClick = { selectedJobType = it })
                    Text(it)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Type of workplace", fontWeight = FontWeight.SemiBold)
            workplaces.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedWorkplace = it }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = selectedWorkplace == it, onClick = { selectedWorkplace = it })
                    Text(it)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Position level", fontWeight = FontWeight.SemiBold)
            positionLevels.forEach { position ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPosition = position }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = selectedPosition == position, onClick = { selectedPosition = position })
                    Text(position)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Experience", fontWeight = FontWeight.SemiBold)
            experienceOptions.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedExperience = it }
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(selected = selectedExperience == it, onClick = { selectedExperience = it })
                    Text(it)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                OutlinedButton(
                    onClick = {
                        selectedLocation = "California"
                        selectedSalary = "$13K"
                        selectedJobType = "Full time"
                        selectedWorkplace = "On-site"
                        selectedPosition = "Senior"
                        selectedExperience = "5–10 years"
                    },
                    border = BorderStroke(1.dp, Color(0xFFFF9100))
                ) {
                    Text("Reset", color = Color(0xFFFF9100))
                }

                Button(
                    onClick = {
                        // Handle apply logic
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E0A57))
                ) {
                    Text("APPLY NOW", color = Color.White)
                }
            }
        }
    }
}