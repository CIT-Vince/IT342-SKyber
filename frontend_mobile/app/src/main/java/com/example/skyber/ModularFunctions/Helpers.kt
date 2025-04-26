package com.example.skyber.ModularFunctions

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.skyber.ui.theme.SKyberYellow
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String = "",
    selectedDate: String,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    /*val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""*/

    TextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = !showDatePicker },)
            {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(40.dp)),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = SKyberYellow,
            unfocusedIndicatorColor = SKyberYellow,
            focusedLabelColor = SKyberYellow,
            unfocusedLabelColor = SKyberYellow
        )
    )

    if (showDatePicker) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { showDatePicker = false }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun PortalTile(
    icon: ImageVector,
    title: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
            //.aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

/*
@Composable
fun DatePickerField(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("DOB") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}
*/