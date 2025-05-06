package com.example.skyber.navigationbar.userprofilescreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.dataclass.User
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfile(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    var newFirstname by remember { mutableStateOf("") }
    var newLastname by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    var newPhonenumber by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "floating animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale animation"
    )

    val topLeftPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating top left"
    )
    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlueGradient)
            ) {
                // Particle system as the background
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White,
                    particleCount = 50,
                    backgroundColor = Color(0xFF0D47A1)
                )
                Text(
                    text = "💠",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )

                // Main content on top of the particle system
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )

                    Text(
                        text = "Edit Profile",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PersonPin,
                            tint = White,
                            contentDescription = "User Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                        )

                        // Display the current user name and double check if null kay mo crash
                        Row(
                            modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            if (user != null) {
                                Text(
                                    text = "${user.firstname} ${user.lastname}",
                                    fontSize = 30.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Button(
                                    onClick = {
                                        val database =
                                            FirebaseHelper.databaseReference
                                        val uid = FirebaseHelper.auth.currentUser?.uid
                                        if (uid != null) {
                                            database.child("users").child(uid).get()
                                                .addOnSuccessListener {
                                                    val currentUser = userProfile.value

                                                    if (currentUser != null && currentUser.password == oldPassword) {
                                                        val updatedUser = User(
                                                            id = currentUser.id,
                                                            firstname = newFirstname.ifEmpty { currentUser.firstname },
                                                            lastname = newLastname.ifEmpty { currentUser.lastname },
                                                            email = newEmail.ifEmpty { currentUser.email },
                                                            password = newPassword.ifEmpty { currentUser.password },
                                                            birthdate = currentUser.birthdate,
                                                            gender = currentUser.gender,
                                                            role = currentUser.role,
                                                            phoneNumber = newPhonenumber.ifEmpty { currentUser.phoneNumber },
                                                            address = newAddress.ifEmpty { currentUser.address }
                                                        )
                                                        FirebaseHelper.databaseReference.child("users")
                                                            .child(currentUser.id!!)
                                                            .setValue(updatedUser)
                                                            .addOnSuccessListener {
                                                                refreshUserProfile()
                                                                Toast.makeText(
                                                                    context,
                                                                    "Profile Updated",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }.addOnFailureListener {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Failed to update profile",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }

                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Old Password Doesn't Match",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SKyberBlue,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(60.dp))
                                        .wrapContentSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Update,
                                        contentDescription = "Update",
                                        tint = White,
                                        modifier = Modifier
                                            .height(20.dp)
                                            .width(20.dp)
                                    )
                                }

                            } else {
                                CircularProgressIndicator(color = SKyberYellow)
                            }
                        }

                    //Main Content
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {

                                CustomOutlinedTextField(
                                    value = newFirstname,
                                    onValueChange = { newFirstname = it },
                                    label = "First Name",
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                CustomOutlinedTextField(
                                    value = newLastname,
                                    onValueChange = { newLastname = it },
                                    label = "Last Name"
                                )


                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = newEmail,
                                    onValueChange = { newEmail = it },
                                    label = "Email"
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = newAddress,
                                    onValueChange = { newAddress = it },
                                    label = "Address"
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = newPhonenumber,
                                    onValueChange = { newPhonenumber = it },
                                    label = "Phone Number"
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = oldPassword,
                                    onValueChange = { oldPassword = it },
                                    label = "Password"
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = newPassword,
                                    onValueChange = { newPassword = it },
                                    label = "New Password"
                                )
                                }
                            }
                        }
                    }
                }//End of main content box
            }
        }
    }
}