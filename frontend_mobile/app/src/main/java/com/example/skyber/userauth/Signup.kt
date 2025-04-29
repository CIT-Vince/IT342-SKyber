package com.example.skyber.userauth

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.R
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavHostController) {
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phonenumber by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val role = "USER"
    val context = LocalContext.current

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "animations")

    // Diamond emoji animation
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Star emoji animation - rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Top-left emoji animation
    val topLeftPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )

    // Gradient for button
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF06B6D4), // cyan-500
            Color(0xFF3B82F6)  // blue-500
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SKyberDarkBlueGradient)
    ) {
        // Particle background
        ParticleSystem(
            modifier = Modifier.fillMaxSize(),
            particleColor = Color.White,
            particleCount = 80,
            backgroundColor = Color(0xFF0D47A1)
        )

        // Decorative elements
        Text(
            text = "💠",
            fontSize = 26.sp,
            modifier = Modifier
                .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                .graphicsLayer(alpha = 0.5f)
        )

        Text(
            text = "✨",
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 20.dp)
                .graphicsLayer(alpha = 0.5f)
        )

        Text(
            text = "★",
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-120).dp, y = (-150).dp)
                .graphicsLayer(
                    alpha = 0.4f,
                    rotationZ = rotation
                )
        )

        // Scrollable content
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.skyber),
                            contentDescription = "SKYBER Logo",
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "SKYBER",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0033CC)
                        )
                    }

                    // Create Account header with bouncing emoji
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Create Account",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "💠",
                            fontSize = 24.sp,
                            modifier = Modifier.scale(scale)
                        )
                    }

                    // Already have account text
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Sign In",
                            fontSize = 14.sp,
                            color = SKyberRed,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                navController.navigate(Screens.Login.screen)
                            }
                        )
                    }

                    // Form fields
                    // Name fields (First name and Last name)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // First Name
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "First Name",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            OutlinedTextField(
                                value = firstname,
                                onValueChange = { firstname = it },
                                placeholder = { Text("John") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )
                        }

                        // Last Name
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Last Name",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            OutlinedTextField(
                                value = lastname,
                                onValueChange = { lastname = it },
                                placeholder = { Text("Doe") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "E-mail",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("example@gmail.com") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF0066FF),
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••") },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF0066FF),
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Password field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Confirm Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            placeholder = { Text("••••••") },
                            singleLine = true,
                            visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF0066FF),
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Gender selection - using your existing RadioButtonGenders function
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "What's your gender? (Optional)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Using your existing radio button implementation
                        RadioButtonGenders(
                            gender = gender,
                            onGenderSelected = { gender = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date of Birth field - using your existing DatePickerField
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "What's your date of birth?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // Using your existing DatePickerField
                        SimpleDatePickerField(
                            selectedDate = dob,
                            onDateSelected = { formattedDate ->
                                dob = formattedDate
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Number field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Phone Number",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        OutlinedTextField(
                            value = phonenumber,
                            onValueChange = { phonenumber = it },
                            placeholder = { Text("09123456789") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF0066FF),
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Address field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Address",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            placeholder = { Text("Your address") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color(0xFF0066FF),
                                unfocusedBorderColor = Color(0xFFD1D5DB)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Remember me & Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF0066FF),
                                    uncheckedColor = Color.Gray
                                )
                            )

                            Text(
                                text = "Remember me",
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        }

                        Text(
                            text = "Forgot Password?",
                            fontSize = 14.sp,
                            color = SKyberRed,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { /* Handle forgot password */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign up button with gradient and Firebase auth
                    Button(
                        onClick = {
                            val auth = FirebaseHelper.auth // Get auth from firebase singleton
                            val database = FirebaseHelper.databaseReference // Firebase Realtime db reference

                            // Additional validation for password confirmation
                            if (password != confirmPassword) {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Basic validation
                            if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() ||
                                password.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                // 1. create Firebase Auth account
                                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            val currentUser = auth.currentUser
                                            val uid = currentUser?.uid

                                            if (uid != null) {
                                                //2. user profile creation and save to Realtime DB using UID as key
                                                val user = User(
                                                    id = uid,
                                                    firstname = firstname,
                                                    lastname = lastname,
                                                    email = email,
                                                    password = password,
                                                    birthdate = dob,
                                                    gender = gender,
                                                    role = role,
                                                    phoneNumber = phonenumber,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        enabled = !isLoading
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(gradientBrush),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Create Account",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Keep your existing RadioButtonGenders function
@Composable
fun RadioButtonGenders(
    gender: String,
    onGenderSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = listOf("Male", "Female", "Others")
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Row(
        modifier
            .selectableGroup()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .selectable(
                        selected = (text == gender),
                        onClick = { onGenderSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == gender),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0066FF),
                        unselectedColor = Color.Gray,
                    )
                )

                Text(
                    text = text,
                    color = if (text == gender) Color(0xFF0066FF) else Color.Gray,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

/*@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun SignupScreenPreview() {
    SkyberTheme {
        SignupScreen(rememberNavController())
    }
}*/