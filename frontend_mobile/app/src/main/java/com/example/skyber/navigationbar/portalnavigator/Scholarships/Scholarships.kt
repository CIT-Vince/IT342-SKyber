package com.example.skyber.navigationbar.portalnavigator.Scholarships

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.ui.theme.SKyberBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Scholarships(navController: NavHostController) {
    val allScholarships = remember { mutableStateListOf<Scholarship>() }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

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
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
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
                                text = "Scholarships & Opportunities",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            // Graduation cap icon/emoji
                            Text(
                                text = "🎓",
                                fontSize = 24.sp
                            )
                        }

                        Text(
                            text = "Find financial support for your studies and make your dreams come true with these amazing scholarship opportunities!",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 8.dp, end = 64.dp)
                        )
                    }
                }

                // White search and content area
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
                                    placeholder = { Text("Search scholarships...") },
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("All", "Public", "Private").forEach { tab ->
                                        FilterTab(
                                            text = tab,
                                            selected = selectedTab == tab,
                                            onClick = { selectedTab = tab },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        // Scholarships content area
                        if (isLoading) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = SKyberBlue)
                            }
                        } else if (filteredScholarship.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No scholarships available",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(filteredScholarship) { scholarship ->
                                    ScholarshipItem(
                                        scholarship = scholarship,
                                        onClick = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "scholarship",
                                                scholarship
                                            )
                                            navController.navigate(Screens.DetailsScholarship.screen)
                                        }
                                    )
                                }

                                // Add some padding at the bottom
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
fun FilterTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (selected) SKyberBlue else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) SKyberBlue else Color.LightGray,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color.Gray,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ScholarshipItem(
    scholarship: Scholarship,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Colored header based on type
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Color(
                            if (scholarship.type.equals("Public", ignoreCase = true))
                                0xFF03A9F4
                            else
                                0xFFE91E63
                        ).copy(alpha = 0.2f)
                    )
            ) {
                // Type badge
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (scholarship.type.equals("Public", ignoreCase = true))
                                Color(0xFF03A9F4)
                            else
                                Color(0xFFE91E63)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = scholarship.type.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Title
                Text(
                    text = scholarship.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = scholarship.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Contact and Apply buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Contact button
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { /* Handle contact action */ }
                            .border(1.dp, Color.Gray, CircleShape)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✉️ Contact",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }

                    // Apply button
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onClick() }
                            .background(SKyberBlue)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🔗 Apply",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}