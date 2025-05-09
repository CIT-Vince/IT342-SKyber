package com.example.skyber.navigationbar.portalnavigator.ProjectTransparency

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.ProjectTransparencyCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.Project
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.SoftCardFontBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Projects(navController: NavHostController) {
    val projectReports = remember { mutableStateListOf<Project>() }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("All") }

    // Define project status tabs
    val statusTabs = listOf("All", "Ongoing", "Planning", "Completed", "Delayed")

    // Load projects from Firebase - with improved error handling
    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("ProjectTransparency")
            .get().addOnSuccessListener { snapshot ->
                projectReports.clear()
                snapshot.children.forEach { child ->
                    val project = child.getValue(Project::class.java)
                    if (project != null) {
                        projectReports.add(project)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    // Filter projects - use derivedStateOf for better performance
    val filteredProjects by remember(searchQuery, selectedTab, projectReports.size) {
        derivedStateOf {
            projectReports
                .filter { project ->
                    when (selectedTab) {
                        "All" -> true
                        else -> project.status.equals(selectedTab, ignoreCase = true)
                    }
                }
                .filter { project ->
                    searchQuery.isEmpty() ||
                            project.projectName.contains(searchQuery, ignoreCase = true) ||
                            project.description.contains(searchQuery, ignoreCase = true)
                }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Content area with blue header and white content
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Blue header with title and subtitle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                            )
                        )
                        .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 24.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Projects",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Folder icon/emoji
                            Text(
                                text = "📁",
                                fontSize = 24.sp
                            )
                        }

                        Text(
                            text = "Discover ongoing and completed projects shaping our community's future!",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 8.dp, end = 64.dp)
                        )
                    }
                }

                // White content area
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FA))
                ) {
                    Column {
                        // Search and filter card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                // Search bar
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Search projects...") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(28.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray,
                                        focusedBorderColor = SKyberBlue
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = "Search",
                                            tint = Color.Gray
                                        )
                                    },
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Filter tabs
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(statusTabs) { tab ->
                                        StatusFilterTab(
                                            text = tab,
                                            selected = selectedTab == tab,
                                            onClick = { selectedTab = tab }
                                        )
                                    }
                                }
                            }
                        }

                        // Projects content area
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = SKyberBlue)
                            }
                        } else if (filteredProjects.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isNotEmpty())
                                        "No projects found matching your search"
                                    else
                                        "No ${if (selectedTab == "All") "" else selectedTab.lowercase()} projects available",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(filteredProjects) { project ->
                                    // Using your existing ProjectTransparencyCard
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
                                }

                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusFilterTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) SKyberBlue else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) SKyberBlue else Color.LightGray,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Gray,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
        )
    }
}