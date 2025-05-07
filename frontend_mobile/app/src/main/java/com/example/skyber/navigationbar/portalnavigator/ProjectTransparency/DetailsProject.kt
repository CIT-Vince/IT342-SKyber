package com.example.skyber.navigationbar.portalnavigator.ProjectTransparency

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxRed
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsProject(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val project = navController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newProjectName by rememberSaveable { mutableStateOf("") }
    var newProjectDescription by rememberSaveable { mutableStateOf("") }
    var newStatus by rememberSaveable { mutableStateOf("") }
    var newStartDate by rememberSaveable { mutableStateOf("") }
    var newEndDate by rememberSaveable { mutableStateOf("") }
    var newBudget by rememberSaveable { mutableStateOf("") }
    var newProjectManager by rememberSaveable { mutableStateOf("") }
    var newProjectMembers by rememberSaveable { mutableStateOf("") }
    var newStakeholders by rememberSaveable { mutableStateOf("") }
    var newSustainabilityGoals by rememberSaveable { mutableStateOf("") }
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
        if (isEditMode && project != null) {
            newProjectName = project.projectName
            newProjectDescription = project.description
            newStatus = project.status
            newStartDate = project.startDate
            newEndDate = project.endDate
            newBudget = project.budget
            newProjectManager = project.projectManager
            newProjectMembers = project.teamMembers
            newStakeholders = project.stakeholders
            newSustainabilityGoals = project.sustainabilityGoals
        }
    }

    if (project == null) {
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

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    //.padding(),
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
                            .background(Color.White, RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isEditMode) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
                                    CustomOutlinedTextField(
                                        value = newProjectName,
                                        onValueChange = { newProjectName = it },
                                        label = "Project Name"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newProjectDescription,
                                        onValueChange = { newProjectDescription = it },
                                        label = "Description"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newStatus,
                                        onValueChange = { newStatus = it },
                                        label = "Status"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newStartDate,
                                        onValueChange = { newStartDate = it },
                                        label = "Start Date"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newEndDate,
                                        onValueChange = { newEndDate = it },
                                        label = "End Date"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newBudget,
                                        onValueChange = { newBudget = it },
                                        label = "Budget"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newProjectManager,
                                        onValueChange = { newProjectManager = it },
                                        label = "Project Manager"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newProjectMembers,
                                        onValueChange = { newProjectMembers = it },
                                        label = "Project Members"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newStakeholders,
                                        onValueChange = { newStakeholders = it },
                                        label = "Stakeholders"
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newSustainabilityGoals,
                                        onValueChange = { newSustainabilityGoals = it },
                                        label = "Sustainability Goals"
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val projectId = project.id

                                            val updatedProject = Project(
                                                projectName = newProjectName.ifEmpty { project.projectName },
                                                description = newProjectDescription.ifEmpty { project.description },
                                                status = newStatus.ifEmpty { project.status },
                                                startDate = newStartDate.ifEmpty { project.startDate },
                                                endDate = newEndDate.ifEmpty { project.endDate },
                                                budget = newBudget.ifEmpty { project.budget },
                                                projectManager = newProjectManager.ifEmpty { project.projectManager },
                                                teamMembers = newProjectMembers.ifEmpty { project.teamMembers },
                                                stakeholders = newStakeholders.ifEmpty { project.stakeholders },
                                                sustainabilityGoals = newSustainabilityGoals.ifEmpty { project.sustainabilityGoals }
                                            )
                                            // Save updated project to database
                                            database.child("ProjectTransparency").child(projectId)
                                                .setValue(updatedProject)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Project updated successfully",
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
                                                text = "Edit",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Text(
                                        text = "Delete",
                                        fontSize = 14.sp,
                                        color = SKyberRed,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable {
                                            val database = FirebaseHelper.databaseReference
                                            val projectId = project.id
                                            database.child("ProjectTransparency").child(projectId)
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
                        } else {// Display project details
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.TopStart)
                                                .padding(horizontal = 16.dp, vertical = 16.dp),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            // Project Name and Status
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = project.projectName,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 28.sp,
                                                    color = SKyberBlue,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(22.dp))
                                                        .background(if (project.status.lowercase() == "ongoing") BoxGreen else BoxRed)
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = project.status,
                                                        fontSize = 18.sp,
                                                        color = if (project.status.lowercase() == "ongoing") BoxTextGreen else SKyberRed,
                                                        fontWeight = FontWeight.SemiBold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Project Manager and Budget
                                            Column {
                                                Text(
                                                    text = "Project Manager: ${project.projectManager}",
                                                    fontSize = 16.sp,
                                                    color = SKyberBlue
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = "Budget: ${project.budget}",
                                                    fontSize = 16.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Start and End Dates
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceEvenly
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = "Start Date",
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = SKyberBlue
                                                    )
                                                    Text(text = project.startDate, color = SKyberBlue)
                                                }
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = "End Date",
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = SKyberBlue
                                                    )
                                                    Text(text = project.endDate, color = SKyberBlue)
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Description
                                            Column {
                                                Text(
                                                    text = "Description",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                    color = SKyberBlue
                                                )

                                                Text(
                                                    text = project.description,
                                                    fontSize = 14.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Stakeholders
                                            Column {
                                                Text(
                                                    text = "Stakeholders",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                    color = SKyberBlue
                                                )

                                                Text(
                                                    text = project.stakeholders,
                                                    fontSize = 14.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Project Members
                                            Column {
                                                Text(
                                                    text = "Project Members",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 18.sp,
                                                    color = SKyberBlue
                                                )

                                                Text(
                                                    text = project.teamMembers,
                                                    fontSize = 14.sp,
                                                    color = SKyberBlue
                                                )
                                            }
                                        }
                                    }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            if(user != null){
                                                if(user.role == "ADMIN"){
                                                    Button(
                                                        onClick = { isEditMode = true },
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
                                                }else{
                                                    null
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


/*Box(
    modifier = Modifier
        .height(200.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        //.background(Color.Gray.copy(alpha = 0.1f)),
    contentAlignment = Alignment.Center
) {
    if (!project.imageData.isNullOrEmpty()) {
        Base64Image(
            base64String = project.imageData,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
    } else {
        Text(
            text = "No image available",
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
    }
}
*/