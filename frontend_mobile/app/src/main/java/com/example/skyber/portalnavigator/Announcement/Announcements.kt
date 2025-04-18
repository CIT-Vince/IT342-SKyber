package com.example.skyber.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.Cards.AnnouncementCard
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Announcements(navController: NavHostController) {
    val announcements = remember { mutableStateListOf<Announcement>() }
    LaunchedEffect(Unit) {
        FirebaseHelper.databaseReference.child("Announcements")
            .get().addOnSuccessListener { snapshot ->
                announcements.clear()
                snapshot.children.forEach { child ->
                    val announcement = child.getValue(Announcement::class.java)
                    if (announcement != null) {
                        announcements.add(announcement)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("AnnouncementFetch", "Failed to load projects", it)
            }
    }
    Scaffold() {  innerPadding ->
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
                    Text("Announcements", fontSize = 24.sp, color = SKyberBlue, fontWeight = FontWeight.Bold)
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
            ){
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(12.dp)
                ) {
                    items(announcements.reversed()) { announcement ->
                        AnnouncementCard(
                            backgroundColor = SKyberBlue,
                            fontColor = White,
                            announcement = announcement,
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("announcement", announcement)
                                navController.navigate(Screens.DetailsAnnouncement.screen)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Button(
                    onClick = {
                        navController.navigate(Screens.PostAnnouncement.screen)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue),
                    modifier = Modifier
                        .padding(16.dp)
                        .width(250.dp)
                ){
                    Text(text = "Post Announcement")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add announcement"
                    )
                }

            }
        }

    }
}



@Preview(showBackground = true)
@Composable
fun PortalPreview() {
    val navController = rememberNavController()
    Announcements(navController = navController)
}
