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
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.ModularFunctions.CandidateCard
import com.example.skyber.dataclass.User
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value

    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else{
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonPin,
                        tint = White,
                        contentDescription = "Person Pin",
                        modifier = Modifier
                            .size(50.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp)
                            .height(40.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center

                    ){
                        // Display the current user name and double check if null kay mo crash
                        Text(text = "Hello, ", fontSize = 30.sp, color = White)
                        Text(
                            text = user.firstname ?: "User",
                            fontSize = 30.sp,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center

                ){
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        tint = White,
                        contentDescription = "Location",
                        modifier = Modifier
                            .size(30.dp)

                    )
                    Text(text = user.address ?: "", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 2.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "News", fontSize = 22.sp, color = White, fontWeight = FontWeight.Bold)
                        Text(text = "See All", fontSize = 22.sp, color = White)
                    }

                    //AnnouncementCard(backgroundColor = White, fontColor = Black, title = "")

                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ){
                        Text(text = "Candidates", fontSize = 22.sp, color = White, fontWeight = FontWeight.Bold)
                    }
                    CandidateCard()
                }//end part for column for announcements and candidates

            }//Main content column
        }
    }//End of scaffold
}


