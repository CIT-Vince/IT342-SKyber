package com.example.skyber.userauth

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import java.util.*
import androidx.compose.ui.platform.LocalContext
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.R
import com.example.skyber.Screens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController){
    var firstname by remember {mutableStateOf ("")}
    var lastname by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf( "")}
    var address by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf( "")}

    //Date of Birth
    var dob by remember { mutableStateOf("") }
    val role = "viewer"
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SKyberDarkBlue)
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                TextField(
                    value = firstname,
                    onValueChange = { firstname = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = SKyberYellow,
                        unfocusedIndicatorColor = SKyberYellow,
                        focusedLabelColor = SKyberYellow,
                        unfocusedLabelColor = SKyberYellow
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    value = lastname,
                    onValueChange = { lastname = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color.White),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = SKyberYellow,
                        unfocusedIndicatorColor = SKyberYellow,
                        focusedLabelColor = SKyberYellow,
                        unfocusedLabelColor = SKyberYellow
                    )
                )
            }

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

            TextField(
                value = phonenumber,
                onValueChange = { phonenumber = it },
                label = { Text("Phone Number") },
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
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
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

            Spacer(modifier = Modifier.height(15.dp))

            //Date of birth field here
            DatePickerField(
                label = "Date of Birth",
                selectedDate = dob,
                onDateSelected = { millis ->
                    dob = convertMillisToDate(millis)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

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

            //Radio Group diri
            RadioButtonGenders(
                modifier = Modifier.padding(16.dp),
                gender = gender,
                onGenderSelected = { gender = it }
            )

            Spacer(modifier = Modifier.height(2.dp))

            //Signup button
            Button(
                onClick = {
                    val auth = FirebaseHelper.auth//get auth from firebase singleton
                    val database = FirebaseHelper.databaseReference//Firebase Realtime db reference gikan sa singleton file

                    // Basic validation
                    if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() ||
                        password.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                        Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 1. create Firebase Auth account
                        auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val currentUser = auth.currentUser
                                    val uid = currentUser?.uid

                                    if (uid != null) {
                                        //2. user profile  creation and save to Realtime DB using UID as key
                                        val user = User(
                                            id = uid,
                                            firstname = firstname,
                                            lastname = lastname,
                                            email = email,
                                            password = password,
                                            dob = dob,
                                            gender = gender,
                                            role = role,
                                            phonenumber = phonenumber,
                                            address = address
                                        )

                                    database.child("users").child(uid).setValue(user)
                                        .addOnCompleteListener { dbTask ->
                                            if (dbTask.isSuccessful) {
                                                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                                navController.navigate(Screens.Login.screen)
                                            } else {
                                                Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                                        }
                                } else {
                                    val errorMessage = task.exception?.message ?: "Registration failed."
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
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

            Spacer(modifier = Modifier.height(3.dp))
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