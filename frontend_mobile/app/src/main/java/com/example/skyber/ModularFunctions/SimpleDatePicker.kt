package com.example.skyber.ModularFunctions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.skyber.ui.theme.White
import java.time.LocalDate

/**
 * A simplified date picker field that only uses Jetpack Compose components.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDatePickerField(
    selectedDate: String = "",
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    // Initialize date picker state with current date if no selection
    var datePickerState by remember {
        mutableStateOf(
            if (selectedDate.isNotEmpty()) {
                try {
                    val parts = selectedDate.split("-")
                    if (parts.size == 3) {
                        Triple(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                    } else {
                        val now = LocalDate.now()
                        Triple(now.year, now.monthValue, now.dayOfMonth)
                    }
                } catch (e: Exception) {
                    val now = LocalDate.now()
                    Triple(now.year, now.monthValue, now.dayOfMonth)
                }
            } else {
                val now = LocalDate.now()
                Triple(now.year, now.monthValue, now.dayOfMonth)
            }
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "What's your date of birth?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Custom date field with rounded corners and calendar icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF2F4F7))
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectedDate.ifEmpty { "Select date" },
                color = if (selectedDate.isEmpty()) Color.Gray else Color.Black,
                fontSize = 16.sp
            )

            // Calendar icon on the right
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Select date",
                tint = Color(0xFF6B7280),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }
    }

    // Simple date picker dialog
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Year, Month, Day pickers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Year dropdown
                        SimpleNumberPicker(
                            label = "Year",
                            value = datePickerState.first,
                            onValueChange = {
                                datePickerState = datePickerState.copy(first = it)
                            },
                            range = (1900..2023).toList(),
                            modifier = Modifier.weight(1f)
                        )

                        // Month dropdown
                        SimpleNumberPicker(
                            label = "Month",
                            value = datePickerState.second,
                            onValueChange = {
                                datePickerState = datePickerState.copy(second = it)
                            },
                            range = (1..12).toList(),
                            modifier = Modifier.weight(1f)
                        )

                        // Day dropdown
                        SimpleNumberPicker(
                            label = "Day",
                            value = datePickerState.third,
                            onValueChange = {
                                datePickerState = datePickerState.copy(third = it)
                            },
                            range = (1..31).toList(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                // Format the date as YYYY-MM-DD
                                val year = datePickerState.first
                                val month = datePickerState.second.toString().padStart(2, '0')
                                val day = datePickerState.third.toString().padStart(2, '0')
                                val formattedDate = "$year-$month-$day"

                                onDateSelected(formattedDate)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0066FF)
                            )
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleNumberPicker(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: List<Int>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(horizontal = 4.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Box {
            Text(
                text = value.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(vertical = 8.dp),
                fontWeight = FontWeight.Medium
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 200.dp)
            ) {
                range.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.toString()) },
                        onClick = {
                            onValueChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Extension function to copy a Triple
fun <A, B, C> Triple<A, B, C>.copy(
    first: A = this.first,
    second: B = this.second,
    third: C = this.third
): Triple<A, B, C> = Triple(first, second, third)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentSimpleDatePickerField(
    selectedDate: String = "",
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    // Initialize date picker state with current date if no selection
    var datePickerState by remember {
        mutableStateOf(
            if (selectedDate.isNotEmpty()) {
                try {
                    val parts = selectedDate.split("-")
                    if (parts.size == 3) {
                        Triple(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                    } else {
                        val now = LocalDate.now()
                        Triple(now.year, now.monthValue, now.dayOfMonth)
                    }
                } catch (e: Exception) {
                    val now = LocalDate.now()
                    Triple(now.year, now.monthValue, now.dayOfMonth)
                }
            } else {
                val now = LocalDate.now()
                Triple(now.year, now.monthValue, now.dayOfMonth)
            }
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "What's your date of birth?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Custom date field with rounded corners and calendar icon
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = selectedDate.ifEmpty { "Select date" },
                color = if (selectedDate.isEmpty()) White else White,
                fontSize = 16.sp
            )

            // Calendar icon on the right
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Select date",
                tint = White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }
    }

    // Simple date picker dialog
    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Year, Month, Day pickers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Year dropdown
                        SimpleNumberPicker(
                            label = "Year",
                            value = datePickerState.first,
                            onValueChange = {
                                datePickerState = datePickerState.copy(first = it)
                            },
                            range = (1900..2023).toList(),
                            modifier = Modifier.weight(1f)
                        )

                        // Month dropdown
                        SimpleNumberPicker(
                            label = "Month",
                            value = datePickerState.second,
                            onValueChange = {
                                datePickerState = datePickerState.copy(second = it)
                            },
                            range = (1..12).toList(),
                            modifier = Modifier.weight(1f)
                        )

                        // Day dropdown
                        SimpleNumberPicker(
                            label = "Day",
                            value = datePickerState.third,
                            onValueChange = {
                                datePickerState = datePickerState.copy(third = it)
                            },
                            range = (1..31).toList(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                // Format the date as YYYY-MM-DD
                                val year = datePickerState.first
                                val month = datePickerState.second.toString().padStart(2, '0')
                                val day = datePickerState.third.toString().padStart(2, '0')
                                val formattedDate = "$year-$month-$day"

                                onDateSelected(formattedDate)
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0066FF)
                            )
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}