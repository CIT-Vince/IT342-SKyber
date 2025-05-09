package com.example.skyber.navigationbar.portalnavigator.Scholarships

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import android.content.ActivityNotFoundException

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScholarship(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val scholarship = navController.previousBackStackEntry?.savedStateHandle?.get<Scholarship>("scholarship")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newDescription by rememberSaveable { mutableStateOf("") }
    var newLink by rememberSaveable { mutableStateOf("") }
    var newContactEmail by rememberSaveable { mutableStateOf("") }
    var newType by rememberSaveable { mutableStateOf("") }
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
        if (isEditMode && scholarship != null) {
            newTitle = scholarship.title
            newDescription = scholarship.description
            newLink = scholarship.link
            newContactEmail = scholarship.contactEmail
            newType = scholarship.type
            newBase64Image = scholarship.scholarImage
        }
    }

    if (scholarship == null) {
        // Show a loading spinner while waiting for data
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
                    text = "📚",
                    fontSize = 26.sp,
                    modifier = Modifier
                        .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                        .graphicsLayer(alpha = 0.3f)
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
                                            text = "Upload Scholarship Image",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newTitle,
                                        onValueChange = { newTitle = it },
                                        label = "Scholarship Title"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newType,
                                        onValueChange = { newType = it },
                                        label = "Type (Public or Private)"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newContactEmail,
                                        onValueChange = { newContactEmail = it },
                                        label = "Contact Email"
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    CustomOutlinedTextField(
                                        value = newLink,
                                        onValueChange = { newLink = it },
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

                                    // Update button
                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val scholarshipId = scholarship.id

                                            // Build updated scholarship
                                            val updatedScholarship = Scholarship(
                                                id = scholarshipId,
                                                title = newTitle.ifEmpty { scholarship.title },
                                                description = newDescription.ifEmpty { scholarship.description },
                                                link = newLink.ifEmpty { scholarship.link },
                                                contactEmail = newContactEmail.ifEmpty { scholarship.contactEmail },
                                                type = newType.ifEmpty { scholarship.type },
                                                scholarImage = newBase64Image ?: scholarship.scholarImage
                                            )

                                            database.child("Scholarships").child(scholarshipId)
                                                .setValue(updatedScholarship)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Scholarship updated successfully",
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

                                    // Delete option
                                    Text(
                                        text = "Delete",
                                        fontSize = 14.sp,
                                        color = SKyberRed,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.clickable {
                                            val database = FirebaseHelper.databaseReference
                                            val scholarshipId = scholarship.id
                                            database.child("Scholarships").child(scholarshipId)
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
                                            // Scholarship Type Badge
                                            Box(
                                                modifier = Modifier
                                                    .padding(bottom = 16.dp)
                                                    .background(
                                                        if (scholarship.type.equals("Public", ignoreCase = true))
                                                            SKyberBlue else SKyberYellow,
                                                        RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = scholarship.type.uppercase(),
                                                    color = if (scholarship.type.equals("Public", ignoreCase = true))
                                                        Color.White else Color.Black,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            // Scholarship Image
                                            if (!scholarship.scholarImage.isNullOrEmpty()) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(200.dp)
                                                        .clip(RoundedCornerShape(16.dp))
                                                        .background(Color.LightGray),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Base64Image(
                                                        base64String = scholarship.scholarImage,
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .clip(RoundedCornerShape(16.dp))
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(16.dp))
                                            }

                                            // Scholarship Title
                                            Text(
                                                text = scholarship.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 26.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Contact Email
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Email,
                                                    contentDescription = "Contact Email",
                                                    tint = SKyberBlue,
                                                    modifier = Modifier.size(18.dp)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                    text = scholarship.contactEmail,
                                                    fontSize = 16.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Institution Type
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.School,
                                                    contentDescription = "Scholarship Type",
                                                    tint = SKyberBlue,
                                                    modifier = Modifier.size(18.dp)
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                    text = "${scholarship.type} Scholarship",
                                                    fontSize = 16.sp,
                                                    color = SKyberBlue
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Description Section
                                            Text(
                                                text = "Description",
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
                                                    text = scholarship.description,
                                                    fontSize = 16.sp,
                                                    lineHeight = 24.sp,
                                                    color = Color(0xFF424242)
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Apply Now Button
                                            Button(
                                                onClick = {
                                                    try {
                                                        // Check if the link is empty
                                                        if (scholarship.link.isEmpty()) {
                                                            Toast.makeText(
                                                                context,
                                                                "No application link provided. Please contact via the email provided.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        } else {
                                                            // Make sure the URL has a proper scheme
                                                            val url = if (!scholarship.link.startsWith("http://") &&
                                                                !scholarship.link.startsWith("https://")) {
                                                                "https://${scholarship.link}"
                                                            } else {
                                                                scholarship.link
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

                                            // Contact via Email Button
                                            Spacer(modifier = Modifier.height(16.dp))

                                            OutlinedButton(
                                                onClick = {
                                                    try {
                                                        if (scholarship.contactEmail.isEmpty()) {
                                                            Toast.makeText(
                                                                context,
                                                                "No contact email provided.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        } else {
                                                            // Create email intent
                                                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                                data = Uri.parse("mailto:${scholarship.contactEmail}")
                                                            }

                                                            // Launch intent directly without checking resolveActivity
                                                            context.startActivity(intent)
                                                        }
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "Error opening email: ${e.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                border = BorderStroke(1.dp, SKyberBlue),
                                                shape = RoundedCornerShape(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Email,
                                                    contentDescription = "Email",
                                                    tint = SKyberBlue
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Contact via Email",
                                                    color = SKyberBlue,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(16.dp))

                                            // Admin Edit Button
                                            if (user?.role == "ADMIN") {
                                                OutlinedButton(
                                                    onClick = { isEditMode = true },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    border = BorderStroke(1.dp, SKyberBlue),
                                                    shape = RoundedCornerShape(28.dp)
                                                ) {
                                                    Text(
                                                        text = "Edit Scholarship",
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
fun DetailsScholarshipPreview() {
    // Create a mock scholarship for preview
    val mockScholarship = Scholarship(
        id = "scholar1",
        title = "DOST Scholarship Program",
        description = "The DOST-SEI Undergraduate Scholarship program aims to support talented and deserving Filipino students who wish to pursue careers in the fields of science, technology, engineering and mathematics.",
        link = "https://sei.dost.gov.ph/",
        contactEmail = "info@sei.dost.gov.ph",
        type = "Public",
        scholarImage = null
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
                        text = "     Scholarship Details",
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
                            // Scholarship Type Badge
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
                                    text = "PUBLIC",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Scholarship Title
                            Text(
                                text = mockScholarship.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Contact Email
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Contact Email",
                                    tint = SKyberBlue,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = mockScholarship.contactEmail,
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Institution Type
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "Scholarship Type",
                                    tint = SKyberBlue,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Public Scholarship",
                                    fontSize = 16.sp,
                                    color = SKyberBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Description Section
                            Text(
                                text = "Description",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Content Section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 120.dp)
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = mockScholarship.description,
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

                            // Contact via Email Button
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedButton(
                                onClick = { /* For preview only */ },
                                modifier = Modifier.fillMaxWidth(),
                                border = BorderStroke(1.dp, SKyberBlue),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = SKyberBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Contact via Email",
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