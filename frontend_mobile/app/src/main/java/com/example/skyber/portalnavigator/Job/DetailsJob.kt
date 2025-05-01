package com.example.skyber.portalnavigator.Job

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.JobListing
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.ParticleSystem
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsJob(navController: NavHostController) {
    val joblisting =
        navController.previousBackStackEntry?.savedStateHandle?.get<JobListing>("joblisting")
    var newJobtitle by remember { mutableStateOf("") }
    var newCompanyname by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }
    var newApplicationLink by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isEditMode by remember { mutableStateOf(false) }

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
        if (isEditMode && joblisting != null) {
            newJobtitle = joblisting.jobTitle
            newCompanyname = joblisting.companyName
            newDescription = joblisting.description
            newCategory = joblisting.employmentType
            newAddress = joblisting.address
            newApplicationLink = joblisting.applicationLink
        }
    }

    if (joblisting == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    } else {
        val jobCategory = joblisting.employmentType.lowercase() == "full-time"
        val statusColor = if (jobCategory) BoxGreen else SoftCardContainerBlue
        val textColor = if (jobCategory) BoxTextGreen else SoftCardFontBlue
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
                        if (isEditMode) {//Edit mode self explainatory
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item {
                                    //Text Fields here
                                    OutlinedTextField(
                                        value = newJobtitle,
                                        onValueChange = { newJobtitle = it },
                                        label = { Text("Job Title") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = newCompanyname,
                                        onValueChange = { newCompanyname = it },
                                        label = { Text("Company Name") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = newApplicationLink,
                                        onValueChange = { newApplicationLink = it },
                                        label = { Text("Apply Here") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CategoryDropdown(
                                        selectedCategory = newCategory,
                                        onCategorySelected = { newCategory = it }
                                    )


                                    Spacer(modifier = Modifier.height(12.dp))


                                    OutlinedTextField(
                                        value = newDescription,
                                        onValueChange = { newDescription = it },
                                        label = { Text("Job Description") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = newAddress,
                                        onValueChange = { newAddress = it },
                                        label = { Text("Address") },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF0066FF),
                                            unfocusedBorderColor = Color(0xFFD1D5DB)
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val jobListingId = joblisting.id

                                            // Build updated joblisting
                                            val updatedJoblisting = JobListing(
                                                id = jobListingId,
                                                jobTitle = newJobtitle.ifEmpty { joblisting.jobTitle },
                                                companyName = newCompanyname.ifEmpty { joblisting.companyName },
                                                description = newDescription.ifEmpty { joblisting.description },
                                                applicationLink = newApplicationLink.ifEmpty { joblisting.applicationLink },
                                                employmentType = newCategory.ifEmpty { joblisting.employmentType },
                                                address = newAddress.ifEmpty { joblisting.address }
                                            )
                                            database.child("JobListing").child(jobListingId)
                                                .setValue(updatedJoblisting)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Job Listing updated successfully",
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
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Delete",
                                        fontSize = 14.sp,
                                        color = SKyberRed,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable {
                                            val database = FirebaseHelper.databaseReference
                                            val jobListingId = joblisting.id
                                            database.child("JobListing").child(jobListingId)
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

                                }//End of lazy Column content
                            }//End of alzy column
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
                                                text = joblisting.jobTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 28.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.width(14.dp))

                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(22.dp))
                                                    .background(statusColor)
                                                    .padding(horizontal = 8.dp)
                                                    .wrapContentWidth()

                                            ) {
                                                Text(
                                                    text = joblisting.employmentType,
                                                    fontSize = 20.sp,
                                                    color = textColor,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = "Company Name: ${joblisting.companyName}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Apply at: ${joblisting.applicationLink}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Location: ${joblisting.address}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(24.dp))

                                            Text(
                                                text = "Job Description",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )
                                            Spacer(modifier = Modifier.height(10.dp))

                                            Box(
                                                modifier = Modifier.heightIn(min = 100.dp) //Caveman custom height setter
                                            ) {
                                                Text(
                                                    text = joblisting.description,
                                                    fontSize = 14.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

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

                                            Text(
                                                text = "Delete",
                                                fontSize = 14.sp,
                                                color = SKyberRed,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.clickable {
                                                    val database = FirebaseHelper.databaseReference
                                                    val jobListingId = joblisting.id
                                                    database.child("JobListing").child(jobListingId)
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
