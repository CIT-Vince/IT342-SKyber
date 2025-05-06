package com.example.skyber.navigationbar.portalnavigator.ProjectTransparency

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.skyber.ModularFunctions.ProjectTransparencyCard
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.Screens
import com.example.skyber.dataclass.Project
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.navigationbar.portalnavigator.PortalNav
import com.example.skyber.navigationbar.portalnavigator.PortalNavHandler
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Projects(navController: NavHostController) {
    val ProjectReports = remember { mutableStateListOf<Project>() }
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
    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("ProjectTransparency")
            .get().addOnSuccessListener { snapshot ->
                ProjectReports.clear()
                snapshot.children.forEach { child ->
                    val projectTransparency = child.getValue(Project::class.java)
                    Log.d("ProjectItem", projectTransparency.toString())
                    if (projectTransparency!= null) {
                        ProjectReports.add(projectTransparency)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                //Log.e("Project Report's Fetch", "Failed to load Reports", it)
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
                            label = "Search Projects",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Projects",
                            fontSize = 24.sp,
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { searchVisible = true }
                        )
                    }

                    val filteredReports = remember(searchQuery) {// Filtered reports based on the search query
                        if (searchQuery.isEmpty()) {
                            ProjectReports
                        } else {
                            ProjectReports.filter {
                                it.projectName.contains(searchQuery, ignoreCase = true)
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        if (filteredReports.isEmpty()) {
                            item {
                                Text("",)
                            }
                        } else {
                            items(filteredReports.reversed()) { project ->
                                ProjectTransparencyCard(
                                    backgroundColor = White,
                                    fontColor = SoftCardFontBlue,
                                    project = project,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "project",
                                            project
                                        )
                                        navController.navigate(Screens.DetailsProject.screen)
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
