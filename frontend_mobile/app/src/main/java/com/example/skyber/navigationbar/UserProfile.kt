package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.Cards.ListCard
import com.example.skyber.FirebaseHelper
import com.example.skyber.R
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.headerbar.*
import com.example.skyber.ui.theme.*




@Composable@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun UserProfile(navController: NavHostController, userProfile : MutableState<User?>)  {
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

                Text(
                    text = "User Profile",
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonPin,
                        tint = White,
                        contentDescription = "User Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Display the current user name and double check if null kay mo crash
                        if (user != null) {
                            Text(
                                text = user.lastname ?: "User",
                                fontSize = 30.sp,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = user.firstname ?: "User",
                                fontSize = 30.sp,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            CircularProgressIndicator(color = SKyberYellow)
                        }
                    }
                }
                //Main Content
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            White,
                            shape = RoundedCornerShape(
                                topStart = 60.dp,
                                topEnd = 60.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(20.dp)
                    ) {
                        ListCard(
                            title = "Edit Profile",
                            icon = Icons.Filled.ManageAccounts,
                            onCardClick = {
                                navController.navigate(Screens.EditProfile.screen)
                            })
                        ListCard(
                            title = "Volunteers",
                            icon = Icons.Filled.VolunteerActivism,
                            onCardClick = {})
                        ListCard(
                            title = "Logout",
                            icon = Icons.AutoMirrored.Filled.Logout,
                            onCardClick = {
                                FirebaseHelper.auth.signOut()
                                navController.navigate(Screens.Login.screen)
                            })

                    }
                }

            }//End of main Column Layout
        }
    }//End of Scaffold
}

/*
@Preview(showBackground = true)
@Composable
fun UserProfilePreview(){
    val navController = rememberNavController()
    UserProfile(navController = navController)
}
*/


/*
@Preview(showBackground = true)
@Composable
fun ListCardPreview(){
    ListCard(title ="Lorem ipsum dolor", icon = Icons.Filled.ManageAccounts, onCardClick = {})
}
*/