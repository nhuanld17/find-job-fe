package com.example.findjob.ui.components.bottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceBottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    title: String,
    description: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF23235B))
                Spacer(modifier = Modifier.height(1.dp))
                Text(description, color = Color(0xFF23235B), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    items(options) { option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOptionSelected(option)
                                    onDismiss()
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == option,
                                onClick = {
                                    onOptionSelected(option)
                                    onDismiss()
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF23235B))
                            )
                            Text(option, fontWeight = FontWeight.Bold, color = Color(0xFF23235B), modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}