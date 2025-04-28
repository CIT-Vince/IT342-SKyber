package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.AnnouncementCard
import com.example.skyber.ModularFunctions.CandidateCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.User
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.NewspaperContainer
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    val allCandidates = remember { mutableStateListOf<CandidateProfile>() }
    val allAnnouncements = remember { mutableStateListOf<Announcement>() }
    LaunchedEffect(Unit) {
        refreshUserProfile()
        try {
            val announcementFetch = async {
                Log.d("DataFetch", "Fetching announcements")

                FirebaseHelper.databaseReference.child("Announcements").get().await()
                    .children.mapNotNull { it.getValue(Announcement::class.java) }
            }
            val candidatesFetch = async {
                Log.d("DataFetch", "Fetching candidates")

                FirebaseHelper.databaseReference.child("CandidateProfile").get().await()
                    .children.mapNotNull { it.getValue(CandidateProfile::class.java) }
            }

            val announcementsList = announcementFetch.await()
            Log.d("DataFetch", "Fetched ${announcementsList.size} announcements")

            val candidatesList = candidatesFetch.await()
            Log.d("DataFetch", "Fetched ${candidatesList.size} candidates")

            allAnnouncements.clear()
            allAnnouncements.addAll(announcementsList)
            Log.d("DataFetch", "Announcements list updated")
            allCandidates.clear()
            allCandidates.addAll(candidatesList)
            Log.d("DataFetch", "Candidates list updated")
        } catch (e: Exception) {
            Log.e("FetchError", "Failed to fetch data", e)
        }
    }

    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
        Scaffold() { innerPadding ->
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
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonPin,
                        tint = White,
                        contentDescription = "Person Pin",
                        modifier = Modifier
                            .size(50.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                            .height(40.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        // Display the current user name and double check if null kay mo crash
                        Text(text = "Hello, ", fontSize = 30.sp, color = White)
                        Text(
                            text = user.firstname ?: "User",
                            fontSize = 30.sp,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center

                ) {
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        tint = White,
                        contentDescription = "Location",
                        modifier = Modifier
                            .size(30.dp)

                    )
                    Text(
                        text = user.address ?: "",
                        fontSize = 25.sp,
                        color = White,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 2.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // --- News Header ---
                        item {
                            Text(
                                text = "News",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // --- News Content ---
                        if (allAnnouncements.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = SKyberYellow)
                                }
                            }
                        } else {
                            items(allAnnouncements.reversed()) { announcement ->
                                AnnouncementCard(
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    announcement = announcement,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "announcement", announcement
                                        )
                                        navController.navigate(Screens.DetailsAnnouncement.screen)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // --- Candidates Header ---
                        item {
                            Text(
                                text = "SK Candidates",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White,
                                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                            )
                        }

                        // --- Candidates Content ---
                        if (allCandidates.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = SKyberYellow)
                                }
                            }
                        } else {
                            items(allCandidates.reversed()) { candidate ->
                                CandidateCard(
                                    candidateProfile = candidate,
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "CandidateProfile", candidate
                                        )
                                        navController.navigate(Screens.DetailsSKcandidates.screen)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }//end part for column for announcements and candidates


            }//Main content column

        }//End of scaffold
    }
}


