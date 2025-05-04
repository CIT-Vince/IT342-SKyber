package com.example.skyber.volunteerhubscreens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.Announcement.showToast
import com.example.skyber.ui.theme.*


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsVolunteerHub(navController: NavHostController) {
    val volunteerPost =
        navController.previousBackStackEntry?.savedStateHandle?.get<VolunteerPost>("volunteerPost")
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

    if (volunteerPost == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
        val isOngoing = volunteerPost.status.lowercase() == "ongoing"
        val backgroundColor = if (isOngoing) BoxGreen else BoxRed
        val textColor = if (isOngoing) BoxTextGreen else SKyberRed

        Scaffold { innerPadding ->
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
                        .graphicsLayer(alpha = 0.3f) // Adjust opacity
                )

                /*Text(
                    text = "✨",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 20.dp)
                        .graphicsLayer(alpha = 0.3f) // Adjust opacity
                )*/

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 40.dp, bottom = 40.dp)
                            //.align(Alignment.Center)
                            .background(Color.White, RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp)
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(34.dp),
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        text = volunteerPost.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        color = SKyberBlue
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(22.dp))
                                            .background(backgroundColor)
                                            .padding(horizontal = 8.dp)
                                            .width(80.dp)
                                    ) {
                                        Text(
                                            text = volunteerPost.status,
                                            fontSize = 18.sp,
                                            color = textColor,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Contact: ${volunteerPost.contactPerson}",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Email: ${volunteerPost.contactEmail}",
                                    fontSize = 14.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Event Details",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = volunteerPost.description,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Location: ${volunteerPost.location}",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Text(
                                    text = "Event Date: ${volunteerPost.eventDate}",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Requirements",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = volunteerPost.requirements,
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(){
                                    Button(
                                        onClick = {
                                            applyToVolunteerEvent(volunteerPost.id, context)
                                            navController.navigate(Screens.VolunteerHub.screen)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                    ){
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(gradientBrush),
                                            contentAlignment = Alignment.Center
                                        ){
                                            Text(
                                                text = "Apply",
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
        }
    }
}


fun applyToVolunteerEvent(eventId: String, context: Context) {
    val userId = FirebaseHelper.auth.currentUser?.uid ?: return

    val userRef = FirebaseHelper.databaseReference.child("users").child(userId)

    userRef.get().addOnSuccessListener { snapshot ->
        val user = snapshot.getValue(User::class.java)

        if (user != null) {
            val updatedList = user.volunteeredActivities.toMutableList()

            if (eventId !in updatedList) {
                updatedList.add(eventId)

                userRef.child("volunteeredActivities").setValue(updatedList)
                    .addOnSuccessListener {
                        showToast(context, "Successfully applied to event")
                    }
                    .addOnFailureListener {
                        showToast(context, "Failed to apply")
                    }
            } else {
                showToast(context, "Already applied to this event")
            }
        }
    }.addOnFailureListener {
        showToast(context, "User fetch failed")
    }
}