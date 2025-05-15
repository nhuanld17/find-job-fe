package com.example.findjob.ui.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class ButtonType {
    object Primary : ButtonType()
    object Pink : ButtonType()
    object Delete: ButtonType()
}

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(130160),
    textColor: Color = Color.White,
    enabled: Boolean = true,
    height: Int = 48,
    type: ButtonType
) {
    when (type) {
        ButtonType.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(height.dp),
                enabled = enabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = textColor,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        ButtonType.Delete -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(height.dp),
                enabled = enabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFE3DD),
                    contentColor = Color(0xFFED7457),
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        ButtonType.Pink -> {
                Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(height.dp),
                enabled = enabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = textColor,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
