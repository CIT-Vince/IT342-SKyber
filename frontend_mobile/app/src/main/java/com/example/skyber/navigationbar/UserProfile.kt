package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.ModularFunctions.ListCard
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.*

@Composable@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun UserProfile(navController: NavHostController, userProfile : MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    LaunchedEffect(Unit) {
        refreshUserProfile()
    }

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

    if (user == null) {// Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient),
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
                        text = "User Profile",
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
                            if (user != null) {
                                Text(
                                    text = user.lastname ?: "User",
                                    fontSize = 30.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = user.firstname ?: "User",
                                    fontSize = 30.sp,
                                    color = White,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                CircularProgressIndicator(color = SKyberYellow)
                            }

                    }
                    //Main Content
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(20.dp)
                        ) {
                            ListCard(
                                title = "Edit Profile",
                                icon = Icons.Filled.ManageAccounts,
                                onCardClick = {
                                    navController.navigate(Screens.EditProfile.screen)
                                })
                            /*ListCard(
                                title = "Volunteered Events",
                                icon = Icons.Filled.VolunteerActivism,
                                onCardClick = {
                                    navController.navigate(Screens.VolunteerList.screen)
                                })*/
                            ListCard(
                                title = "Logout",
                                icon = Icons.AutoMirrored.Filled.Logout,
                                onCardClick = {
                                    FirebaseHelper.auth.signOut()
                                    navController.navigate(Screens.Login.screen)
                                })

                        }
                    }

                }//End of main Column Layout
            }
        }//End of Scaffold
    }
}
