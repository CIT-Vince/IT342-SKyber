package com.example.skyber.navigationbar.portalnavigator.ProjectTransparency

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsProject(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val project = navController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newProjectName by rememberSaveable { mutableStateOf("") }
    var newProjectDescription by rememberSaveable { mutableStateOf("") }
    var newStatus by rememberSaveable { mutableStateOf("") }
    var newStartDate by rememberSaveable { mutableStateOf("") }
    var newEndDate by rememberSaveable { mutableStateOf("") }
    var newBudget by rememberSaveable { mutableStateOf("") }
    var newProjectManager by rememberSaveable { mutableStateOf("") }
    var newProjectMembers by rememberSaveable { mutableStateOf("") }
    var newStakeholders by rememberSaveable { mutableStateOf("") }
    var newSustainabilityGoals by rememberSaveable { mutableStateOf("") }
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

    LaunchedEffect(isEditMode) {
        if (isEditMode && project != null) {
            newProjectName = project.projectName
            newProjectDescription = project.description
            newStatus = project.status
            newStartDate = project.startDate
            newEndDate = project.endDate
            newBudget = project.budget
            newProjectManager = project.projectManager
            newProjectMembers = project.teamMembers
            newStakeholders = project.stakeholders
            newSustainabilityGoals = project.sustainabilityGoals
        }
    }

    if (project == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else { // Display project details
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Blue header with back button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(SKyberBlue)
                    .align(Alignment.TopCenter)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Project Details",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Main content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 56.dp) // Account for the header
            ) {
                // Banner image - could be a placeholder or project image
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1)),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            )
                    )
                }

                // Main project info card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(0.dp), // Flat edges
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            // Status Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (project.status.lowercase()) {
                                            "complete", "completed" -> Color(0xFF4CAF50)
                                            else -> SKyberBlue
                                        }
                                    )
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = project.status.uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Project Name
                            Text(
                                text = project.projectName,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Budget Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF2196F3))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "BUDGET: ₱${project.budget}",
                                        fontSize = 14.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                // Sustainability Goals Badge
                                if (project.sustainabilityGoals.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color(0xFF4CAF50))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "SUSTAINABILITY GOAL",
                                            fontSize = 14.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Project Description
                            Text(
                                text = project.description,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Date Row with Icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Date",
                                    tint = SKyberBlue,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "${project.startDate} - ${project.endDate}",
                                    fontSize = 15.sp,
                                    color = Color.DarkGray
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Progress Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Project Progress",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )

                                // Calculate completion percentage based on status
                                val progressPercentage = when (project.status.lowercase()) {
                                    "complete", "completed", "finished" -> 1f
                                    "ongoing", "active", "in-progress" -> 0.5f
                                    "planning", "upcoming" -> 0.1f
                                    else -> 0f
                                }

                                Text(
                                    text = "${(progressPercentage * 100).toInt()}%",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SKyberBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Progress Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.LightGray.copy(alpha = 0.5f))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(
                                            when (project.status.lowercase()) {
                                                "complete", "completed", "finished" -> 1f
                                                "ongoing", "active", "in-progress" -> 0.5f
                                                "planning", "upcoming" -> 0.1f
                                                else -> 0f
                                            }
                                        )
                                        .height(8.dp)
                                        .background(Color(0xFF4CAF50))
                                )
                            }
                        }
                    }
                }

                // Project Team Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SKyberBlue),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            // Project Team Section Title
                            Text(
                                text = "Project Team",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Project Manager Section
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar placeholder (could be replaced with actual images)
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = project.projectManager.firstOrNull()?.toString()
                                            ?: "P",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = project.projectManager,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Box(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color.Red)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "PROJECT MANAGER",
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Team Members Section
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = "Team Members",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "Team Members",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = project.teamMembers,
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    lineHeight = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Stakeholders Section
                            Column {
                                Text(
                                    text = "Stakeholders",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Display stakeholders as a bulleted list
                                project.stakeholders.split(",").forEach { stakeholder ->
                                    if (stakeholder.trim().isNotEmpty()) {
                                        Row(
                                            verticalAlignment = Alignment.Top,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(6.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.White)
                                                    .align(Alignment.CenterVertically)
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = stakeholder.trim(),
                                                fontSize = 14.sp,
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Spacer at bottom
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Edit button for admin
                    if (user?.role == "ADMIN") {
                        Button(
                            onClick = { isEditMode = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue)
                        ) {
                            Text(
                                text = "Edit Project Details",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
    }


/*Box(
    modifier = Modifier
        .height(200.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        //.background(Color.Gray.copy(alpha = 0.1f)),
    contentAlignment = Alignment.Center
) {
    if (!project.imageData.isNullOrEmpty()) {
        Base64Image(
            base64String = project.imageData,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        Text(
            text = "No image available",
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}
*/