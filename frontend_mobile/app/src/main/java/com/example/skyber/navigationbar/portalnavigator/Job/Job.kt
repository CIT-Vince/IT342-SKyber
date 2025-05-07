package com.example.skyber.navigationbar.portalnavigator.Job

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.ModularFunctions.JobListingCard
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Job(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("All".lowercase()) }
    val allJobListings = remember { mutableStateListOf<JobListing>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists
    var searchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }//search bar state management


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


    // Filtered job listings based on the selected tab and search query
    val filteredJobListings = allJobListings.filter { job ->
        val matchesTab = selectedTab == "All".lowercase() || job.employementType.equals(selectedTab, ignoreCase = true)
        val matchesSearch = job.companyName.contains(searchQuery, ignoreCase = true) ||
                job.jobTitle.contains(searchQuery, ignoreCase = true)

        matchesTab && matchesSearch
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("JobListings")
            .get().addOnSuccessListener { snapshot ->
                allJobListings.clear()
                snapshot.children.forEach { child ->
                    val joblisting = child.getValue(JobListing::class.java)
                    if (joblisting != null) {
                        allJobListings.add(joblisting)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient),
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

                    if (searchVisible) {// Search Bar UI
                        CustomSearchOTF(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            onSearchClick = {
                                // Optional handling of search action but lets not
                            },
                            onClearClick = {
                                searchQuery = ""
                                searchVisible = false
                            },
                            label = "Search Jobs",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Job Listings",
                            fontSize = 24.sp,
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { searchVisible = true }
                        )
                    }


                    LazyRow(// Tabs for selecting job categories
                        modifier = Modifier
                            .width(300.dp)
                            //.clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val categories = listOf("All", "Full-Time", "Part-Time")
                        items(categories) { category ->
                            Text(
                                text = category,
                                fontSize = 24.sp,
                                color = if (selectedTab == category) White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = category.lowercase() }
                            )
                        }
                    }

                    when {// Job Listings Content
                        isLoading -> {
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = SKyberYellow)
                            }
                        }
                        filteredJobListings.isEmpty() -> {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No Job Listings Available",
                                    color = SKyberRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            }
                        }
                        else -> {
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
                    }

                }
            }
        }
    }
}