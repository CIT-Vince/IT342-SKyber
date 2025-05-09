package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.AnnouncementCard
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.R
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.User
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    val allAnnouncements = remember { mutableStateListOf<Announcement>() }
    val allCandidates = remember { mutableStateListOf<CandidateProfile>() }
    val scrollState = rememberScrollState()

    // Load data from Firebase
    LaunchedEffect(Unit) {
        refreshUserProfile()
        try {
            val announcementFetch = async {
                FirebaseHelper.databaseReference.child("Announcements").get().await()
                    .children.mapNotNull { it.getValue(Announcement::class.java) }
            }
            val candidatesFetch = async {
                FirebaseHelper.databaseReference.child("Candidates").get().await()
                    .children.mapNotNull { it.getValue(CandidateProfile::class.java) }
            }

            allAnnouncements.clear()
            allAnnouncements.addAll(announcementFetch.await())

            allCandidates.clear()
            allCandidates.addAll(candidatesFetch.await())
        } catch (e: Exception) {
            // Handle error
        }
    }

    if (user == null) {
        // Loading state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFFFC107))
        }
    } else {
        Scaffold { _ ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                        )
                    )
            ) {
                // Background particle effect (simplified for mobile)
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White.copy(alpha = 0.3f),
                    particleCount = 30,
                    backgroundColor = Color.Transparent
                )

                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    // Top app bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Logo and name
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // You can replace this with the actual SKyber logo
                            Text(
                                text = "SKYBER",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // User profile
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "User",
                                color = Color.White,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            // Profile image with circle background
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.firstName?.firstOrNull()?.toString() ?: "U",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Hero section
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Multi-colored hero text
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                                    append("Empowering ")
                                }
                                withStyle(style = SpanStyle(color = Color(0xFFE53935), fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                                    append("Communities")
                                }
                            },
                            lineHeight = 38.sp
                        )

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color(0xFF2196F3), fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                                    append("Securing ")
                                }
                                withStyle(style = SpanStyle(color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                                    append("the ")
                                }
                                withStyle(style = SpanStyle(color = Color(0xFFFFC107), fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
                                    append("Future.")
                                }
                            },
                            lineHeight = 38.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Description text
                        Text(
                            text = "Transform your community with SKyber — an AI-powered security platform bringing safety and efficiency to neighborhoods and campuses.",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )

//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        // "View Announcements" Button
//                        Button(
//                            onClick = { navController.navigate(Screens.Announcements.screen) },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = Color(0xFF2196F3)
//                            ),
//                            shape = RoundedCornerShape(24.dp),
//                            modifier = Modifier.height(48.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Announcement,
//                                contentDescription = null,
//                                tint = Color.White,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "View Announcements",
//                                color = Color.White,
//                                fontWeight = FontWeight.Medium
//                            )
//                        }
                    }

                    // Community illustration (simplified for mobile)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // This would ideally be replaced with the actual community circle graphic
                        // For now using a placeholder
                        Image(
                            painter = painterResource(id = R.drawable.communitylogo),
                            contentDescription = "Community graphic",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Quicklinks section with cards
                    Text(
                        text = "Quick Access",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Projects card
                        QuickAccessCard(
                            icon = Icons.Default.Construction,
                            title = "Projects",
                            onClick = { navController.navigate(Screens.Projects.screen) },
                            modifier = Modifier.weight(1f)
                        )

                        // Announcements card
//                        QuickAccessCard(
//                            icon = Icons.Default.Announcement,
//                            title = "Announcements",
//                            onClick = { navController.navigate(Screens.Announcements.screen) },
//                            modifier = Modifier.weight(1f)
//                        )

                        // Candidates card
                        QuickAccessCard(
                            icon = Icons.Default.Group,
                            title = "Candidates",
                            onClick = { navController.navigate(Screens.SKcandidates.screen) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Latest announcement section
                    Text(
                        text = "Latest News",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )

                    if (allAnnouncements.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFFC107))
                        }
                    } else {
                        val latestAnnouncement = allAnnouncements.lastOrNull()
                        if (latestAnnouncement != null) {
                            Box(
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                AnnouncementCard(
                                    backgroundColor = Color.White,
                                    fontColor = Color(0xFF1E3A8A),
                                    announcement = latestAnnouncement,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "announcement", latestAnnouncement
                                        )
                                        navController.navigate(Screens.DetailsAnnouncement.screen)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}