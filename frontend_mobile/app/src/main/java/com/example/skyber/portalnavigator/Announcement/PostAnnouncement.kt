package com.example.skyber.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.getCurrentDateTime
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.ParticleSystem
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostAnnouncement(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value
    var title by remember { mutableStateOf("")}
    var content by remember {mutableStateOf("")}
    var category by remember {mutableStateOf("")}
    var barangay by remember {mutableStateOf("")}
    val context = LocalContext.current
    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "floating animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale animation"
    )

    val topLeftPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating top left"
    )

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
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlueGradient)
            ) {
                // Particle system as the background
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White,
                    particleCount = 80,
                    backgroundColor = Color(0xFF0D47A1)
                )

                Text(
                    text = "💠",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )

                /*Text(
                    text = "✨",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 20.dp)
                        .graphicsLayer(alpha = 0.5f)
                )*/

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = barangay,
                                onValueChange = { barangay = it },
                                label = { Text("Barangay") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Category") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = content,
                                onValueChange = { content = it },
                                label = { Text("Content") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color(0xFF0066FF),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        // Create Announcement object
                        val databaseRef =
                            FirebaseHelper.databaseReference.child("Announcements").push()
                        val id = databaseRef.key
                        if (id != null) {
                            val newAnnouncement = Announcement(
                                id = id,
                                title = title,
                                content = content,
                                postedAt = getCurrentDateTime(), // Assuming this function returns the correct formatted date
                                //author = "${user.firstname} ${user.lastname}",
                                barangay = barangay,
                                category = category,// Full name of the user
                            )
                            uploadAnnouncement(databaseRef, newAnnouncement, context)
                            navController.navigate(Screens.Announcement.screen)
                        } else {
                            showToast(context, "Failed to create announcement")
                        }
                        // Upload the announcement and show the toast

                    },
                        modifier = Modifier
                            .width(200.dp)
                            .height(60.dp),
                            shape = RoundedCornerShape(28.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(gradientBrush),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "Post Announcement",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

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