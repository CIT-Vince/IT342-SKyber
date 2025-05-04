package com.example.skyber.headerbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.skyber.R
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.White

@Composable
fun HeaderBar(
    trailingContent: @Composable (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .width(130.dp)
                .padding(4.dp),
            contentScale = ContentScale.Fit
        )

        trailingContent?.invoke()

        }
    }


/*@Preview(showBackground = true)
@Composable
fun HeaderBarPreview() {
    HeaderBar(
        trailingContent = { // Add the Icon as trailing content here
            Icon(
                imageVector = Icons.Filled.NotificationsActive,
                contentDescription = "Preview Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
                    .offset(x = -4.dp, y = -8.dp) // Adjust this offset if needed
            )
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationModal(
    notifications: List<String>,
    onDismiss: () -> Unit
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
                    .padding(12.dp)
           ){
               Text("Notifications")
               Spacer(Modifier.height(8.dp))
               notifications.forEach {
                   Text(text = "• $it")
               }
           }
       }
   }
}

@Composable
fun NotificationHandler(
    modifier: Modifier = Modifier,
    fetchNotifications: () -> List<String> = { listOf("Default Notification") }
) {
    var showModal by remember { mutableStateOf(false) }
    val notifications = remember { fetchNotifications() }

    // Notification Icon Trigger
    Icon(
        imageVector = Icons.Filled.NotificationsActive,
        contentDescription = "Notifications",
        tint = Color.White,
        modifier = modifier
            .size(36.dp)
            .clickable { showModal = true }
            .offset(x = -6.dp, y = -8.dp)
    )

    // Show Modal
    if (showModal) {
        NotificationModal(
            notifications = notifications,
            onDismiss = { showModal = false }
        )
    }
}


