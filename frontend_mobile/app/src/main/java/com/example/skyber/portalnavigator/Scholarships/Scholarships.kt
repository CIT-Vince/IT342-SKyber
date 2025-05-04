package com.example.skyber.portalnavigator.Scholarships

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
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.ScholarshipCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Scholarships(navController: NavHostController) {
    val allScholarships = remember { mutableStateListOf<Scholarship>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists
    var selectedTab by remember { mutableStateOf("All") }

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

    val filteredScholarship = when (selectedTab) {
        "Private" -> allScholarships.filter { it.type.equals("Private", ignoreCase = true) }
        "Public" -> allScholarships.filter { it.type.equals("Public", ignoreCase = true) }
        else -> allScholarships
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("Scholarships")
            .get().addOnSuccessListener { snapshot ->
                allScholarships.clear()
                snapshot.children.forEach { child ->
                    val scholarship = child.getValue(Scholarship::class.java)
                    Log.d("JobListingItem", scholarship.toString())
                    if (scholarship != null) {
                        allScholarships.add(scholarship)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                Log.e("ScholarshipsFetch", "Failed to load scholarships list", it)
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
                                "Scholarships",
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
                                "All",
                                fontSize = 24.sp,
                                color = if (selectedTab == "All") White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = "All" }
                            )
                            Text(
                                "Public",
                                fontSize = 24.sp,
                                color = if (selectedTab == "Public") White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = "Public" }
                            )
                            Text(
                                "Private",
                                fontSize = 24.sp,
                                color = if (selectedTab == "Private") White else Color.Gray,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { selectedTab = "Private" }
                            )
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

                        Button(
                            onClick = {
                                navController.navigate(Screens.PostScholarship.screen)
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
                                    text = "Post Scholarships",
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