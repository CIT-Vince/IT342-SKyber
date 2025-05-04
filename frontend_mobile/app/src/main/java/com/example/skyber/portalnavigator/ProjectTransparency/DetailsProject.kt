package com.example.skyber.portalnavigator.ProjectTransparency

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.dataclass.Project
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxRed
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

@Composable
fun DetailsProject(navController: NavHostController) {
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
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient)
        ) {
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
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = project.projectName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        color = SKyberBlue
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(22.dp))
                                            .background(if (project.status.lowercase() == "ongoing") BoxGreen else BoxRed)
                                            .padding(horizontal = 8.dp)
                                            .width(80.dp)
                                    ) {
                                        Text(
                                            text = project.status,
                                            fontSize = 18.sp,
                                            color = if (project.status.lowercase() == "ongoing") BoxTextGreen else SKyberRed,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    "Project Manager: ${project.projectManager}",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "Budget: ${project.budget}",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "Start Date",
                                            fontWeight = FontWeight.SemiBold,
                                            color = SKyberBlue
                                        )
                                        Text(project.startDate, color = SKyberBlue)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "End Date",
                                            fontWeight = FontWeight.SemiBold,
                                            color = SKyberBlue
                                        )
                                        Text(project.endDate, color = SKyberBlue)
                                    }
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    "Description",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = SKyberBlue
                                )

                                Text(project.description, fontSize = 14.sp, color = SKyberBlue)

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    "Stakeholders",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = SKyberBlue
                                )

                                Text(project.stakeholders, fontSize = 14.sp, color = SKyberBlue)

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    "Project Members",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = SKyberBlue
                                )

                                Text(project.teamMembers, fontSize = 14.sp, color = SKyberBlue)

                                Spacer(modifier = Modifier.height(24.dp))

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

                                Spacer(modifier = Modifier.height(18.dp))

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
                    }
                }
            }
        }
    }
}
