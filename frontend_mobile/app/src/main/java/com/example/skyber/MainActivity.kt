package com.example.skyber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyber.NavigationBar.Analytics
import com.example.skyber.NavigationBar.Announcements
import com.example.skyber.NavigationBar.Home
import com.example.skyber.NavigationBar.UserProfile
import com.example.skyber.NavigationBar.VolunteerHub
import com.example.skyber.ui.theme.NavBarColor

import com.example.skyber.ui.theme.SkyberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        Screens.Analytics.screen,
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
                        composable(Screens.Home.screen) { Home() }
                        composable(Screens.VolunteerHub.screen) { VolunteerHub() }
                        composable(Screens.Analytics.screen) { Analytics() }
                        composable(Screens.Announcement.screen) { Announcements() }
                        composable(Screens.UserProfile.screen) { UserProfile() }

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
        NavItem(Icons.Filled.DateRange, Screens.VolunteerHub.screen),
        NavItem(Icons.Filled.KeyboardArrowUp, Screens.Analytics.screen),
        NavItem(Icons.Filled.Search, Screens.Announcement.screen),
        NavItem(Icons.Filled.Person, Screens.UserProfile.screen),
    )

    val selected = remember { mutableStateOf(Icons.Default.Home) }

    BottomAppBar(
        containerColor = NavBarColor,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 57.dp, topEnd = 57.dp))
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
                    tint = if (selected.value == item.icon) Color.White else Color.DarkGray
                )
            }
        }
    }
}
