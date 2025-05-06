package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.ModularFunctions.PortalTile
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberGreen
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Portal(navController: NavHostController,  userProfile: MutableState<User?>) {
    val user = userProfile.value
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {},
        containerColor = Color.Transparent,
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().background(SKyberDarkBlueGradient),
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

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {

                    PortalTile(
                        icon = Icons.Default.Campaign,
                        title = "Announcements",
                        backgroundColor = SKyberBlue
                    ) { navController.navigate(Screens.Announcement.screen) }

                    PortalTile(
                        icon = Icons.Default.Work,
                        title = "Jobs",
                        backgroundColor = SKyberYellow
                    ) { navController.navigate(Screens.Job.screen) }

                    PortalTile(
                        icon = Icons.Default.School,
                        title = "Scholarships",
                        backgroundColor = SKyberRed
                    ) { navController.navigate(Screens.Scholarship.screen) }

                    PortalTile(
                        icon = Icons.Default.Construction,
                        title = "Projects",
                        backgroundColor = SKyberGreen
                    ) { navController.navigate(Screens.Projects.screen) }
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (user != null) {
                    if(user.role != "ADMIN"){

                    }else{
                        PortalOptionsFAB(//Fab tastic buttons
                            navController = navController,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 20.dp, bottom = 30.dp)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun PortalOptionsFAB(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
        ) {
            if (expanded) {
                PortalFabItem(
                    label = "Post Announcement",
                    icon = Icons.Filled.Campaign,
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostAnnouncement.screen)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PortalFabItem(
                    label = "Post Job Listing",
                    icon = Icons.Filled.Work,
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostJob.screen)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PortalFabItem(
                    label = "Post Project",
                    icon = Icons.Filled.Build,
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostProject.screen)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PortalFabItem(
                    label = "Post Scholarship",
                    icon = Icons.Filled.School,
                    onClick = {
                        expanded = false
                        navController.navigate(Screens.PostScholarship.screen)
                    }
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

@Composable
fun PortalFabItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = { Text(label, color = SKyberBlue) },
        icon = { Icon(icon, contentDescription = null, tint = SKyberBlue) },
        onClick = onClick,
        containerColor = White
    )
}

