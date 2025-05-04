package com.example.skyber.navigationbar.userprofilescreens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.Screens
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxRed
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.navigationbar.volunteerhubscreens.applyToVolunteerEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsVolunteerList(navController: NavHostController) {
    val volunteerPost = navController.previousBackStackEntry?.savedStateHandle?.get<VolunteerPost>("volunteerPost")
    //val context = LocalContext.current

    if (volunteerPost == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
        val isOngoing = volunteerPost.status.lowercase() == "ongoing"
        val backgroundColor = if (isOngoing) BoxGreen else BoxRed
        val textColor = if (isOngoing) BoxTextGreen else SKyberRed

        Scaffold { innerPadding ->
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
                        .padding(top = 32.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        .background(White)
                        .padding(horizontal = 28.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(6.dp)
                    ){ item{
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp),
                            verticalAlignment = Alignment.Bottom,
                        ){
                            Text(
                                text = volunteerPost.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = SKyberBlue
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(backgroundColor)
                                    .padding(horizontal = 8.dp)
                                    .width(80.dp)

                            ){
                                Text(
                                    text = volunteerPost.status,
                                    fontSize = 18.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Contact: ${volunteerPost.contactPerson}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Contact Email: ${volunteerPost.contactEmail}",
                            fontSize = 14.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Event Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = volunteerPost.description,
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Location: ${volunteerPost.location}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Text(
                            text = "Event Date: ${volunteerPost.eventDate}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Requirements",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = volunteerPost.requirements,
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    }
                    /*
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Button(
                            onClick = {
                                applyToVolunteerEvent(volunteerPost.eventId, context)
                                navController.navigate(Screens.VolunteerHub.screen)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue),
                            modifier = Modifier
                                .padding(16.dp)
                                .width(150.dp)
                        ) {
                            Text(text = "Apply")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "add event"
                            )
                        }
                    }
                    */

                }
            }
        }
    }
}