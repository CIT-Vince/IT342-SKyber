package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CandidateCard
import com.example.skyber.ModularFunctions.CustomSearchOTF
import com.example.skyber.ModularFunctions.MemberCard
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.Screens
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

@Suppress("SpellCheckingInspection")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SKcandidates(navController: NavHostController, userProfile: MutableState<User?>) {
    val allCandidates = remember { mutableStateListOf<CandidateProfile>() }
    val allMembers = remember { mutableStateListOf<SKProfile>() }
    var isLoading by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf("Members") }
    var searchQuery by remember { mutableStateOf("") }
    var searchVisible by remember { mutableStateOf(false) }
    var user = userProfile.value

    val filteredCandidates = allCandidates.filter {
        val fullName = "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}"
        fullName.contains(searchQuery, ignoreCase = true)
    }

    val filteredMembers = allMembers.filter {
        val fullName = "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}"
        fullName.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val candidatesDeferred = async {
                FirebaseHelper.databaseReference.child("Candidates").get().await()
                    .children.mapNotNull { it.getValue(CandidateProfile::class.java) }
            }

            val membersDeferred = async {
                FirebaseHelper.databaseReference.child("SKProfiles").get().await()
                    .children.mapNotNull { it.getValue(SKProfile::class.java) }
            }

            val fetchedCandidates = candidatesDeferred.await()
            val fetchedMembers = membersDeferred.await()

            allCandidates.clear()
            allCandidates.addAll(fetchedCandidates)

            allMembers.clear()
            allMembers.addAll(fetchedMembers)

        } catch (e: Exception) {
            Log.e("DataFetchError", "Failed to fetch SK data", e)
        } finally {
            isLoading = false
        }
    }

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "floating animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale animation"
    )

    val topLeftPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating top left"
    )

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if(user != null && user.role == "ADMIN"){
                    PostOptionsFAB(navController = navController)
            }else{
                    null
                }
            },
            floatingActionButtonPosition = FabPosition.End
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlueGradient)
            ) {
                // Particle system as the background
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White,
                    particleCount = 50,
                    backgroundColor = Color(0xFF0D47A1)
                )
                Text(
                    text = "💠",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (searchVisible) {
                        CustomSearchOTF(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            onSearchClick = {
                                searchQuery = ""
                                searchVisible = false
                            },
                            onClearClick = {
                                searchQuery = ""
                                searchVisible = false
                            },
                            label = "Search SK ${selectedTab}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else {
                        Text(
                            "Sangguniang Kabataan",
                            fontSize = 24.sp,
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .clickable { searchVisible = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Members",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Members") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Members" }
                        )
                        Text(
                            "Candidates",
                            fontSize = 24.sp,
                            color = if (selectedTab == "Candidates") White else Color.Gray,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { selectedTab = "Candidates" }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 12.dp,
                            top = 12.dp,
                            bottom = 80.dp // ⬅️ This gives space for the FAB
                        )
                    ) {
                        when (selectedTab) {
                            "Candidates" -> {
                                val list = filteredCandidates.reversed()
                                if (list.isEmpty()) {
                                    item { Text("") }
                                } else {
                                    items(list) { candidate ->
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
                            }

                            "Members" -> {
                                val list = filteredMembers.reversed()
                                if (list.isEmpty()) {
                                    item { Text("") }
                                } else {
                                    items(list) { member ->
                                        MemberCard(
                                            skProfile = member,
                                            backgroundColor = SoftCardContainerBlue,
                                            fontColor = SoftCardFontBlue,
                                            onClick = {
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "SKProfile", member
                                                )
                                                navController.navigate(Screens.DetailsSKmembers.screen)
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
    }
}

@Composable
fun PostOptionsFAB(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd // align to bottom-end
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
        ) {
            if (expanded) {
                ExtendedFloatingActionButton(
                    text = { Text("Post Member", color = SKyberBlue) },
                    icon = {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = SKyberBlue)
                    },
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostSKmembers.screen)
                    },
                    containerColor = White
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExtendedFloatingActionButton(
                    text = { Text("Post Candidate", color = SKyberBlue) },
                    icon = {
                        Icon(Icons.Filled.HowToVote, contentDescription = null, tint = SKyberBlue)
                    },
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostSKcandidates.screen)
                    },
                    containerColor = White
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = White
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.Close else Icons.Filled.Add,
                    contentDescription = "Toggle FAB",
                    tint = SKyberBlue
                )
            }
        }
    }
}