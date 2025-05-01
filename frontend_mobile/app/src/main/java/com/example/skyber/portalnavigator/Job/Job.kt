package com.example.skyber.portalnavigator.Job

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.JobListingCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.ParticleSystem
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardContainerLavender
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Job(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("Full-Time") }
    val allJobListings = remember { mutableStateListOf<JobListing>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists

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

    // Filter for event status on selected tab
    val filteredJobListings = when (selectedTab) {
        "Part-Time" -> allJobListings.filter { it.employmentType == "Part-Time" }
        "Full-Time" -> allJobListings.filter { it.employmentType == "Full-Time" }
        else -> allJobListings
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("JobListing")
            .get().addOnSuccessListener { snapshot ->
                allJobListings.clear()
                snapshot.children.forEach { child ->
                    val joblisting = child.getValue(JobListing::class.java)
                    Log.d("JobListingItem", joblisting.toString())
                    if (joblisting != null) {
                        allJobListings.add(joblisting)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                Log.e("VolunteerHubFetch", "Failed to load volunteer events", it)
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlueGradient)
            ) {
                // Particle system as the background
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White,
                    particleCount = 80,
                    backgroundColor = Color(0xFF0D47A1)
                )
                Text(
                    text = "💠",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )

                /*Text(
                    text = "✨",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )*/

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )

                    PortalNav(
                        trailingContent = {
                            PortalNavHandler(navController = navController)
                            Text(
                                "Job Listings",
                                fontSize = 24.sp,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )

                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Part-Time",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Part-Time") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Part-Time" }
                        )
                        Text(
                            "Full-Time",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Full-Time") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Full-Time" }
                        )
                    }


                    if (filteredJobListings.isEmpty()) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                "No Job Listings Available",
                                color = SKyberRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(filteredJobListings.reversed()) { joblisting ->
                                JobListingCard(
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    joblisting = joblisting,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "joblisting",
                                            joblisting
                                        )
                                        navController.navigate(Screens.DetailsJob.screen)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }


                    Button(
                        onClick = {
                            navController.navigate(Screens.PostJob.screen)
                        },
                        modifier = Modifier
                            .width(180.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(28.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(gradientBrush),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "Post Job Listing",
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