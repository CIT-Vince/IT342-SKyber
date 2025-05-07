package com.example.skyber.navigationbar.portalnavigator.ProjectTransparency

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.User
import com.example.skyber.navigationbar.portalnavigator.Announcement.showToast
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostProject(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value//passed logged in user
    var projectname by remember { mutableStateOf("") }
    var projectdescription by remember { mutableStateOf("") }
    var startdate by remember { mutableStateOf("") }
    var enddate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var projectmanager by remember { mutableStateOf("") }
    var projectmembers by remember { mutableStateOf("") }
    var stakeholders by remember { mutableStateOf("") }
    var sustainabilitygoals by remember { mutableStateOf("") }
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
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                            //Text Fields here
                            CustomOutlinedTextField(
                                value = projectname,
                                onValueChange = { projectname = it },
                                label = "Project Name"
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = projectmanager,
                                onValueChange = { projectmanager = it },
                                label = "Project Manager"
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = projectdescription,
                                onValueChange = { projectdescription = it },
                                label = "Project Description",
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            DatePickerField(
                                label = "Start Date",
                                selectedDate = startdate,
                                onDateSelected = { millis ->
                                    startdate = convertMillisToDate(millis)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            DatePickerField(
                                label = "End Date",
                                selectedDate = enddate,
                                onDateSelected = { millis ->
                                    enddate = convertMillisToDate(millis)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomOutlinedTextField(
                                value = projectmembers,
                                onValueChange = { projectmembers = it },
                                label = "Project Members",
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomOutlinedTextField(
                                value = stakeholders,
                                onValueChange = { stakeholders = it },
                                label = "Stakeholders"
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomOutlinedTextField(
                                value = budget,
                                onValueChange = { input ->
                                    // Allow only digits
                                    if (input.all { it.isDigit() }) {
                                        budget = input
                                    }
                                },
                                label = "Budget",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomOutlinedTextField(
                                value = sustainabilitygoals,
                                onValueChange = { sustainabilitygoals = it },
                                label = "Sustainability Goals",
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    // Create Project Post object
                                    if (projectname.isBlank() || startdate.isBlank() || enddate.isBlank()) {
                                        showToast(context, "Please fill out required fields")
                                    } else {
                                        val databaseRef =
                                            FirebaseHelper.databaseReference.child("ProjectTransparency")
                                                .push()
                                        val postId = databaseRef.key
                                        if (postId != null) {
                                            val newProject = Project(
                                                id = postId,
                                                projectName = projectname,
                                                description = projectdescription,
                                                status = "Ongoing",
                                                startDate = startdate,
                                                endDate = enddate,
                                                budget = budget,
                                                projectManager = projectmanager,
                                                sustainabilityGoals = sustainabilitygoals,
                                                teamMembers = projectmembers,
                                                stakeholders = stakeholders
                                            )
                                            // Upload the project post and show the toast
                                            uploadProject(databaseRef, newProject, context)
                                            navController.navigate(Screens.Projects.screen)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(200.dp)
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
                                        text = "Post Announcement",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                        }//End of lazy Column content
                    }//End of alzy column
                } //end of main content column
            }//end of top level column
        }//end of scaffold
    }
}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun uploadProject(databaseRef: DatabaseReference, project: Project, context: Context) {
    databaseRef.setValue(project).addOnSuccessListener {
        // Show success toast when announcement is uploaded successfully
        showToast(context, "Project Report uploaded successfully")
        }
        .addOnFailureListener {
            // Show failure toast when something goes wrong
            showToast(context, "Failed to Post Project Report")
        }
}

/*
@Preview(showBackground = true)//Use this preview for screens that need mock up user data
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
    val staticUserProfile = remember { mutableStateOf<User?>(user) }

    PostProject(userProfile = staticUserProfile)
}

 */