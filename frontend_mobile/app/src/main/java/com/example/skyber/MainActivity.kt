package com.example.skyber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyber.navigationbar.Reports
import com.example.skyber.navigationbar.Announcements
import com.example.skyber.navigationbar.Home
import com.example.skyber.navigationbar.UserProfile
import com.example.skyber.navigationbar.VolunteerHub
import com.example.skyber.ui.theme.NavBarColor
import com.example.skyber.ui.theme.SkyberBlue

import com.example.skyber.ui.theme.SkyberTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            SkyberTheme {
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
                    // Navigation logic same as react routers

                    val currentBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = currentBackStackEntry?.destination?.route

                    // logic for showing the bottom nav when not in login or the signup screens
                    val bottomNavRoutes = listOf(
                        Screens.Home.screen,
                        Screens.VolunteerHub.screen,
                        Screens.Reports.screen,
                        Screens.Announcement.screen,
                        Screens.UserProfile.screen
                    )

                    showBottomNav.value = currentRoute in bottomNavRoutes

                    NavHost(
                        navController = navController,
                        startDestination = Screens.Login.screen,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Main screens
                        composable(Screens.Home.screen) { Home(navController) }
                        composable(Screens.VolunteerHub.screen) { VolunteerHub() }
                        composable(Screens.Reports.screen) { Reports() }
                        composable(Screens.Announcement.screen) { Announcements() }
                        composable(Screens.UserProfile.screen) { UserProfile(navController) }

                        // Auth screens
                        composable(Screens.Login.screen) { LoginScreen(navController) }
                        composable(Screens.SignUp.screen) { SignupScreen(navController) }
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
        NavItem(Icons.Filled.Hub, Screens.VolunteerHub.screen),
        NavItem(Icons.AutoMirrored.Filled.TrendingUp, Screens.Reports.screen),
        NavItem(Icons.AutoMirrored.Filled.Announcement, Screens.Announcement.screen),
        NavItem(Icons.Filled.Person, Screens.UserProfile.screen),
    )

    val selected = remember { mutableStateOf(Icons.Default.Home) }

    BottomAppBar(
        containerColor = NavBarColor,
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
                    tint = if (selected.value == item.icon) SkyberBlue else Color.DarkGray
                )
            }
        }
    }
}
