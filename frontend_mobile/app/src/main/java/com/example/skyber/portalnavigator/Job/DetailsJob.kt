package com.example.skyber.portalnavigator.Job

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.dataclass.JobListing
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsJob(navController: NavHostController){
    val joblisting = navController.previousBackStackEntry?.savedStateHandle?.get<JobListing>("joblisting")

    if (joblisting == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else {
        val jobCategory = joblisting.category.lowercase() == "full-time"
        val statusColor = if (jobCategory) BoxGreen else SoftCardContainerBlue
        val textColor = if (jobCategory)  BoxTextGreen else SoftCardFontBlue
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
                        text = "Job Listing",
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

                    Row(
                        modifier = Modifier.height(30.dp),
                        verticalAlignment = Alignment.Bottom
                    ){
                        Text(
                            text = joblisting.jobtitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(22.dp))
                                .background(statusColor)
                                .padding(horizontal = 8.dp)
                                .wrapContentWidth()

                        ){
                            Text(
                                text = joblisting.category,
                                fontSize = 18.sp,
                                color = textColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Company Name: ${joblisting.companyname}",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Location: ${joblisting.location}",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Text(
                        text = "Location: ${joblisting.address}",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )


                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Job Description",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = SKyberBlue
                    )
                    Text(
                        text = joblisting.description,
                        fontSize = 14.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Contact Information",
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = joblisting.contactperson,
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = joblisting.contact,
                        fontSize = 16.sp,
                        color = SKyberBlue
                    )

                }

            }
        }
    }
}