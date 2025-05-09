package com.example.skyber.navigationbar.userprofilescreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfile(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Initialize form fields with current user data
    var newFirstname by remember { mutableStateOf(user?.firstName ?: "") }
    var newLastname by remember { mutableStateOf(user?.lastName ?: "") }
    var newEmail by remember { mutableStateOf(user?.email ?: "") }
    var newAddress by remember { mutableStateOf(user?.address ?: "") }
    var newPhonenumber by remember { mutableStateOf(user?.phoneNumber ?: "") }
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    // Track field errors
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var phoneNumberError by remember { mutableStateOf<String?>(null) }

    // Track if form is submitting
    var isSubmitting by remember { mutableStateOf(false) }

    // Handler for validating and updating profile
    val handleUpdateProfile = {
        // Reset errors
        emailError = null
        passwordError = null
        phoneNumberError = null

        // Simple validation
        var hasError = false

        // Email validation
        if (newEmail.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            emailError = "Please enter a valid email address"
            hasError = true
        }

        // Phone number validation
        if (newPhonenumber.isNotEmpty() && !newPhonenumber.all { it.isDigit() || it == '+' || it == '-' || it == ' ' || it == '(' || it == ')' }) {
            phoneNumberError = "Please enter a valid phone number"
            hasError = true
        }

        // Password validation
        if (newPassword.isNotEmpty() && newPassword != confirmNewPassword) {
            passwordError = "Passwords don't match"
            hasError = true
        }

        if (!hasError) {
            isSubmitting = true

            val database = FirebaseHelper.databaseReference
            val uid = FirebaseHelper.auth.currentUser?.uid

            if (uid != null) {
                database.child("users").child(uid).get()
                    .addOnSuccessListener {
                        val currentUser = userProfile.value

                        if (currentUser != null && (oldPassword.isEmpty() || currentUser.password == oldPassword)) {
                            val updatedUser = User(
                                id = currentUser.id,
                                firstName = newFirstname.ifEmpty { currentUser.firstName },
                                lastName = newLastname.ifEmpty { currentUser.lastName },
                                email = newEmail.ifEmpty { currentUser.email },
                                password = newPassword.ifEmpty { currentUser.password },
                                birthdate = currentUser.birthdate,
                                gender = currentUser.gender,
                                role = currentUser.role,
                                phoneNumber = newPhonenumber.ifEmpty { currentUser.phoneNumber },
                                address = newAddress.ifEmpty { currentUser.address },
                                age = currentUser.age
                            )

                            FirebaseHelper.databaseReference.child("users")
                                .child(currentUser.id!!)
                                .setValue(updatedUser)
                                .addOnSuccessListener {
                                    refreshUserProfile()
                                    Toast.makeText(
                                        context,
                                        "Profile Updated Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.popBackStack()
                                    isSubmitting = false
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed to update profile",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isSubmitting = false
                                }
                        } else {
                            Toast.makeText(
                                context,
                                "Current password is incorrect",
                                Toast.LENGTH_SHORT
                            ).show()
                            passwordError = "Current password is incorrect"
                            isSubmitting = false
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Failed to retrieve user data",
                            Toast.LENGTH_SHORT
                        ).show()
                        isSubmitting = false
                    }
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }

                Text(
                    text = "Edit Profile",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Personal Information",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // First Name
                        FormField(
                            label = "First Name",
                            value = newFirstname,
                            onValueChange = { newFirstname = it },
                            required = true,
                            icon = Icons.Default.Person,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            )
                        )

                        // Last Name
                        FormField(
                            label = "Last Name",
                            value = newLastname,
                            onValueChange = { newLastname = it },
                            required = true,
                            icon = Icons.Default.Person,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            )
                        )

                        // Email
                        FormField(
                            label = "E-mail",
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            required = true,
                            icon = Icons.Default.Email,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            error = emailError
                        )

                        // Phone Number
                        FormField(
                            label = "Phone Number",
                            value = newPhonenumber,
                            onValueChange = { newPhonenumber = it },
                            icon = Icons.Default.Phone,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            error = phoneNumberError
                        )

                        // Address
                        FormField(
                            label = "Address",
                            value = newAddress,
                            onValueChange = { newAddress = it },
                            icon = Icons.Default.Home,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Change Password",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Current Password
                        PasswordField(
                            label = "Current Password",
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            required = newPassword.isNotEmpty(),
                            error = passwordError?.takeIf { it == "Current password is incorrect" }
                        )

                        // New Password
                        PasswordField(
                            label = "New Password",
                            value = newPassword,
                            onValueChange = { newPassword = it }
                        )

                        // Confirm New Password
                        PasswordField(
                            label = "Confirm New Password",
                            value = confirmNewPassword,
                            onValueChange = { confirmNewPassword = it },
                            required = newPassword.isNotEmpty(),
                            error = passwordError?.takeIf { it == "Passwords don't match" }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = { handleUpdateProfile() },
                        enabled = !isSubmitting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SKyberBlue
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Save Changes")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )

            if (required) {
                Text(
                    text = " *",
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
        }

        CustomOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = "",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            maxLines = 1,
            backgroundColor = Color(0xFFF5F7FA)
        )

        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    error: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )

            if (required) {
                Text(
                    text = " *",
                    fontSize = 14.sp,
                    color = Color.Red
                )
            }
        }

        CustomOutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = "",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            maxLines = 1,
            textColor = Color.Black,
            backgroundColor = Color(0xFFF5F7FA)
        )

        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}