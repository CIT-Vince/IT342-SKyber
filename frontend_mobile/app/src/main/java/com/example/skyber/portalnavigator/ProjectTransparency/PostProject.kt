package com.example.skyber.portalnavigator.ProjectTransparency

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.Screens
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.getCurrentDateTime
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostProject(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user
    var projectname by remember { mutableStateOf("") }
    var projectdescription by remember { mutableStateOf("") }
    var startdate by remember { mutableStateOf("") }
    var enddate by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var projectmanager by remember { mutableStateOf("") }
    // For Project Members
    var newmember by remember { mutableStateOf("") }
    val projectmembers = remember { mutableStateListOf<String>() }
    // For Stakeholders
    var newstakeholder by remember { mutableStateOf("") }
    val stakeholders = remember { mutableStateListOf<String>() }
    var sustainabilitygoals by remember { mutableStateOf("") }
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
    }else {
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
                ){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(14.dp)
                    ) {
                        item{
                            //Text Fields here
                            TextField(
                                value = projectname,
                                onValueChange = { projectname = it },
                                label = { Text("Project Name") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp)),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = projectmanager,
                                onValueChange = { projectmanager = it },
                                label = { Text("Project Manager") },
                                modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = projectdescription,
                                onValueChange = { projectdescription = it },
                                label = { Text("Project Description") },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .padding(0.dp),
                                maxLines = 10
                                ,
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
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

                            Text("Project Members", fontWeight = FontWeight.Bold, color = Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = newmember,
                                    onValueChange = { newmember = it },
                                    label = { Text("Add a member") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    if (newmember.isNotBlank()) {
                                        projectmembers.add(newmember.trim())
                                        newmember = ""
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                    containerColor = SKyberBlue,
                                    contentColor = Color.White
                                )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AddCircle,
                                        contentDescription = "add member"
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            projectmembers.forEachIndexed { index, member ->
                                Text("• $member", fontSize = 14.sp, color = SKyberYellow)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Stakeholders", fontWeight = FontWeight.Bold , color = Black)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    value = newstakeholder,
                                    onValueChange = { newstakeholder = it },
                                    label = { Text("Add a stakeholder") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    if (newstakeholder.isNotBlank()) {
                                        stakeholders.add(newstakeholder.trim())
                                        newstakeholder = ""
                                    }
                                },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SKyberBlue,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Add")
                                }

                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            stakeholders.forEach { person ->
                                Text("• $person", fontSize = 14.sp, color = SKyberYellow)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = budget,
                                onValueChange = { budget = it },
                                label = { Text("Budget") },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            TextField(
                                value = sustainabilitygoals,
                                onValueChange = { sustainabilitygoals = it },
                                label = { Text("Sustainability Goals") },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                        }//End of lazy Column content
                    }//End of alzy column

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                        containerColor = SKyberBlue,
                        contentColor = Color.White
                    ),
                        onClick = {
                       // Create Project Post object
                            if (projectname.isBlank() || startdate.isBlank() || enddate.isBlank()) {
                                showToast(context, "Please fill out required fields")
                            }else{
                                val newProject = Project(
                                    projectName = projectname,
                                    projectDescription = projectdescription,
                                    status = "Ongoing",
                                    startDate = startdate,
                                    endDate = enddate,
                                    budget = budget,
                                    projectManager = projectmanager,
                                    sustainabilityGoals = sustainabilitygoals,
                                    projectMembers = projectmembers.toList(),
                                    stakeholders = stakeholders.toList()
                                )
                                // Upload the project post and show the toast
                                uploadProject(newProject, context)
                                navController.navigate(Screens.Projects.screen)
                            }
                    }) {
                        Text("Post Project")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                } //end of main content column
            }//end of top level column
        }//end of scaffold
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun uploadProject(project: Project, context: Context) {
    val databaseRef = FirebaseHelper.databaseReference.child("ProjectTransparency").push()
    databaseRef.setValue(project).addOnSuccessListener {
        // Show success toast when announcement is uploaded successfully
        showToast(context, "Project Report uploaded successfully")
    }
        .addOnFailureListener { error ->
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