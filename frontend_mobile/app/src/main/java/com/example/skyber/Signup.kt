package com.example.skyber

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import java.util.*
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController){
    var firstname by remember {mutableStateOf ("")}
    var lastname by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf( "")}
    //Date of Birth
    var dob by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SKyberDarkBlue)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(130.dp)
                    .height(50.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )

        }
        Column(
            modifier = Modifier
                .width(480.dp)
                .padding(20.dp)
                .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

        ){


            Text(
                text = "Create Account",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = firstname,
                onValueChange = { firstname = it },
                label = { Text("First Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = SKyberYellow,
                    unfocusedIndicatorColor = SKyberYellow,
                    focusedLabelColor = SKyberYellow,
                    unfocusedLabelColor = SKyberYellow
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Last Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = SKyberYellow,
                    unfocusedIndicatorColor = SKyberYellow,
                    focusedLabelColor = SKyberYellow,
                    unfocusedLabelColor = SKyberYellow
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = SKyberYellow,
                    unfocusedIndicatorColor = SKyberYellow,
                    focusedLabelColor = SKyberYellow,
                    unfocusedLabelColor = SKyberYellow
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            //Date of birth field here
            DatePickerField(onDateSelected = {
                dob = convertMillisToDate(it)
            })

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = SKyberYellow,
                    unfocusedIndicatorColor = SKyberYellow,
                    focusedLabelColor = SKyberYellow,
                    unfocusedLabelColor = SKyberYellow
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color.White),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = SKyberYellow,
                    unfocusedIndicatorColor = SKyberYellow,
                    focusedLabelColor = SKyberYellow,
                    unfocusedLabelColor = SKyberYellow
                )
            )

            Spacer(modifier = Modifier.height(15.dp))
            //Radio Group diri
            RadioButtonGenders(
                modifier = Modifier.padding(16.dp),
                gender = gender,
                onGenderSelected = { gender = it }
            )

            Spacer(modifier = Modifier.height(15.dp))

            //Signup button
            //Bring to Login for now change onlclick logic later
            Button(
                onClick = {
                    //Firebase Realtime db reference gikan sa singleton file
                    val reference = FirebaseHelper.databaseReference
                    //get reference from singleton
                    val userReference = FirebaseHelper.databaseReference.child("users")
                    //user object creation
                    val userId = userReference.push().key

                    // Basic validation
                    if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                        Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                    } else if (userId != null) {//logic for registering user to realtime db
                        val user = User(
                            id = userId,
                            firstname = firstname,
                            lastname = lastname,
                            email = email,
                            password = password,
                            dob = dob,
                            gender = gender
                        )

                        userReference.child(userId).setValue(user).addOnCompleteListener{task ->
                            if(task.isSuccessful){
                                navController.navigate(Screens.Login.screen)
                                Toast.makeText(context, "Registration Succes", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(context, "Unexpected Errorm Please try again.", Toast.LENGTH_SHORT).show()
                    }

                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = SKyberRed,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(600.dp))
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Log in",
                color = SKyberYellow,
                fontSize = 15.sp,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Screens.Login.screen)
                    }
            )

        }//End of main Column layout

    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    val navController = rememberNavController()
    SignupScreen(navController = navController)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String = "Date of Birth",
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    TextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(
                onClick = { showDatePicker = !showDatePicker },

            ) {
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

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
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

///for genders
@Composable
fun RadioButtonGenders(
    gender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
    ) {
    val radioOptions = listOf("Male", "Female", "Others")
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Row(
        modifier.selectableGroup()
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .width(100.dp)
                    .height(30.dp)
                    .selectable(
                        selected = (text == gender),
                        onClick = { onGenderSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                RadioButton(
                    selected = (text == gender),
                    onClick = null ,
                    colors = RadioButtonDefaults.colors(
                    selectedColor = SKyberYellow,
                    unselectedColor = Color.White,
                    )
                )

                Text(
                    text = text,
                    color = SKyberYellow,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )

            }
        }
    }
}