package com.example.skyber.navigationbar

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VolunteerHub(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("Ongoing") }
    val allVolunteerPosts = remember { mutableListOf<VolunteerPost>() }
    // Filter for event status on selected tab
    val filteredPosts = when (selectedTab) {
        "Ongoing" -> allVolunteerPosts.filter { it.status == "Ongoing" }
        "Completed" -> allVolunteerPosts.filter { it.status == "Completed" }
        else -> allVolunteerPosts
    }

    LaunchedEffect(Unit) {
        FirebaseHelper.databaseReference.child("VolunteerHubEvent")
            .get().addOnSuccessListener { snapshot ->
                allVolunteerPosts.clear()
                snapshot.children.forEach { child ->
                    val event = child.getValue(VolunteerPost::class.java)
                    Log.d("VolunteerEventItem", event.toString())
                    if (event!= null) {
                        allVolunteerPosts.add(event)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("VolunteerHubFetch", "Failed to load volunteer events", it)
            }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(SKyberDarkBlue)
                .fillMaxHeight()
                .fillMaxWidth(),

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
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(0.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(White),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Volunteer Hub",
                    fontSize = 24.sp,
                    color = SKyberBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .width(300.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Ongoing",
                        fontSize = 24.sp,
                        color = if (selectedTab == "Ongoing") SKyberBlue else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { selectedTab = "Ongoing" }
                    )
                    Text(
                        "Closed",
                        fontSize = 24.sp,
                        color = if (selectedTab == "Completed") SKyberBlue else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { selectedTab = "Completed" }
                    )
                }

                if(allVolunteerPosts.isEmpty()){
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text("No Events Right Now", color = SKyberRed, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                }else{
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(12.dp)
                    ) {
                        items(filteredPosts.reversed()) { event ->
                            VolunteerCard(volunteerPost = event,
                                backgroundColor = SoftCardContainerBlue,
                                fontColor = SoftCardFontBlue,
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("volunteerPost", event)
                                    navController.navigate(Screens.DetailsVolunteerHub.screen)
                                })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }


                Button(
                    onClick = {
                        navController.navigate(Screens.PostVolunteerHub.screen)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue),
                    modifier = Modifier
                        .padding(16.dp)
                        .width(250.dp)
                ) {
                    Text(text = "Post Event")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add event"
                    )
                }

            }//End of content column
        }//end of Main column layout
    }//end of scaffold
}


@Preview(showBackground = true)
@Composable
fun Preview(){
    val navController = rememberNavController()
    VolunteerHub(navController)
}
