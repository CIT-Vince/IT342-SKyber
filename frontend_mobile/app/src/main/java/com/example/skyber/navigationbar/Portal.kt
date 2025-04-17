package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.PortalNav
import com.example.skyber.portalnavigator.PortalNavHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.userauth.SignupScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Portal(navController: NavHostController) {
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

            PortalNav(
                trailingContent = {
                    PortalNavHandler(navController = navController)
                    Text("Portal", fontSize = 24.sp, color = SKyberBlue, fontWeight = FontWeight.Bold)
                }
            )

            Column(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(0.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                    .background(White)

            ){

            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun PortalPreview() {
    val navController = rememberNavController()
    Portal(navController = navController)
}
