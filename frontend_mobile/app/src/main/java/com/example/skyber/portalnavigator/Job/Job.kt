package com.example.skyber.portalnavigator.Job

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.JobListingCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardContainerLavender
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Job(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("Full-Time") }
    val allJobListings = remember { mutableStateListOf<JobListing>() }
    var isLoading by remember { mutableStateOf(true) }//Add this later to all lists

    // Filter for event status on selected tab
    val filteredJobListings = when (selectedTab) {
        "Part-Time" -> allJobListings.filter { it.category == "Part-Time" }
        "Full-Time" -> allJobListings.filter { it.category == "Full-Time" }
        else -> allJobListings
    }

    LaunchedEffect(Unit) {
        isLoading = true
        FirebaseHelper.databaseReference.child("JobListing")
            .get().addOnSuccessListener { snapshot ->
                allJobListings.clear()
                snapshot.children.forEach { child ->
                    val joblisting = child.getValue(JobListing::class.java)
                    Log.d("JobListingItem", joblisting.toString())
                    if (joblisting != null) {
                        allJobListings.add(joblisting)
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                Log.e("VolunteerHubFetch", "Failed to load volunteer events", it)
                isLoading = false
            }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
    } else {
        Scaffold() { innerPadding ->
            Column(
                modifier = Modifier
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

                PortalNav(
                    trailingContent = {
                        PortalNavHandler(navController = navController)
                        Text(
                            "Jobs",
                            fontSize = 24.sp,
                            color = SKyberBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Full-Time",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Full-Time") SKyberBlue else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Full-Time" }
                        )
                        Text(
                            "Part-Time",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Part-Time") SKyberBlue else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Part-Time" }
                        )
                    }

                    if(filteredJobListings.isEmpty()){
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text("No Job Listings available", color = SKyberRed, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        }
                    }else{
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(filteredJobListings.reversed()) { joblisting ->
                                JobListingCard(
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    joblisting = joblisting,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set("joblisting",joblisting)
                                        navController.navigate(Screens.DetailsJob.screen)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }


                    Button(
                        onClick = {
                            navController.navigate(Screens.PostJob.screen)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue),
                        modifier = Modifier
                            .padding(16.dp)
                            .width(250.dp)
                    ) {
                        Text(text = "Post Job Listing")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "add job listing"
                        )
                    }
                }
            }
        }
    }
}