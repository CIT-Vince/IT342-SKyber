package com.example.skyber.navigationbar.portalnavigator.Job

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.Base64Image
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ImageUtils
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsJob(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val jobListing = navController.previousBackStackEntry?.savedStateHandle?.get<JobListing>("joblisting")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newCompany by rememberSaveable { mutableStateOf("") }
    var newAddress by rememberSaveable { mutableStateOf("") }
    var newDescription by rememberSaveable { mutableStateOf("") }
    var newApplicationLink by rememberSaveable { mutableStateOf("") }
    var newEmploymentType by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var newBase64Image by remember { mutableStateOf<String?>(null) }

    // Launch image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            newBase64Image = ImageUtils.bitmapToBase64(bitmap)
        }
    }

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

    LaunchedEffect(isEditMode) {
        if (isEditMode && jobListing != null) {
            newTitle = jobListing.jobTitle
            newCompany = jobListing.companyName
            newAddress = jobListing.address
            newDescription = jobListing.description
            newApplicationLink = jobListing.applicationLink
            newEmploymentType = jobListing.employementType
            newBase64Image = jobListing.jobImage
        }
    }

    if (jobListing == null) {
        // Show a loading spinner while waiting for job data
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
                            // Admin Edit Mode
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item {
                                    // Image Preview
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(Color.LightGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (newBase64Image != null) {
                                            Base64Image(
                                                base64String = newBase64Image,
                                                modifier = Modifier
                                                    .size(200.dp)
                                                    .clip(RoundedCornerShape(16.dp))
                                            )
                                        } else {
                                            Text(
                                                text = "No image selected",
                                                color = Color.Gray,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Upload Button
                                    Button(
                                        onClick = { launcher.launch("image/*") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.UploadFile,
                                            contentDescription = "Upload",
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Upload Job Image",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newTitle,
                                        onValueChange = { newTitle = it },
                                        label = "Job Title"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newCompany,
                                        onValueChange = { newCompany = it },
                                        label = "Company Name"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newAddress,
                                        onValueChange = { newAddress = it },
                                        label = "Address"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newEmploymentType,
                                        onValueChange = { newEmploymentType = it },
                                        label = "Employment Type (Full-Time or Part-Time)"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newApplicationLink,
                                        onValueChange = { newApplicationLink = it },
                                        label = "Application Link"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newDescription,
                                        onValueChange = { newDescription = it },
                                        label = "Description",
                                        maxLines = 5
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Button(//Update button
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val jobId = jobListing.id

                                            // Build updated job
                                            val updatedJob = JobListing(
                                                id = jobId,
                                                jobTitle = newTitle.ifEmpty { jobListing.jobTitle },
                                                companyName = newCompany.ifEmpty { jobListing.companyName },
                                                address = newAddress.ifEmpty { jobListing.address },
                                                description = newDescription.ifEmpty { jobListing.description },
                                                applicationLink = newApplicationLink.ifEmpty { jobListing.applicationLink },
                                                employementType = newEmploymentType.ifEmpty { jobListing.employementType },
                                                jobImage = newBase64Image ?: jobListing.jobImage
                                            )
                                            database.child("JobListings").child(jobId)
                                                .setValue(updatedJob)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Job updated successfully",
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
                                            val jobId = jobListing.id
                                            database.child("JobListings").child(jobId)
                                                .removeValue()
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Deleted Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    navController.popBackStack()
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
                        } else {
                            // User view mode
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.TopStart),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            // Employment Type Badge
                                            Box(
                                                modifier = Modifier
                                                    .padding(bottom = 16.dp)
                                                    .background(
                                                        if (jobListing.employementType.equals("Full-time", ignoreCase = true))
                                                            SKyberBlue else SKyberYellow,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = jobListing.employementType.uppercase(),
                                                    color = if (jobListing.employementType.equals("Full-time", ignoreCase = true))
                                                        Color.White else Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            // Job Image
                                            if (!jobListing.jobImage.isNullOrEmpty()) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(200.dp)
                                                        .clip(RoundedCornerShape(16.dp))
                                                        .background(Color.LightGray),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Base64Image(
                                                        base64String = jobListing.jobImage,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(RoundedCornerShape(16.dp))
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(16.dp))
                                            }

                                            // Job Title
                                            Text(
                                                text = jobListing.jobTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 26.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Company Name
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Business,
                                                    contentDescription = "Company",
                                                    tint = SKyberBlue,
                                                    modifier = Modifier.size(18.dp)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                    text = jobListing.companyName,
                                                    fontSize = 18.sp,
                                                    color = SKyberBlue,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Location
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.LocationOn,
                                                    contentDescription = "Location",
                                                    tint = SKyberBlue,
                                                    modifier = Modifier.size(18.dp)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                    text = jobListing.address,
                                                    fontSize = 16.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Job Description Section
                                            Text(
                                                text = "Job Description",
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Content Section
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(min = 180.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .padding(vertical = 12.dp)
                                            ) {
                                                Text(
                                                    text = jobListing.description,
                                                    fontSize = 16.sp,
                                                    lineHeight = 24.sp,
                                                    color = Color(0xFF424242)
                                                )
                                            }


                                            Spacer(modifier = Modifier.height(16.dp))

// Apply Now Button
                                            Button(
                                                onClick = {
                                                    try {
                                                        // Check if the link is empty
                                                        if (jobListing.applicationLink.isEmpty()) {
                                                            Toast.makeText(
                                                                context,
                                                                "No application link provided. Please contact the employer directly.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        } else {
                                                            // Make sure the URL has a proper scheme
                                                            val url = if (!jobListing.applicationLink.startsWith("http://") &&
                                                                !jobListing.applicationLink.startsWith("https://")) {
                                                                "https://${jobListing.applicationLink}"
                                                            } else {
                                                                jobListing.applicationLink
                                                            }

                                                            // Create and launch the intent directly without checking resolveActivity
                                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                            context.startActivity(intent)
                                                        }
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "Error opening link: ${e.message}",
                                                            Toast.LENGTH_LONG
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
                                                        text = "Apply Now",
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = Color.White
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Admin Edit Button
                                            if (user?.role == "ADMIN") {
                                                OutlinedButton(
                                                    onClick = { isEditMode = true },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    border = BorderStroke(1.dp, SKyberBlue)
                                                ) {
                                                    Text(
                                                        text = "Edit Job Listing",
                                                        color = SKyberBlue,
                                                        fontWeight = FontWeight.Medium
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
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsJobPreview() {
    // Create a mock job listing for preview
    val mockJob = JobListing(
        id = "job1",
        jobTitle = "Construction Helper (2 Slots Available)",
        companyName = "SolidBuild Construction Services",
        address = "Lahug Siguro, Cebu City",
        description = "Qualifications: Male, 18-35 years old. Physically fit and willing to do manual labor. With or without experience. Salary: ₱450 per day. Inclusions: Free lunch and transportation allowance. How to Apply: Submit your barangay clearance and valid ID to the Barangay Hall or contact 0912-345-6789.",
        applicationLink = "https://example.com/apply",
        employementType = "Full-time",
        jobImage = null
    )

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SKyberDarkBlueGradient)
        ) {
            // Preview doesn't support particle system, so we'll omit it

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Simple header for preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(SKyberBlue),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "     Job Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 40.dp, bottom = 40.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopStart),
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Employment Type Badge
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .background(
                                        SKyberBlue,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "FULL-TIME",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Job Title
                            Text(
                                text = mockJob.jobTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Company Name
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = "Company",
                                    tint = SKyberBlue,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = mockJob.companyName,
                                    fontSize = 18.sp,
                                    color = SKyberBlue,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Location
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = SKyberBlue,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = mockJob.address,
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Job Description Section
                            Text(
                                text = "Job Description",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Content Section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 180.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = mockJob.description,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = Color(0xFF424242)
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Apply Now Button
                            Button(
                                onClick = { /* For preview only */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(28.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue)
                            ) {
                                Text(
                                    text = "Apply Now",
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
}