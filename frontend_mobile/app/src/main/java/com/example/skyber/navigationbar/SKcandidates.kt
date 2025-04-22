package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CandidateCard
import com.example.skyber.Screens
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@Suppress("SpellCheckingInspection")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SKcandidates(navController: NavHostController) {
    val allCandidates = remember { mutableListOf<CandidateProfile>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        FirebaseHelper.databaseReference.child("CandidateProfile")
            .get().addOnSuccessListener { snapshot ->
                allCandidates.clear()
                snapshot.children.forEach { child ->
                    val candidate = child.getValue(CandidateProfile::class.java)
                    Log.d("Candidate Fetch", candidate.toString())
                    if (candidate != null) {
                        allCandidates.add(candidate)
                        isLoading = false
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Candidates Fetch", "Failed to load Candidate Profiles", it)
                isLoading = false
            }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlue),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
    } else {
        Scaffold() { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlue),
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "SK CANDIDATES",
                            fontSize = 24.sp,
                            color = SKyberBlue,
                            fontWeight = FontWeight.Bold,
                            //modifier = Modifier.clickable { selectedTab = "Ongoing" }
                        )
                    }
                    if (allCandidates.isEmpty()) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                "No candidates available",
                                color = SKyberRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(12.dp)
                        ) {
                            items(allCandidates.reversed()) { candidate ->
                                CandidateCard(candidateProfile = candidate,
                                    backgroundColor = SoftCardContainerBlue,
                                    fontColor = SoftCardFontBlue,
                                    onClick = {
                                        navController.currentBackStackEntry?.savedStateHandle?.set(
                                            "CandidateProfile",
                                            candidate
                                        )
                                        navController.navigate(Screens.DetailsSKcandidates.screen)
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }


                    Button(
                        onClick = {
                            navController.navigate(Screens.PostSKcandidates.screen)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue),
                        modifier = Modifier
                            .padding(16.dp)
                            .width(250.dp)
                    ) {
                        Text(text = "Post Candidate Profile")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "add Candidate Profile"
                        )
                    }

                }
            }

        }
    }
}
