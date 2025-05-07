package com.example.skyber.navigationbar.portalnavigator.Announcement

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
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.skyber.ModularFunctions.AnnouncementCard
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun Announcements(navController: NavHostController) {
    val announcements = remember { mutableStateListOf<Announcement>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists
    var searchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }//search bar state management

    // Filtered announcements based on the search query
    val filteredAnnouncements = if (searchQuery.isEmpty()) {
        announcements
    } else {
        announcements.filter {
            it.title.contains(searchQuery, ignoreCase = true) // Search based on the title
        }
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
        LaunchedEffect(Unit) {
            isLoading = true
            FirebaseHelper.databaseReference.child("Announcements")
            .get().addOnSuccessListener { snapshot ->
                announcements.clear()
                snapshot.children.forEach { child ->
                    val announcement = child.getValue(Announcement::class.java)
                    if (announcement != null) {
                        announcements.add(announcement)
                    }
                }
                    isLoading = false
                }
            .addOnFailureListener {
                //Log.e("AnnouncementFetch", "Failed to load projects", it)
                isLoading = true
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
        Scaffold() {
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
                                label = "Search Announcements",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Announcements",
                                fontSize = 24.sp,
                                color = White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clickable { searchVisible = true }
                            )
                        }

                        if (announcements.isEmpty()) {
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                Text(
                                    "No Announcements Right Now",
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
                                contentPadding = PaddingValues(10.dp)
                            ) {
                                if (filteredAnnouncements.isEmpty()) {
                                    item {
                                        Text("")
                                    }
                                } else {
                                    items(filteredAnnouncements.reversed()) { announcement ->
                                        AnnouncementCard(
                                            backgroundColor = SoftCardContainerBlue,
                                            fontColor = SoftCardFontBlue,
                                            announcement = announcement,
                                            onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "announcement",
                                                    announcement
                                                )
                                                navController.navigate(Screens.DetailsAnnouncement.screen)
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
}


/*@Preview(showBackground = true)
@Composable
fun PortalPreview() {
    val navController = rememberNavController()
    Announcements(navController = navController)
}*/
