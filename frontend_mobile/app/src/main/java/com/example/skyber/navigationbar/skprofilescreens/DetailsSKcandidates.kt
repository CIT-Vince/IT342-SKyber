package com.example.skyber.navigationbar.skprofilescreens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import androidx.compose.ui.tooling.preview.Preview

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsSKcandidates(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val candidateProfile = navController.previousBackStackEntry?.savedStateHandle?.get<CandidateProfile>("CandidateProfile")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newPlatform by remember { mutableStateOf("") }
    var newFirstname by remember { mutableStateOf("") }
    var newLastname by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("") }
    var newPartylist by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    val context = LocalContext.current
    // State to hold selected image in Base64
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
        if (isEditMode && candidateProfile != null) {
            newFirstname = candidateProfile.firstName ?: ""
            newLastname = candidateProfile.lastName ?: ""
            newPlatform = candidateProfile.platform ?: ""
            newAge = candidateProfile.age ?: ""
            newPartylist = candidateProfile.partylist ?: ""
            newAddress = candidateProfile.address ?: ""
            newBase64Image = candidateProfile.candidateImage
        }
    }

    if (candidateProfile == null) {
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
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))  // Light background for better contrast
            ) {
                // Main content layout
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Custom Header similar to web UI
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(SKyberBlue)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "← Back",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    navController.popBackStack()
                                }
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "Candidate Profile",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    if (isEditMode) {
                        // Edit Mode Content
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            item {
                                // Keep existing Edit mode UI
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
                                        text = "Upload Candidate Image",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomOutlinedTextField(
                                    value = newFirstname,
                                    onValueChange = { newFirstname = it },
                                    label = "First Name"
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomOutlinedTextField(
                                    value = newLastname,
                                    onValueChange = { newLastname = it },
                                    label = "Last Name"
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                CustomOutlinedTextField(
                                    value = newAge,
                                    onValueChange = { newAge = it },
                                    label = "Age",
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                CustomOutlinedTextField(
                                    value = newPlatform,
                                    onValueChange = { newPlatform = it },
                                    label = "Platform",
                                    maxLines = 5
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                CustomOutlinedTextField(
                                    value = newPartylist,
                                    onValueChange = { newPartylist = it },
                                    label = "Party List"
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val candidateId = candidateProfile.id
                                        if(candidateId != null){
                                            val updatedCandidateProfile = CandidateProfile(
                                                id = candidateId,
                                                firstName = newFirstname.ifEmpty { candidateProfile.firstName },
                                                lastName = newLastname.ifEmpty { candidateProfile.lastName },
                                                age = newAge.ifEmpty { candidateProfile.age },
                                                partylist = newPartylist.ifEmpty { candidateProfile.partylist },
                                                platform = newPlatform.ifEmpty { candidateProfile.platform },
                                                address = newAddress.ifEmpty{ candidateProfile.address},
                                                candidateImage = newBase64Image ?: candidateProfile.candidateImage,
                                            )
                                            // Save updated project to database
                                            database.child("Candidates").child(candidateId)
                                                .setValue(updatedCandidateProfile)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Updated successfully",
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
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(28.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue)
                                ) {
                                    Text(
                                        text = "Save Changes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Delete button
                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val candidateId = candidateProfile.id
                                        if(candidateId != null){
                                            database.child("Candidates").child(candidateId)
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
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(28.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SKyberRed)
                                ) {
                                    Text(
                                        text = "Delete Candidate",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        // Display Mode - Similar to Web UI
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item {
                                // Profile card
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 24.dp, bottom = 16.dp)
                                        .background(Color.White, RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Profile Image - Circular as in web UI
                                        Box(
                                            modifier = Modifier
                                                .size(180.dp)
                                                .clip(CircleShape)
                                                .background(Color.Gray.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (!candidateProfile.candidateImage.isNullOrEmpty()) {
                                                Base64Image(
                                                    base64String = candidateProfile.candidateImage,
                                                    modifier = Modifier
                                                        .size(170.dp)
                                                        .clip(CircleShape)
                                                )
                                            } else {
                                                Text(
                                                    text = "No image",
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Candidate Name - Larger font like in web UI
                                        Text(
                                            text = "${candidateProfile.firstName} ${candidateProfile.lastName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Party list badge
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(SKyberBlue.copy(alpha = 0.2f))
                                                .padding(horizontal = 16.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = candidateProfile.partylist ?: "",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 16.sp,
                                                color = SKyberBlue
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Age and Address like in web UI
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Age: ",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    color = Color.Gray
                                                )
                                                Text(
                                                    text = candidateProfile.age ?: "",
                                                    fontSize = 16.sp,
                                                    color = Color.DarkGray
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    text = "Address: ",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 16.sp,
                                                    color = Color.Gray
                                                )
                                                Text(
                                                    text = candidateProfile.address ?: "",
                                                    fontSize = 16.sp,
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }
                                    }
                                }

                                // Platform section
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .background(Color.White, RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Platform",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Format platform text as bullet points if it contains line breaks
                                        val platformText = candidateProfile.platform ?: ""
                                        if (platformText.contains("\n")) {
                                            val platforms = platformText.split("\n")
                                            platforms.forEach { platform ->
                                                if (platform.isNotBlank()) {
                                                    Row(
                                                        modifier = Modifier.padding(vertical = 4.dp),
                                                        verticalAlignment = Alignment.Top
                                                    ) {
                                                        Text(
                                                            text = "• ",
                                                            fontSize = 16.sp,
                                                            color = SKyberBlue
                                                        )
                                                        Text(
                                                            text = platform.trim(),
                                                            fontSize = 16.sp,
                                                            color = Color.DarkGray
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            // Single line platform
                                            Text(
                                                text = platformText,
                                                fontSize = 16.sp,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }
                                }

                                // Admin Update Button
                                if (user?.role == "ADMIN") {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = { isEditMode = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp),
                                        shape = RoundedCornerShape(28.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SKyberBlue)
                                    ) {
                                        Text(
                                            text = "Update Candidate Information",
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
    }
}
@Preview(showBackground = true)
@Composable
fun DetailsSKcandidatesPreview() {
    // Create mock data for preview
    val mockCandidateProfile = CandidateProfile(
        id = "preview-id",
        firstName = "John",
        lastName = "Doe",
        age = "35",
        partylist = "Sample Party",
        platform = "Sample platform focusing on education reform, healthcare accessibility, and environmental protection initiatives.",
        address = "Manila, Philippines",
        candidateImage = null
    )

    // Create a simple theme wrapper
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SKyberDarkBlueGradient)
    ) {
        // Particle system as background (simplified for preview)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D47A1).copy(alpha = 0.5f))
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mock header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(SKyberBlue),
                contentAlignment = Alignment.Center
            ) {
                Text("SKyber", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 40.dp, bottom = 40.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image Section
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Preview Image",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "John Doe",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = SKyberBlue
                )

                Text(
                    text = "Sample Party",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.padding(4.dp)) {
                    Text(
                        "Platforms",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = SKyberBlue
                    )
                    Text(
                        "Sample platform focusing on education reform, healthcare accessibility, and environmental protection initiatives.",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        "Age 35",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Manila, Philippines",
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
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
            }
        }
    }
}