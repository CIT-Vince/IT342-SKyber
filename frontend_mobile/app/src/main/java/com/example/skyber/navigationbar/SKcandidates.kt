package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CandidateCard
import com.example.skyber.ModularFunctions.MemberCard
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.Screens
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Suppress("SpellCheckingInspection")
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SKcandidates(navController: NavHostController, userProfile: MutableState<User?>) {
    val allCandidates = remember { mutableStateListOf<CandidateProfile>() }
    val allMembers = remember { mutableStateListOf<SKProfile>() }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var searchVisible by remember { mutableStateOf(false) }
    val user = userProfile.value

    // Use tabs with proper state handling
    val tabs = listOf("Members", "Candidates")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val selectedTab = tabs[selectedTabIndex]

    // Use LazyListState to track scrolling
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Use derivedStateOf for filtered lists to optimize recomposition
    val filteredCandidates by remember(searchQuery, allCandidates) {
        derivedStateOf {
            allCandidates.filter {
                val fullName = "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}"
                fullName.contains(searchQuery, ignoreCase = true) ||
                        it.platform.orEmpty().contains(searchQuery, ignoreCase = true)
            }
        }
    }

    val filteredMembers by remember(searchQuery, allMembers) {
        derivedStateOf {
            allMembers.filter {
                val fullName = "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}"
                fullName.contains(searchQuery, ignoreCase = true) ||
                        it.position.orEmpty().contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // Show or hide FAB based on scroll position
    val showFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true
        loadError = null
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
            loadError = "Failed to load data. Please check your connection and try again."
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = White
                ),
                title = {
                    if (!searchVisible) {
                        Text(
                            "Sangguniang Kabataan",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    if (searchVisible) {
                        // Search field in top bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search ${selectedTab}...", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                                cursorColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.7f)
                            )
                        )
                        IconButton(onClick = {
                            searchQuery = ""
                            searchVisible = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Close Search", tint = Color.White)
                        }
                    } else {
                        // Search icon
                        IconButton(onClick = { searchVisible = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (user != null && user.role == "admin" && showFab) {
                PostOptionsFAB(navController = navController)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                    )
                )
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // Light particle effect (reduced count for performance)
            ParticleSystem(
                modifier = Modifier.fillMaxSize(),
                particleColor = Color.White.copy(alpha = 0.3f),
                particleCount = 30,
                backgroundColor = Color.Transparent
            )

            if (isLoading) {
                // Loading state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = SKyberYellow)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Loading ${selectedTab.lowercase()}...",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else if (loadError != null) {
                // Error state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = "Error",
                            tint = Color.Red.copy(alpha = 0.8f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            loadError ?: "An unknown error occurred",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    loadError = null
                                    // Try loading data again
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
                                        loadError = null
                                    } catch (e: Exception) {
                                        loadError = "Failed to load data. Please check your connection and try again."
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SKyberYellow
                            )
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tab selector
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        divider = {},
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = {
                                    selectedTabIndex = index
                                    // Scroll to top when changing tabs
                                    coroutineScope.launch {
                                        listState.scrollToItem(0)
                                    }
                                },
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 16.sp
                                    )
                                }
                            )
                        }
                    }

                    when (selectedTabIndex) {
                        0 -> {
                            // Members tab
                            if (filteredMembers.isEmpty()) {
                                EmptyState(
                                    title = "No Members Found",
                                    message = if (searchQuery.isEmpty())
                                        "There are no members to display"
                                    else
                                        "No members match your search"
                                )
                            } else {
                                MembersList(
                                    members = filteredMembers,
                                    listState = listState,
                                    navController = navController
                                )
                            }
                        }
                        1 -> {
                            // Candidates tab
                            if (filteredCandidates.isEmpty()) {
                                EmptyState(
                                    title = "No Candidates Found",
                                    message = if (searchQuery.isEmpty())
                                        "There are no candidates to display"
                                    else
                                        "No candidates match your search"
                                )
                            } else {
                                CandidatesList(
                                    candidates = filteredCandidates,
                                    listState = listState,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(title: String, message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MembersList(
    members: List<SKProfile>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    navController: NavHostController
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(members) { member ->
            // Use the existing MemberCard from CardFile.kt
            MemberCard(
                backgroundColor = SoftCardContainerBlue.copy(alpha = 0.9f),
                fontColor = SoftCardFontBlue,
                skProfile = member,
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "SKProfile", member
                    )
                    navController.navigate(Screens.DetailsSKmembers.screen)
                }
            )
        }

        // Add bottom padding for FAB
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun CandidatesList(
    candidates: List<CandidateProfile>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    navController: NavHostController
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(candidates) { candidate ->
            // Use the existing CandidateCard from CardFile.kt
            CandidateCard(
                backgroundColor = SoftCardContainerBlue.copy(alpha = 0.9f),
                fontColor = SoftCardFontBlue,
                candidateProfile = candidate,
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "CandidateProfile", candidate
                    )
                    navController.navigate(Screens.DetailsSKcandidates.screen)
                }
            )
        }

        // Add bottom padding for FAB
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun MemberCardImproved(
    member: SKProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftCardContainerBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image container
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E88E5))
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.firstName?.firstOrNull()?.toString() ?: "?",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${member.firstName ?: ""} ${member.lastName ?: ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCardFontBlue
                )

                Text(
                    text = member.position ?: "Position not specified",
                    fontSize = 14.sp,
                    color = SoftCardFontBlue.copy(alpha = 0.8f)
                )

                Text(
                    text = "Age: ${member.age ?: "N/A"}",
                    fontSize = 14.sp,
                    color = SoftCardFontBlue.copy(alpha = 0.7f)
                )
            }

            IconButton(
                onClick = { /* Share functionality */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = SKyberBlue
                )
            }
        }
    }
}

@Composable
fun CandidateCardImproved(
    candidate: CandidateProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftCardContainerBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image container
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF9C27B0)) // Different color for candidates
                    .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HowToVote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${candidate.firstName ?: ""} ${candidate.lastName ?: ""}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftCardFontBlue
                )

//                Text(
//                    text = candidate.runningFor ?: "Position not specified",
//                    fontSize = 14.sp,
//                    color = SoftCardFontBlue.copy(alpha = 0.8f)
//                )

                Text(
                    text = "Platform: ${candidate.platform?.take(50)}${if ((candidate.platform?.length ?: 0) > 50) "..." else ""}",
                    fontSize = 14.sp,
                    color = SoftCardFontBlue.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }

            IconButton(
                onClick = { /* Share functionality */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = SKyberBlue
                )
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
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        // Animated visibility for option buttons
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
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
        }

        // Main FAB with animated rotation
        val rotation by animateFloatAsState(
            targetValue = if (expanded) 45f else 0f,
            animationSpec = tween(200),
            label = "FAB rotation"
        )

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = SKyberBlue,
            contentColor = White
        ) {
//            Icon(
//                imageVector = Icons.Default.Add,
//                contentDescription = "Toggle options",
//                modifier = Modifier.graphicsLayer(rotationZ = rotation)
//            )
        }
    }
}