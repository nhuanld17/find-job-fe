package com.example.findjob.ui.components.form

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChangePasswordForm(
    isLoading: Boolean = false,
    onSave: (oldPass: String, newPass: String) -> Unit,
    errorMessage: String? = null,
    showSuccess: Boolean = false,

) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf(false) }
    var newPasswordError by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun validate(): Boolean {
        newPasswordError = if (newPassword.length < 6) "Password must be at least 6 characters" else null
        confirmError = newPassword != confirmPassword
        return newPasswordError == null && !confirmError && oldPassword.isNotBlank() && newPassword.isNotBlank()
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
                Text(
                    text = "Update Password",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1446),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                // Old Password
                Text(
                    text = "Old Password",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1446),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444)
                    )
                )
                Spacer(Modifier.height(12.dp))
                // New Password
                Text(
                    text = "New Password",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1446),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        newPasswordError = if (it.length < 6) "Password must be at least 6 characters" else null
                        confirmError = confirmPassword.isNotEmpty() && it != confirmPassword
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = newPasswordError != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (newPasswordError != null) Color(0xFFE53935) else Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444),
                        errorBorderColor = Color(0xFFE53935)
                    )
                )
                if (newPasswordError != null) {
                    Text(newPasswordError!!, color = Color(0xFFE53935), fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
                }
                Spacer(Modifier.height(12.dp))
                // Confirm Password
                Text(
                    text = "Confirm Password",
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1446),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmError = newPassword != it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmError,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (confirmError) Color(0xFFE53935) else Color(0xFF1A1444),
                        unfocusedBorderColor = Color(0xFFE5E5EF),
                        cursorColor = Color(0xFF1A1444),
                        errorBorderColor = Color(0xFFE53935)
                    )
                )
                if (confirmError) {
                    Text("Password incorrect", color = Color(0xFFE53935), fontSize = 13.sp, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (validate()) {
                            onSave(oldPassword, newPassword)
                        } else {
                            focusRequester.requestFocus()
                        }
                    },
                    enabled = !isLoading && oldPassword.isNotBlank() && newPassword.isNotBlank() && confirmPassword.isNotBlank() && newPasswordError == null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1444))
                ) {
                    if (isLoading) {
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
                if (errorMessage != null) {
                    Text(errorMessage, color = Color(0xFFE53935), fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                }
                if (showSuccess) {
                    Text("Password changed successfully!", color = Color(0xFF1A1444), fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}