package com.example.skyber.portalnavigator.ProjectTransparency

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.dataclass.Project
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsProject(navController: NavHostController){
    val project = navController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")

    if (project == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else {
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(White),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Projects",
                        color = SKyberBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = project.projectName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Project Manager: ${project.projectManager}",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Budget: ${project.budget}",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Start Date", fontWeight = FontWeight.SemiBold, color = SKyberBlue)
                            Text(project.startDate, color = SKyberBlue)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("End Date", fontWeight = FontWeight.SemiBold, color = SKyberBlue)
                            Text(project.endDate, color = SKyberBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Description",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = SKyberBlue
                    )
                    Text(
                        text = project.projectDescription,
                        fontSize = 14.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Stakeholders", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = SKyberBlue)
                    project.stakeholders.forEach { person ->
                        Text("• $person", fontSize = 14.sp,color = SKyberBlue)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Project Members", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = SKyberBlue)
                    project.projectMembers.forEach { member ->
                        Text("• $member", fontSize = 14.sp, color = SKyberBlue)
                    }

                }

            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    DetailsAnnouncement(navController = navController)
}*/