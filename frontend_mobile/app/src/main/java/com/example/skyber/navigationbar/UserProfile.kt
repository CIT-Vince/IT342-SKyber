package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserProfile(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        refreshUserProfile()
    }

    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberBlue)
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .verticalScroll(scrollState)
            ) {
                // User card with profile info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile image
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0E0E0))
                                .border(2.dp, SKyberBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.size(80.dp),
                                tint = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // User name
                        Text(
                            text = "${user.firstName ?: ""} ${user.lastName ?: ""}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // User email
                        Text(
                            text = user.email ?: "",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )

                        // Admin badge if applicable
                        if (user.role == "admin") {
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFB39DDB))
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Admin",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = "ADMIN",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Profile option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Profile action */ }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                tint = SKyberBlue
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "Profile",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        // Logout option
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    FirebaseHelper.auth.signOut()
                                    navController.navigate(Screens.Login.screen)
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                tint = Color.Red.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                color = Color.Red.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Personal Information card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        // Header with Edit button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Personal Information",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Button(
                                onClick = { navController.navigate(Screens.EditProfile.screen) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SKyberBlue
                                ),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text(
                                    text = "Edit Profile",
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // First Name
                        InfoField(
                            label = "First Name",
                            value = user.firstName ?: "-",
                            required = true
                        )

                        // Last Name
                        InfoField(
                            label = "Last Name",
                            value = user.lastName ?: "-",
                            required = true
                        )

                        // Age
                        InfoField(
                            label = "Age",
                            value = (user.age ?: "-").toString()
                        )

                        // Gender
                        InfoField(
                            label = "Gender",
                            value = user.gender ?: "-"
                        )

                        // Phone Number (if available in your data class)
                        if (user::class.java.declaredFields.any { it.name == "phoneNumber" }) {
                            InfoField(
                                label = "Phone Number",
                                value = try {
                                    user.javaClass.getMethod("getPhoneNumber").invoke(user)?.toString() ?: "-"
                                } catch (e: Exception) {
                                    "-"
                                }
                            )
                        }

                        // E-mail
                        InfoField(
                            label = "E-mail",
                            value = user.email ?: "-",
                            required = true
                        )

                        // Address (if available in your data class)
                        if (user::class.java.declaredFields.any { it.name == "address" }) {
                            InfoField(
                                label = "Address",
                                value = try {
                                    user.javaClass.getMethod("getAddress").invoke(user)?.toString() ?: "-"
                                } catch (e: Exception) {
                                    "-"
                                }
                            )
                        }

                        // Birthdate
                        InfoField(
                            label = "Birthdate",
                            value = user.birthdate ?: "-"
                        )
                    }
                }

                // Add some space at the bottom for better scrolling
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun InfoField(label: String, value: String, required: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
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

        Text(
            text = value,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = Color.LightGray
        )
    }
}