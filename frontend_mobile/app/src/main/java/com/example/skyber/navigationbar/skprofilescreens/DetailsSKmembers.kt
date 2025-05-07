package com.example.skyber.navigationbar.skprofilescreens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.Base64Image
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ImageUtils
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.ModularFunctions.SimpleDatePickerField
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import com.example.skyber.userauth.RadioButtonGenders

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsSKmembers(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value
    val skProfile = navController.previousBackStackEntry?.savedStateHandle?.get<SKProfile>("SKProfile")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newFirstname by remember { mutableStateOf("") }
    var newLastname by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPosition by remember { mutableStateOf("") }
    var newTerm by remember { mutableStateOf("") }
    var newPlatform by remember { mutableStateOf("") }
    var newBirthdate by remember { mutableStateOf("") }
    var newGender by remember { mutableStateOf("") }
    var newAge by remember { mutableStateOf("") }
    var newPhoneNumber by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    val context = LocalContext.current
    // State to hold selected image in Base64
    var newBase64Image by remember { mutableStateOf<String?>(null) }

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

    // Launch image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            newBase64Image = ImageUtils.bitmapToBase64(bitmap)
        }
    }

    LaunchedEffect(isEditMode) {
        if (isEditMode && skProfile != null) {
            newFirstname = skProfile.firstName ?: ""
            newLastname = skProfile.lastName ?: ""
            newEmail = skProfile.email ?: ""
            newPosition= skProfile.platform ?: ""
            newTerm= skProfile.term ?: ""
            newPlatform = skProfile.platform ?: ""
            newBirthdate = skProfile.birthdate ?: ""
            newGender = skProfile.gender ?: ""
            newAge = skProfile.age.toString()
            newPhoneNumber = skProfile.phoneNumber ?: ""
            newAddress = skProfile.address ?: ""
            newBase64Image = skProfile.skImage
        }
    }

    if (skProfile == null) {
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
                    ) {
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
                                        label = "First Name",
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    CustomOutlinedTextField(
                                        value = newLastname,
                                        onValueChange = { newLastname = it },
                                        label = "Last Name",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newEmail,
                                        onValueChange = { newEmail = it },
                                        label = "Email",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newPosition,
                                        onValueChange = { newPosition = it },
                                        label = "Position",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newTerm,
                                        onValueChange = { newTerm = it },
                                        label = "Term",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newPlatform,
                                        onValueChange = { newPlatform = it },
                                        label = "Platforms",
                                        maxLines = 5
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    SimpleDatePickerField(
                                        selectedDate = newBirthdate,
                                        onDateSelected = { newDate -> newBirthdate = newDate },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    RadioButtonGenders(
                                        gender = newGender,
                                        onGenderSelected = { newGender = it },
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newAge,
                                        onValueChange = { newAge = it },
                                        label = "Age",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newPhoneNumber,
                                        onValueChange = { newPhoneNumber = it },
                                        label = "Phone Number",
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newAddress,
                                        onValueChange = { newAddress = it },
                                        label = "Address",
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val memberId = skProfile.uid
                                            if(memberId != null){
                                                val updatedMemberProfile = SKProfile(
                                                    firstName = newFirstname,
                                                    lastName = newLastname,
                                                    position = newPosition,
                                                    email = newEmail,
                                                    platform = newPlatform,
                                                    term = newTerm,
                                                    birthdate = newBirthdate,
                                                    gender = newGender,
                                                    age = newAge.toInt(),
                                                    phoneNumber = newPhoneNumber,
                                                    address = newAddress,
                                                    skImage = newBase64Image
                                                )
                                                // Save updated project to database
                                                database.child("SKProfiles").child(memberId)
                                                    .setValue(updatedMemberProfile)
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
                                            val memberId = skProfile.uid
                                            if(memberId != null){
                                                database.child("SKProfiles").child(memberId)
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
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .padding(horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                contentPadding = PaddingValues(vertical = 6.dp)
                            ) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .weight(1f)
                                            .padding(horizontal = 6.dp)
                                            .padding(top = 20.dp, bottom = 20.dp)
                                            .background(Color.White, RoundedCornerShape(24.dp))
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(220.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE3F2FD)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (skProfile.skImage != null) {
                                                Base64Image(
                                                    base64String = skProfile.skImage,
                                                    modifier = Modifier
                                                        .fillMaxSize()
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

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            text = "${skProfile.firstName} ${skProfile.lastName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp,
                                            color = SKyberBlue
                                        )

                                        Text(
                                            text = "${skProfile.position}, ${skProfile.term}",
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 24.sp,
                                            color = Color.Gray
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                            Column(modifier = Modifier.padding(4.dp)) {
                                                Text(
                                                    "Platforms",
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 24.sp,
                                                    color = SKyberBlue
                                                )
                                                Text(
                                                    skProfile.platform ?: "",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )

                                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                                Text(
                                                    "Born ${skProfile.birthdate}, Age ${skProfile.age}",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = skProfile.email ?: "",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )
                                                Text(
                                                    text = skProfile.phoneNumber ?: "",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )
                                                Text(
                                                    text = skProfile.address ?: "",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )
                                                Text(
                                                    text = skProfile.gender ?: "",
                                                    fontSize = 18.sp,
                                                    color = Color.DarkGray
                                                )
                                            }


                                        Spacer(modifier = Modifier.height(24.dp))

                                        if(user != null){
                                            if (user.role == "ADMIN") {
                                                // Your untouched button
                                                Button(
                                                    onClick = { isEditMode = true },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(60.dp),
                                                    shape = RoundedCornerShape(28.dp),
                                                    contentPadding = PaddingValues(0.dp),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = Color.Transparent
                                                    )
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
