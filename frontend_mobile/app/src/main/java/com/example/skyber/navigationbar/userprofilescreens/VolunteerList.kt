package com.example.skyber.navigationbar.userprofilescreens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.VolunteerCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.White


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VolunteerList(navController: NavHostController, userProfile: MutableState<User?>) {
    val appliedEvents = remember { mutableStateListOf<VolunteerPost>() }//User applied events
    val user = userProfile.value
    val tabs = listOf("Ongoing", "Completed")
    var selectedTab by remember { mutableStateOf("Ongoing") }
    Log.d("VolunteerDebug", "userProfile: $user")
    val filteredAppliedEvents = when (selectedTab) {//Tab status same logic with volunteerHub
        "Ongoing" -> appliedEvents.filter { it.status == "Ongoing" }
        "Completed" -> appliedEvents.filter { it.status == "Completed" }
        else -> appliedEvents
    }
    LaunchedEffect(userProfile.value?.volunteeredActivities) {//logic for fetching user events applied and record of VolunteerHubEvents
        val volunteeredActivities = userProfile.value?.volunteeredActivities ?: return@LaunchedEffect
        if (volunteeredActivities.isEmpty()) {
            Log.d("VolunteerDebug", "No volunteeredActivities found")
            return@LaunchedEffect
        }

        Log.d("VolunteerDebug", "Volunteered IDs: $volunteeredActivities")

        FirebaseHelper.databaseReference
            .child("VolunteerHubEvent")
            .get()
            .addOnSuccessListener { postSnapshot ->
                Log.d("VolunteerDebug", "Fetched VolunteerHubEvent: ${postSnapshot.childrenCount}")

                val matchedPosts = postSnapshot.children.mapNotNull { postSnap -> //Logic for matching user applied events and VolunteerHubEvents
                    val post = postSnap.getValue(VolunteerPost::class.java)
                    val eventId = post?.id
                    if (eventId in volunteeredActivities) {
                        Log.d("VolunteerDebug", "MATCH FOUND: eventId=$eventId")
                        post
                    } else {
                        null
                    }
                }
                Log.d("VolunteerDebug", "Matched Posts Count: ${matchedPosts.size}")
                appliedEvents.clear()
                appliedEvents.addAll(matchedPosts)
            }
            .addOnFailureListener {
                Log.e("VolunteerDebug", "Failed to fetch VolunteerHubEvent", it)
            }
    }


    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(SKyberDarkBlue)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderBar(
                trailingContent = {
                    NotificationHandler()
                }
            )

            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(0.dp)
                    .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                    .background(White),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    if (appliedEvents.isEmpty()) {
                        item {
                            Text(
                                text = "No applied volunteer events yet!",
                                color = SKyberBlue,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        if (appliedEvents.isEmpty()) {
                            item {
                                Text(
                                    text = "No applied volunteer events yet!",
                                    color = SKyberBlue,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        } else {
                            item {//Header
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        tabs.forEach { tab ->
                                            Text(
                                                text = tab,
                                                fontSize = 24.sp,
                                                color = if (selectedTab == tab) SKyberBlue else Color.Gray,
                                                fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                                modifier = Modifier
                                                    .clickable { selectedTab = tab }
                                                    .padding(8.dp)
                                            )
                                        }
                                    }

                            }

                            items(filteredAppliedEvents.reversed()) { event ->
                                VolunteerCard(
                                    volunteerPost = event,
                                    backgroundColor = SKyberBlue,
                                    fontColor = White,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("volunteerPost", event)
                                        navController.navigate(Screens.DetailsVolunteerHub.screen)
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



/*
@Preview(showBackground = true)//Use this preview for screens that need mock up user data
@Composable
fun Preview() {
    val user = User(
        id = "static123",
        firstname = "Preview",
        lastname = "User",
        email = "preview@example.com",
        password = "password123",
        dob = "01/01/1990",
        gender = "Non-binary",
        role = "viewer"
    )
    val staticUserProfile = remember { mutableStateOf<User?>(user) }
    val navController = rememberNavController()
    VolunteerList(navController,  userProfile = staticUserProfile)
}

*/