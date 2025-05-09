package com.example.skyber.navigationbar.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.AnnouncementCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.ui.theme.SKyberBlue

private fun showAnnouncementToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Announcements(navController: NavHostController) {
    val announcements = remember { mutableStateListOf<Announcement>() }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val context = LocalContext.current

    // Get all available categories from announcements
    val categories = listOf("All", "News", "Event", "Notice", "Emergency")

    // Filter announcements based on search query AND selected category
    val filteredAnnouncements = announcements.filter { announcement ->
        val matchesSearch = announcement.title.contains(searchQuery, ignoreCase = true) ||
                announcement.content.contains(searchQuery, ignoreCase = true)

        val matchesCategory = selectedCategory == "All" ||
                announcement.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

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
                showToast(context, "Failed to load announcements")
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
                                text = "Announcements",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Announcement icon/emoji
                            Text(
                                text = "📢",
                                fontSize = 24.sp
                            )
                        }

                        Text(
                            text = "Check out the latest updates, events, and important announcements for our community!",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(top = 8.dp, end = 64.dp)
                        )
                    }
                }

                // White content area with rounded corners
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FA))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        // Search bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search announcements...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
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

                        // Category filters
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(categories) { category ->
                                CategoryFilterChip(
                                    category = category,
                                    selected = selectedCategory == category,
                                    onClick = { selectedCategory = category }
                                )
                            }
                        }

                        // Loading state
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = SKyberBlue)
                            }
                        }
                        // Empty state
                        else if (filteredAnnouncements.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (searchQuery.isEmpty() && selectedCategory == "All")
                                        "No announcements available"
                                    else
                                        "No matching announcements found",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        // Announcement list
                        else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(filteredAnnouncements) { announcement ->
                                    AnnouncementCard(
                                        backgroundColor = Color.White,
                                        fontColor = Color.Black,
                                        announcement = announcement,
                                        onClick = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                "announcement",
                                                announcement
                                            )
                                            navController.navigate(Screens.DetailsAnnouncement.screen)
                                        }
                                    )
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
fun CategoryFilterChip(
    category: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) SKyberBlue else Color.Transparent
    val textColor = if (selected) Color.White else Color.Gray
    val borderColor = if (selected) SKyberBlue else Color.LightGray

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
            .border(1.dp, borderColor, CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (category == "All") {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = category,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                color = textColor
            )
        }
    }
}