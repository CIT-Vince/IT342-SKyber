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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row

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
            newPosition = skProfile.position ?: ""
            newTerm = skProfile.term ?: ""
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
                    .background(Color(0xFFF5F5F5))  // Light background for better contrast
            ) {
                // Main content layout
                Column(
                    modifier = Modifier.fillMaxSize(),
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
                                text = "SK Member Profile",
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
                                        text = "Upload Member Image",
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

                                Spacer(modifier = Modifier.height(12.dp))

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
                                                uid = memberId,
                                                firstName = newFirstname,
                                                lastName = newLastname,
                                                position = newPosition,
                                                email = newEmail,
                                                platform = newPlatform,
                                                term = newTerm,
                                                birthdate = newBirthdate,
                                                gender = newGender,
                                                age = newAge.toIntOrNull() ?: 0,
                                                phoneNumber = newPhoneNumber,
                                                address = newAddress,
                                                skImage = newBase64Image
                                            )
                                            // Save updated profile to database
                                            database.child("SKProfiles").child(memberId)
                                                .setValue(updatedMemberProfile)
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
                                        text = "Delete Member",
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
                                                .background(Color(0xFFE3F2FD)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (!skProfile.skImage.isNullOrEmpty()) {
                                                Base64Image(
                                                    base64String = skProfile.skImage,
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

                                        // Member Name - Larger font like in web UI
                                        Text(
                                            text = "${skProfile.firstName} ${skProfile.lastName}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Position badge
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color(0xFFFFD700))
                                                .padding(horizontal = 16.dp, vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = skProfile.position?.uppercase() ?: "",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Contact information
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "📧 ",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = skProfile.email ?: "",
                                                    fontSize = 16.sp,
                                                    color = Color.DarkGray
                                                )
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "📱 ",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = skProfile.phoneNumber ?: "",
                                                    fontSize = 16.sp,
                                                    color = Color.DarkGray
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                verticalAlignment = Alignment.Top,
                                                modifier = Modifier.padding(vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "📍 ",
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = skProfile.address ?: "",
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
                                        val platformText = skProfile.platform ?: ""
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

                                // Personal info section
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .background(Color.White, RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "Personal Information",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            color = SKyberBlue
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Age: ",
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = skProfile.age.toString(),
                                                color = Color.DarkGray
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Gender: ",
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = skProfile.gender ?: "",
                                                color = Color.DarkGray
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Birthday: ",
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Gray
                                            )
                                            Text(
                                                text = skProfile.birthdate ?: "",
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
                                            text = "Update Member Information",
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
fun DetailsSKmembersPreview() {
    // Create mock data for preview
    val mockSkProfile = SKProfile(
        uid = "preview-id",
        firstName = "Vince Kimlo",
        lastName = "Tan",
        position = "Chairman",
        email = "vincekimlo.tan@cit.edu",
        platform = "**Education Assistance** - Provide financial aid and scholarships for deserving students.\n" +
                "- Launch a community-based tutoring program for elementary and high school students.",
        term = "2023-2026",
        birthdate = "January 15, 2000",
        gender = "Male",
        age = 24,
        phoneNumber = "09665566551",
        address = "BLK 4 LOT 17 CHIVAS CABANCALAN I# BULACAO CEBU CITY",
        skImage = null
    )

    // Simplified UI for preview
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(SKyberBlue),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "← Back",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "SK Member Profile",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            // Profile Image placeholder
                            Box(
                                modifier = Modifier
                                    .size(180.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE3F2FD)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "VT",
                                    fontSize = 70.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SKyberBlue
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Member Name
                            Text(
                                text = "${mockSkProfile.firstName} ${mockSkProfile.lastName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Position badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFFD700))
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = mockSkProfile.position?.uppercase() ?: "",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Contact information
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📧 ",
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = mockSkProfile.email ?: "",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📱 ",
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = mockSkProfile.phoneNumber ?: "",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "📍 ",
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = mockSkProfile.address ?: "",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }

                    // Platform section - simplified for preview
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
                                    text = "Education Assistance - Provide financial aid and scholarships",
                                    fontSize = 16.sp,
                                    color = Color.DarkGray
                                )
                            }

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
                                    text = "Launch a community-based tutoring program",
                                    fontSize = 16.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}