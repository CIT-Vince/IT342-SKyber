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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.Base64Image
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ImageUtils
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.gradientBrush

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
    }else{
            Scaffold { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SKyberDarkBlueGradient)
                ) {
                    // Particle system as the background
                    ParticleSystem(
                        modifier = Modifier.fillMaxSize(),
                        particleColor = Color.White,
                        particleCount = 50,
                        backgroundColor = Color(0xFF0D47A1)
                    )
                    Text(
                        text = "💠",
                        fontSize = 26.sp,
                        modifier = Modifier
                            .padding(start = topLeftPosition.dp + 10.dp, top = 20.dp)
                            .graphicsLayer(alpha = 0.5f)
                    )

                    // Main content on top of the particle system
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
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            if (isEditMode) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
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
                                            label = "Description"
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
                                                            firstName = newFirstname.ifEmpty { candidateProfile.firstName },
                                                            lastName = newLastname.ifEmpty { candidateProfile.lastName },
                                                            age = newAge.ifEmpty { candidateProfile.age },
                                                            partylist = newPartylist.ifEmpty { candidateProfile.partylist },
                                                            platform = newPlatform.ifEmpty { candidateProfile.platform },
                                                            address = newAddress.ifEmpty{ candidateProfile.address},
                                                            candidateImage = newBase64Image?.ifEmpty { candidateProfile.candidateImage },
                                                        )
                                                        // Save updated project to database
                                                        database.child("Candidates").child(candidateId)
                                                            .setValue(updatedCandidateProfile)
                                                            .addOnSuccessListener {
                                                                Toast.makeText(
                                                                    context,
                                                                    "updated successfully",
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
                                                }
                                            )
                                    }
                                }
                            } else {// Display project details
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 24.dp, vertical = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    item {
                                        // Profile Image Section
                                        Box(
                                            modifier = Modifier
                                                .size(220.dp)
                                                .clip(CircleShape)
                                                .background(Color.Gray.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (!candidateProfile.candidateImage.isNullOrEmpty()) {
                                                Base64Image(
                                                    base64String = candidateProfile.candidateImage,
                                                    modifier = Modifier
                                                        .size(200.dp)
                                                        .clip(CircleShape)
                                                )
                                            } else {
                                                Text(
                                                    text = "No image available",
                                                    color = Color.Gray,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // Candidate Name
                                        Text(
                                            text = "${candidateProfile.firstName} ${candidateProfile.lastName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 26.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Age
                                        Text(
                                            text = "Age: ${candidateProfile.age}",
                                            fontSize = 16.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Address
                                        Text(
                                            text = "${candidateProfile.address}",
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Partylist
                                        Text(
                                            text = "${candidateProfile.partylist}",
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // Platforms Title
                                        Text(
                                            text = "Platforms",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Platform Description
                                        Text(
                                            text = "${candidateProfile.platform}",
                                            fontSize = 14.sp,
                                            color = SKyberBlue,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        if(user != null){// Update Button
                                            if(user.role == "ADMIN"){

                                                Button(
                                                    onClick = { isEditMode = true },
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
                                            }else{
                                                null
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}

