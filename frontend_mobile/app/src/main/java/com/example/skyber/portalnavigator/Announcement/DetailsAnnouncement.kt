package com.example.skyber.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.ParticleSystem
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsAnnouncement(navController: NavHostController){
    val announcement = navController.previousBackStackEntry?.savedStateHandle?.get<Announcement>("announcement")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("")}
    var newContent by rememberSaveable {mutableStateOf("")}
    var newCategory by rememberSaveable {mutableStateOf("")}
    var newBarangay by rememberSaveable {mutableStateOf("")}
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

    LaunchedEffect(isEditMode) {
        if (isEditMode && announcement != null) {
            newTitle = announcement.title
            newContent = announcement.content
            newCategory = announcement.category
            newBarangay = announcement.barangay
        }
    }


    if (announcement == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else{
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SKyberDarkBlueGradient)
            ) {
                // Particle background
                ParticleSystem(
                    modifier = Modifier.fillMaxSize(),
                    particleColor = Color.White,
                    particleCount = 80,
                    backgroundColor = Color(0xFF0D47A1)
                )

                // Decorative elements
                Text(
                    text = "💠",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.3f) // Adjust opacity
                )

                /*Text(
                    text = "✨",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 30.dp, bottom = 20.dp)
                        .graphicsLayer(alpha = 0.3f) // Adjust opacity
                )*/

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
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
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 40.dp, bottom = 40.dp)
                            //.align(Alignment.Center)
                            .background(Color.White, RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isEditMode) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item {
                                    OutlinedTextField(
                                        value = newTitle,
                                        onValueChange = { newTitle = it },
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
                                        value = newBarangay,
                                        onValueChange = { newBarangay = it },
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
                                        value = newCategory,
                                        onValueChange = { newCategory = it },
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
                                        value = newContent,
                                        onValueChange = { newContent = it },
                                        label = { Text("Content") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(//Update button
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val announcementId = announcement.id

                                            // Build updated announcement
                                            val updatedAnnouncement = Announcement(
                                                id = announcementId,
                                                title = newTitle.ifEmpty { announcement.title },
                                                content = newContent.ifEmpty { announcement.content },
                                                postedAt = announcement.postedAt, // keep original date
                                                barangay = newBarangay.ifEmpty { announcement.barangay },
                                                category = newCategory.ifEmpty { announcement.category }
                                            )
                                            database.child("Announcements").child(announcementId)
                                                .setValue(updatedAnnouncement)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Announcement updated successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    isEditMode = false
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Update unsuccessful",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(gradientBrush),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Update",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Delete",
                                        fontSize = 14.sp,
                                        color = SKyberRed,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable {
                                            val database = FirebaseHelper.databaseReference
                                            val announcementId = announcement.id
                                            database.child("Announcements").child(announcementId)
                                                .removeValue()
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Deleted Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    isEditMode = false
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Deletion unsuccessful",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                        }
                                    )

                                }//end of items
                            }//end of lazy column

                        } else {//Details mode
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        // Content at the top
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.TopStart),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = announcement.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 28.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = "Posted on ${announcement.postedAt}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Barangay ${announcement.barangay}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "${announcement.category}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Text(
                                                text = "Announcement",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Box(
                                                modifier = Modifier.heightIn(min = 200.dp) //Caveman custom height setter
                                            ) {
                                                Text(
                                                    text = announcement.content,
                                                    fontSize = 16.sp,
                                                    lineHeight = 22.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            // Buttons at the bottom
                                            Button(//Switch to edit mode screen
                                                onClick = {
                                                    isEditMode = true
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(60.dp),
                                                shape = RoundedCornerShape(28.dp),
                                                contentPadding = PaddingValues(0.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .background(gradientBrush),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "Edit",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = "Delete",
                                                fontSize = 14.sp,
                                                color = SKyberRed,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.clickable {
                                                    val database = FirebaseHelper.databaseReference
                                                    val announcementId = announcement.id
                                                    database.child("Announcements")
                                                        .child(announcementId)
                                                        .removeValue()
                                                        .addOnSuccessListener {
                                                            Toast.makeText(
                                                                context,
                                                                "Deleted Successfully",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            isEditMode = false
                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(
                                                                context,
                                                                "Deletion unsuccessful",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    DetailsAnnouncement(navController = navController)
}*/
