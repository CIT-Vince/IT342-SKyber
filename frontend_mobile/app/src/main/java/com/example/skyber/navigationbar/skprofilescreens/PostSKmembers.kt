package com.example.skyber.navigationbar.skprofilescreens

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.Base64Image
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ImageUtils
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.TransparentSimpleDatePickerField
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import com.example.skyber.userauth.RadioButtonGenders
import com.google.firebase.database.DatabaseReference

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostSKmembers(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user
    var uid by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var term by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("ADMIN") }
    val context = LocalContext.current
    // State to hold selected image in Base64
    var base64Image by remember { mutableStateOf<String?>(null) }

    // Launch image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            base64Image = ImageUtils.bitmapToBase64(bitmap)
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

    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
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
                ){
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
                        item { // Image Preview
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                if (base64Image != null) {
                                    Base64Image(
                                        base64String = base64Image,
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

                            Button(// Upload Button
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

                            Spacer(modifier = Modifier.width(16.dp))

                            CustomOutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = "First Name",
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            CustomOutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = "Last Name",
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email",
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = position,
                                onValueChange = { position = it },
                                label = "Position",
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = term,
                                onValueChange = { term = it },
                                label = "Term",
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = platform,
                                onValueChange = { platform = it },
                                label = "Platforms",
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            TransparentSimpleDatePickerField(
                                selectedDate = birthdate,
                                onDateSelected = { newDate -> birthdate = newDate },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            RadioButtonGenders(
                                gender = gender,
                                onGenderSelected = { gender = it },
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = age,
                                onValueChange = { age = it },
                                label = "Age",
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = "Phone Number",
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = "Address",
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    // Create SK member  object
                                   val ageNumber = age.toIntOrNull()
                                    if  (firstName.isBlank() || lastName.isBlank() || address.isBlank() || platform.isBlank()){
                                        com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.showToast(
                                            context,
                                            "Please fill out required fields"
                                        )
                                    }else if (ageNumber == null || ageNumber <= 0)  {
                                        com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.showToast(
                                            context,
                                            "Please enter a valid age"
                                        )
                                    } else {
                                        val databaseRef = FirebaseHelper.databaseReference.child("SKProfiles").push()
                                        val memberId = databaseRef.key
                                        val ageNumber = age.toIntOrNull() ?: 0 // Convert age to Int safely
                                        val newMemberProfile = SKProfile(
                                            uid = memberId,
                                            firstName = firstName,
                                            lastName = lastName,
                                            email = email,
                                            position = position,
                                            term = term,
                                            platform = platform,
                                            birthdate = birthdate,
                                            gender = gender,
                                            age = ageNumber,
                                            phoneNumber = phoneNumber,
                                            address = address,
                                            skImage = base64Image,
                                            role = role
                                        )

                                        uploadMemberProfile(newMemberProfile, context, navController, databaseRef)
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
                                        text = "Post Candidate",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                }
                            }

                        }//End of lazy Column content
                    }//End of alzy column
                }
            }
        }
    }
}


fun uploadMemberProfile(skProfile: SKProfile, context: Context, navController: NavHostController, databaseRef: DatabaseReference) {
    databaseRef.setValue(skProfile)
        .addOnSuccessListener {
            com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.showToast(
                context,
                "Member Profile uploaded successfully"
            )// Show success toast when announcement is uploaded successfully
            navController.navigate(Screens.SKcandidates.screen)
        }
        .addOnFailureListener { error ->
            com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.showToast(
                context,
                "Failed to Upload Member Profile"
            )    // Show failure toast when something goes wrong
        }
}
