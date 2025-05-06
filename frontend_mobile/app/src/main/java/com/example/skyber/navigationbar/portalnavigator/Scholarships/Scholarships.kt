package com.example.skyber.navigationbar.portalnavigator.Scholarships

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
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.ScholarshipCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.navigationbar.portalnavigator.PortalNav
import com.example.skyber.navigationbar.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Scholarships(navController: NavHostController) {
    val allScholarships = remember { mutableStateListOf<Scholarship>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists
    var selectedTab by remember { mutableStateOf("All") }
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

    val filteredScholarship = remember(searchQuery, selectedTab) {
        val filteredByTab = when (selectedTab) {
            "Private" -> allScholarships.filter { it.type.equals("Private", ignoreCase = true) }
            "Public" -> allScholarships.filter { it.type.equals("Public", ignoreCase = true) }
            else -> allScholarships
        }
        if (searchQuery.isEmpty()) {
            filteredByTab
        } else {
            filteredByTab.filter {
                it.title.contains(searchQuery, ignoreCase = true) // Filter by name
            }
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("Scholarships")
            .get().addOnSuccessListener { snapshot ->
                allScholarships.clear()
                snapshot.children.forEach { child ->
                    val scholarship = child.getValue(Scholarship::class.java)
                    if (scholarship != null) {
                        allScholarships.add(scholarship)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
        kotlinx.coroutines.delay(5000)
        isLoading = false
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

                    if (searchVisible) {
                        CustomSearchOTF(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            onSearchClick = {
                                // Optional: handle search action
                            },
                            onClearClick = {
                                searchQuery = ""
                                searchVisible = false
                            },
                            label = "Search Scholarships",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Scholarships",
                            fontSize = 24.sp,
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { searchVisible = true }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf("All", "Public", "Private").forEach { category ->
                            Text(
                                text = category,
                                fontSize = 24.sp,
                                color = if (selectedTab == category) White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = category }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    when {
                        isLoading -> {
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = SKyberYellow)
                            }
                        }
                        filteredScholarship.isEmpty() -> {
                            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    "No Scholarships available",
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
                                items(filteredScholarship.reversed()) { scholarship ->
                                    ScholarshipCard(
                                        backgroundColor = SoftCardContainerPast,
                                        fontColor = SoftCardFontPast,
                                        scholarship = scholarship,
                                        onClick = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "scholarship",
                                                scholarship
                                            )
                                            navController.navigate(Screens.DetailsScholarship.screen)
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