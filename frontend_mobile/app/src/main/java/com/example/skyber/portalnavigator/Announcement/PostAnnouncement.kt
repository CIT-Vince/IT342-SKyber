package com.example.skyber.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.getCurrentDateTime
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.google.firebase.database.DatabaseReference

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostAnnouncement(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value
    var title by remember { mutableStateOf("")}
    var content by remember {mutableStateOf("")}
    var category by remember {mutableStateOf("")}
    var barangay by remember {mutableStateOf("")}
    val context = LocalContext.current


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
        Scaffold(){ innerPadding->
            Column(
                modifier = Modifier
                    .background(SKyberDarkBlue)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
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
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(14.dp)
                    ) {
                        item {
                            TextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = barangay,
                                onValueChange = { barangay = it },
                                label = { Text("Barangay") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = content,
                                onValueChange = { content = it },
                                label = { Text("Content") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp), // Adjust height for multiline
                                maxLines = 10
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        // Create Announcement object
                        val databaseRef = FirebaseHelper.databaseReference.child("Announcements").push()
                        val id = databaseRef.key
                        if(id != null){
                            val newAnnouncement = Announcement(
                                id = id,
                                title = title,
                                content = content,
                                postedAt = getCurrentDateTime(), // Assuming this function returns the correct formatted date
                                //author = "${user.firstname} ${user.lastname}",
                                barangay = barangay,
                                category = category,// Full name of the user
                            )
                            uploadAnnouncement(databaseRef,newAnnouncement, context)
                            navController.navigate(Screens.Announcement.screen)
                        }else{
                            showToast(context, "Failed to create announcement")
                        }
                        // Upload the announcement and show the toast

                    }) {
                        Text("Post Announcement")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

    fun uploadAnnouncement(databaseRef: DatabaseReference, announcement: Announcement, context: Context) {
        databaseRef.setValue(announcement).addOnSuccessListener {
                // Show success toast when announcement is uploaded successfully
                showToast(context, "Announcement uploaded successfully")
            }
            .addOnFailureListener {
                // Show failure toast when something goes wrong
                showToast(context, "Failed to Post Announcement")
            }
    }
/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    val user = User(
        id = "static123",
        firstname = "Preview",
        lastname = "User",
        email = "preview@example.com",
        password = "password123",
        dob = "01/01/1990",
        gender = "Non-binary",
        role = "viewer"
    )
    val staticUserProfile = remember { mutableStateOf(user) }
    val navController = rememberNavController()
    PostAnnouncement(navController = navController, userProfile = staticUserProfile)
}
*/