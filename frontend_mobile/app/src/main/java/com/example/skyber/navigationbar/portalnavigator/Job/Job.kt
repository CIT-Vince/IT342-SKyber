package com.example.skyber.navigationbar.portalnavigator.Job

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SKyberBlue
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.FilterList

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Job(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("All") }
    val allJobListings = remember { mutableStateListOf<JobListing>() }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    // Filtered job listings based on the selected tab and search query
    val filteredJobListings = allJobListings.filter { job ->
        val matchesTab = selectedTab == "All" ||
                (selectedTab == "Full Time" && job.employementType.equals("Full-time", ignoreCase = true)) ||
                (selectedTab == "Part Time" && job.employementType.equals("Part-time", ignoreCase = true))

        val matchesSearch = searchQuery.isEmpty() ||
                job.companyName.contains(searchQuery, ignoreCase = true) ||
                job.jobTitle.contains(searchQuery, ignoreCase = true) ||
                job.address.contains(searchQuery, ignoreCase = true) // Changed from location to address

        matchesTab && matchesSearch
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("JobListings")
            .get().addOnSuccessListener { snapshot ->
                allJobListings.clear()
                snapshot.children.forEach { child ->
                    val joblisting = child.getValue(JobListing::class.java)
                    if (joblisting != null) {
                        allJobListings.add(joblisting)
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
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SKyberBlue)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header banner - more compact for mobile
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp) // Reduced height for mobile
                                .background(SKyberBlue)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 16.dp), // Smaller padding
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Title with briefcase icon
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Job Opportunities ",
                                        fontSize = 22.sp, // Smaller for mobile
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Work,
                                        contentDescription = "Job",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp) // Smaller icon
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Find your dream job and start building your career with these amazing opportunities!",
                                    fontSize = 14.sp, // Smaller for mobile
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Search and filter section - simplified for mobile
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp) // Smaller padding
                        ) {
                            // Search bar - rounded to match screenshot
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                placeholder = {
                                    Text(
                                        "Search jobs by title, company or keywords...",
                                        fontSize = 14.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                shape = RoundedCornerShape(24.dp),
                                colors = outlinedTextFieldColors(
                                    focusedBorderColor = SKyberBlue,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Filter row - simplified to match screenshot
                            // Replace the filter row with this updated version
                            // Replace the filter row with this simplified, centered version
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center, // Center the buttons
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // All button
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(80.dp)
                                        .background(
                                            color = if (selectedTab == "All") SKyberBlue else Color.White,
                                            shape = CircleShape
                                        )
                                        .border(1.dp, if (selectedTab == "All") SKyberBlue else Color.LightGray, CircleShape)
                                        .clickable { selectedTab = "All" },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "All",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selectedTab == "All") Color.White else Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp)) // Space between buttons

                                // Full Time (F) button
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(80.dp)
                                        .background(
                                            color = if (selectedTab == "Full Time") SKyberBlue else Color.White,
                                            shape = CircleShape
                                        )
                                        .border(1.dp, if (selectedTab == "Full Time") SKyberBlue else Color.LightGray, CircleShape)
                                        .clickable { selectedTab = "Full Time" },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Full Time",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selectedTab == "Full Time") Color.White else Color.Gray
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp)) // Space between buttons

                                // Part Time (P) button
                                Box(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(80.dp)
                                        .background(
                                            color = if (selectedTab == "Part Time") SKyberBlue else Color.White,
                                            shape = CircleShape
                                        )
                                        .border(1.dp, if (selectedTab == "Part Time") SKyberBlue else Color.LightGray, CircleShape)
                                        .clickable { selectedTab = "Part Time" },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Part Time",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (selectedTab == "Part Time") Color.White else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    // Job listings - more compact for mobile
                    if (filteredJobListings.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No job opportunities found",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else {
                        items(filteredJobListings) { job ->
                            CompactJobCard(job = job) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "joblisting", job
                                )
                                navController.navigate(Screens.DetailsJob.screen)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(72.dp)) // Added space for bottom nav
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterButtonCompact(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = if (selected) SKyberBlue else Color.White,
                shape = CircleShape
            )
            .border(1.dp, if (selected) SKyberBlue else Color.LightGray, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else Color.Gray
        )
    }
}

@Composable
private fun CompactJobCard(job: JobListing, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp), // Lower elevation
        shape = RoundedCornerShape(8.dp) // Less rounded corners
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp) // Less padding
        ) {
            // Job title
            Text(
                text = job.jobTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp, // Smaller text
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Company and location in row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "Company",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp) // Smaller icon
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = job.companyName,
                    fontSize = 12.sp, // Smaller text
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp) // Smaller icon
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = job.address, // Changed from location to address
                    fontSize = 12.sp, // Smaller text
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bottom row with time posted and job type
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Posted date
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Posted",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp) // Smaller icon
                    )

                    Spacer(modifier = Modifier.width(2.dp))

                    Text(
                        text = "Recently posted",
                        fontSize = 12.sp, // Smaller text
                        color = Color.Gray
                    )
                }

                // Job type badge - more compact
                if (job.employementType.equals("Full-time", ignoreCase = true)) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = SKyberBlue,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "FULL TIME",
                            fontSize = 10.sp, // Smaller text
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(
                                color = SKyberYellow,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "PART TIME",
                            fontSize = 10.sp, // Smaller text
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}