package com.example.skyber.navigationbar.portalnavigator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skyber.Screens
import com.example.skyber.navigationbar.Portal
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.White

@Composable
fun PortalNav(trailingContent: @Composable (() -> Unit)? = null){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(12.dp)
            .clip(RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ){

        trailingContent?.invoke()

    }

}
/*
@Preview(showBackground = true)
@Composable
fun PortalNavPreview() {
    PortalNav()
}
*/

/*@Preview(showBackground = true)
@Composable
fun PortalNavModalPreview() {
    val mockRoutes = listOf("Announcements", "Projects", "Jobs", "Scholarships")

    val onDismiss: () -> Unit = {  }

    PortalNavModal(
        navRoutes = mockRoutes,
        onDismiss = onDismiss,
        onRouteClick = { route ->
            println("Navigating to: $route")
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortalNavModal(
    navRoutes: List<String>,
    onDismiss: () -> Unit,
    onRouteClick: (String) -> Unit
) {

    Dialog(onDismissRequest = {onDismiss() }){
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ){
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Portal Navigation", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = SKyberBlue)
                }
                Spacer(Modifier.height(8.dp))
                navRoutes.forEach { route ->
                    Text(
                        text = "$route",
                        color = SKyberBlue,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .height(34.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                onRouteClick(route)
                            }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PortalNavHandler(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    fetchRoutes: () -> List<String> = {
        listOf(
            Screens.Announcement.screen,
            Screens.Job.screen,
            Screens.Scholarship.screen,
            Screens.Projects.screen,
        )
    }
) {
    var showModal by remember { mutableStateOf(false) }
    val routes = remember { fetchRoutes() }

    // Get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentLabel = routes.find { route ->
        currentRoute?.contains(route, ignoreCase = true) == true
    } ?: "News And Updates"

    // Modal Trigger
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .clickable { showModal = true }
            .padding(16.dp)
    ) {
        Text(
            text = currentLabel,
            color = Color.Black,
            fontSize = 30.sp
        )
    }

    // Show Modal
    if (showModal) {
        PortalNavModal(
            navRoutes = routes,
            onDismiss = { showModal = false },
            onRouteClick = { route ->
                navController.navigate(route)
                showModal = false
            }
        )
    }
}
