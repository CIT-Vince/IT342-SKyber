package com.example.skyber.portalnavigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Reports(navController: NavHostController) {
    Scaffold() {  innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
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
                    Text("Reports", fontSize = 24.sp, color = SKyberBlue, fontWeight = FontWeight.Bold)
                }
            )


        }

    }
}