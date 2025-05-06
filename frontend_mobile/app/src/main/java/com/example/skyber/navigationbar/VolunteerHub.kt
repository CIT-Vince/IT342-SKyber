package com.example.skyber.navigationbar

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.VolunteerCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VolunteerHub(navController: NavHostController, userProfile: MutableState<User?>) {
    var selectedTab by remember { mutableStateOf("active") }
    val allVolunteerPosts = remember { mutableStateListOf<VolunteerPost>() }
    var isLoading by remember { mutableStateOf(true) }
    var searchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val user = userProfile.value
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

    val filteredPosts = allVolunteerPosts
        .filter { selectedTab == "All" || it.status == selectedTab }
        .filter {
            searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true)
        }

    LaunchedEffect(Unit) {
        FirebaseHelper.databaseReference.child("Volunteers")
            .get().addOnSuccessListener { snapshot ->
                allVolunteerPosts.clear()
                snapshot.children.forEach { child ->
                    val event = child.getValue(VolunteerPost::class.java)
                    if (event != null) {
                        allVolunteerPosts.add(event)
                        isLoading = false
                    }
                }
            }
            .addOnFailureListener {
                isLoading = false
            }

        kotlinx.coroutines.delay(3000)
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
            modifier = Modifier.fillMaxSize(),
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
                            label = "Search Volunteer Hub",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Volunteer Hub",
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
                            .width(340.dp)
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
                            "Active",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Active") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Active" }
                        )
                        Text(
                            "Closed",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Completed") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Completed" }
                        )
                    }

                    if (allVolunteerPosts.isEmpty()) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                "No Events Right Now",
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
                            items(filteredPosts.reversed()) { event ->
                                VolunteerCard(volunteerPost = event,
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "volunteerPost",
                                            event
                                        )
                                        navController.navigate(Screens.DetailsVolunteerHub.screen)
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    if (user != null) {
                        if (user.role != "ADMIN") {

                        } else {
                            Button(
                                onClick = {
                                    navController.navigate(Screens.PostVolunteerHub.screen)
                                },
                                modifier = Modifier
                                    .width(130.dp)
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
                                ) {
                                    Text(
                                        text = "Post",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }//end of Main column layout
                }
            }//end of scaffold
        }
    }
}