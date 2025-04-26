package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.ModularFunctions.PortalTile
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberGreen
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Portal(navController: NavHostController,  userProfile: MutableState<User?>) {
    val user = userProfile.value
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {  innerPadding ->
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
                    .padding(12.dp)
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
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PortalPreview() {
    val navController = rememberNavController()
    Portal(navController = navController)
}
*/