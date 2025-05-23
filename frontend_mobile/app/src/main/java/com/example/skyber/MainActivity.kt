package com.example.skyber

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ViewTimeline
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.navigationbar.Home
import com.example.skyber.navigationbar.Portal
import com.example.skyber.navigationbar.SKcandidates
import com.example.skyber.navigationbar.UserProfile
import com.example.skyber.navigationbar.VolunteerHub
import com.example.skyber.navigationbar.portalnavigator.Announcement.Announcements
import com.example.skyber.navigationbar.portalnavigator.Announcement.DetailsAnnouncement
import com.example.skyber.navigationbar.portalnavigator.Announcement.PostAnnouncement
import com.example.skyber.navigationbar.portalnavigator.Job.DetailsJob
import com.example.skyber.navigationbar.portalnavigator.Job.Job
import com.example.skyber.navigationbar.portalnavigator.Job.PostJob
import com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.DetailsProject
import com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.PostProject
import com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.Projects
import com.example.skyber.navigationbar.portalnavigator.Scholarships.DetailsScholarship
import com.example.skyber.navigationbar.portalnavigator.Scholarships.PostScholarship
import com.example.skyber.navigationbar.portalnavigator.Scholarships.Scholarships
import com.example.skyber.navigationbar.skprofilescreens.DetailsSKcandidates
import com.example.skyber.navigationbar.skprofilescreens.DetailsSKmembers
import com.example.skyber.navigationbar.skprofilescreens.PostSKcandidates
import com.example.skyber.navigationbar.skprofilescreens.PostSKmembers
import com.example.skyber.navigationbar.userprofilescreens.DetailsVolunteerList
import com.example.skyber.navigationbar.userprofilescreens.EditProfile
import com.example.skyber.navigationbar.userprofilescreens.VolunteerList
import com.example.skyber.navigationbar.volunteerhubscreens.DetailsVolunteerHub
import com.example.skyber.navigationbar.volunteerhubscreens.PostVolunteerHub
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SkyberTheme
import com.example.skyber.ui.theme.White
import com.example.skyber.userauth.LoginScreen
import com.example.skyber.userauth.SignupScreen
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    100 // Request code
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    100 // Request code
                )
            }
        }

        FirebaseApp.initializeApp(this)

        fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults)
            if (requestCode == 100) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    // Permission denied
                }
            }
        }

        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(true)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()


        enableEdgeToEdge()
        setContent {
            val uid = FirebaseHelper.auth.currentUser?.uid //Get current user globally
            val userProfile = remember { mutableStateOf<User?>(null) }//hold the user profile data
            var isLoading by remember { mutableStateOf(true) }

            //global refresher function for changes to user data
            fun refreshUserProfile() {
                val uid = FirebaseHelper.auth.currentUser?.uid
                if (uid != null) {
                    FirebaseHelper.databaseReference.child("users").child(uid)
                        .get()
                        .addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(User::class.java)
                            userProfile.value = user
                            isLoading = false
                        }
                        .addOnFailureListener {
                            isLoading = false
                        }
                }
            }

            LaunchedEffect(uid) {
                if (uid != null) {
                    FirebaseHelper.databaseReference.child("users").child(uid)
                        .get().addOnSuccessListener { snapshot ->
                            val user = snapshot.getValue(User::class.java)
                            userProfile.value = user
                            isLoading = false
                        }
                        .addOnFailureListener { e ->
                            isLoading = false
                        }
                } else {
                    isLoading = false
                }
            }

            // Determine the start destination based on user profile availability
            val startDestination = if (userProfile.value != null) {
                Screens.Home.screen  // or any other screen if the user is logged in
                //Screens.CandidateTestScreen.screen
            } else {
                Screens.Login.screen
            }

            SkyberTheme {
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
                    val navController = rememberNavController()
                    val showBottomNav = remember { mutableStateOf(true) }
                    Scaffold(
                        bottomBar = {
                            // Manages visibility of the bottom navigation bar
                            if (showBottomNav.value) {
                                BottomNavBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        // Main navigation and content handling
                        val currentBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = currentBackStackEntry?.destination?.route

                        // Define routes where the bottom nav should be visible
                        val bottomNavRoutes = listOf(
                            Screens.Home.screen,
                            Screens.VolunteerHub.screen,
                            Screens.PostVolunteerHub.screen,
                            Screens.DetailsVolunteerHub.screen,
                            Screens.Portal.screen,
                            Screens.Announcement.screen,
                            Screens.PostAnnouncement.screen,
                            Screens.DetailsAnnouncement.screen,
                            Screens.UserProfile.screen,
                            Screens.Reports.screen,
                            Screens.EditProfile.screen,
                            Screens.SKcandidates.screen,
                            Screens.PostSKcandidates.screen,
                            Screens.DetailsSKcandidates.screen,
                            Screens.Projects.screen,
                            Screens.PostProject.screen,
                            Screens.DetailsProject.screen,
                            Screens.VolunteerList.screen,
                            Screens.DetailsVolunteerList.screen,
                            Screens.Job.screen,
                            Screens.PostJob.screen,
                            Screens.DetailsJob.screen,
                            Screens.Scholarship.screen,
                            Screens.PostScholarship.screen,
                            Screens.DetailsScholarship.screen,
                            Screens.CandidateTestScreen.screen,
                            Screens.DetailsSKmembers.screen,
                            Screens.PostSKmembers.screen
                        )

                        // Set visibility of the bottom nav
                        showBottomNav.value = currentRoute in bottomNavRoutes &&
                                currentRoute != Screens.SignUp.screen &&
                                currentRoute != Screens.Login.screen

                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // Auth screens
                            composable(Screens.Login.screen) { LoginScreen(navController, ::refreshUserProfile) }
                            composable(Screens.SignUp.screen) { SignupScreen(navController) }

                            // Main screens
                            composable(Screens.Home.screen) {Home(navController,userProfile = userProfile, ::refreshUserProfile)}
                            composable(Screens.VolunteerHub.screen) { VolunteerHub(navController , userProfile = userProfile) }
                            composable(Screens.Portal.screen) {Portal(navController,userProfile = userProfile)}
                            composable(Screens.UserProfile.screen) {UserProfile(navController,userProfile = userProfile,::refreshUserProfile )}
                            composable(Screens.SKcandidates.screen) { SKcandidates(navController,  userProfile = userProfile) }

                            //Nested screens for SKcandidates
                            composable(Screens.PostSKcandidates.screen) { PostSKcandidates(navController,userProfile = userProfile) }
                            composable(Screens.DetailsSKcandidates.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<CandidateProfile>("CandidateProfile")
                                    }
                                }
                                DetailsSKcandidates(navController, userProfile)
                            }

                            //Nested screens for SK members
                            composable(Screens.PostSKmembers.screen) { PostSKmembers(navController, userProfile = userProfile) }
                            composable(Screens.DetailsSKmembers.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<SKProfile>("SKProfile")
                                    }
                                }
                                DetailsSKmembers(navController, userProfile)
                            }

                            //Nested Screens in Portal
                            composable(Screens.Announcement.screen) { Announcements(navController) }
                            composable(Screens.Job.screen) { Job(navController) }
                            composable(Screens.Projects.screen) { Projects(navController) }
                            composable(Screens.Scholarship.screen) { Scholarships(navController) }

                            //Nested Screens in Scholarship
                            composable(Screens.PostScholarship.screen) { PostScholarship(navController,userProfile = userProfile) }
                            composable(Screens.DetailsScholarship.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<Scholarship>("scholarship")
                                    }
                                }
                                DetailsScholarship(navController, userProfile)
                            }

                            //Nested Screens in Job
                            composable(Screens.PostJob.screen) { PostJob(navController,userProfile = userProfile) }
                            composable(Screens.DetailsJob.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<JobListing>("joblisting")
                                    }
                                }
                                DetailsJob(navController, userProfile)
                            }

                            //Nested Screens in Project
                            composable(Screens.PostProject.screen) { PostProject(navController,userProfile = userProfile) }
                            composable(Screens.DetailsProject.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<Project>("project")
                                    }
                                }
                                DetailsProject(navController, userProfile)
                            }

                            //Nested Screens in Announcement
                            composable(Screens.PostAnnouncement.screen) { PostAnnouncement(navController,userProfile = userProfile) }
                            composable(Screens.DetailsAnnouncement.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<Announcement>("announcement")
                                    }
                                }
                                DetailsAnnouncement(navController, userProfile = userProfile as MutableState<User>)
                            }

                            //Nested Screens in VolunteerHub
                            composable(Screens.PostVolunteerHub.screen) { PostVolunteerHub(navController,userProfile = userProfile) }
                            composable(Screens.DetailsVolunteerHub.screen) {
                                val navBackStackEntry = navController.currentBackStackEntry
                                DisposableEffect(navBackStackEntry) {
                                    onDispose {
                                        navBackStackEntry?.savedStateHandle?.remove<VolunteerPost>("volunteerPost")
                                    }
                                }
                                DetailsVolunteerHub(navController, userProfile)
                            }

                            //Nested Screens in User Profile
                            composable(Screens.EditProfile.screen) { EditProfile(navController, userProfile = userProfile, ::refreshUserProfile) }
                            composable(Screens.VolunteerList.screen) { VolunteerList(navController,userProfile = userProfile) }
                            composable(Screens.DetailsVolunteerList.screen) { DetailsVolunteerList(navController) }

                            //Test Screens

                        }
                    }
                }
            }
        }
    }

    data class NavItem(
        val icon: ImageVector,
        val destination: String
    )

    @Composable
    fun BottomNavBar(navController: NavController) {
        val navItems = listOf(
            NavItem(Icons.Filled.Home, Screens.Home.screen),
            NavItem(Icons.Filled.VolunteerActivism, Screens.VolunteerHub.screen),
            NavItem(Icons.Filled.ViewTimeline, Screens.Portal.screen),
            NavItem(Icons.Filled.Gavel, Screens.SKcandidates.screen),
            NavItem(Icons.Filled.Person, Screens.UserProfile.screen)
        )

        val selected = remember { mutableStateOf(Icons.Default.Home) }

        BottomAppBar(
            containerColor = White,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            navItems.forEach { item ->
                IconButton(
                    onClick = {
                        selected.value = item.icon
                        navController.navigate(item.destination) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == item.icon) SKyberBlue else Color.DarkGray
                    )
                }
            }
        }
    }
}

/*
composable(Screens.CandidateTestScreen.screen) {
    val candidateViewModel: CandidateViewModel = viewModel()
    CandidateTestScreen(viewModel = candidateViewModel,navController)
}
 */

