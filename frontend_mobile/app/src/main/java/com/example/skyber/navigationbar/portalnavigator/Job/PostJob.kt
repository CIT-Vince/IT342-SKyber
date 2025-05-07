package com.example.skyber.navigationbar.portalnavigator.Job

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import com.example.skyber.ModularFunctions.headerbar.HeaderBar
import com.example.skyber.ModularFunctions.headerbar.NotificationHandler
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.User
import com.example.skyber.navigationbar.portalnavigator.ProjectTransparency.showToast
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun PostJob(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user
    var jobtitle by remember { mutableStateOf("") }
    var companyname by remember {mutableStateOf("")}
    var description by remember { mutableStateOf("") }
    var employmentType by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var applicationLink by remember { mutableStateOf("") }
    var base64Image by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

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

        if (user == null){// Show a loading spinner while waiting for user data
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SKyberYellow)
            }
            return
        }else{
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
                            item{
                                // Image Preview
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
                                        text = "Upload Image",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                //Text Fields here
                                CustomOutlinedTextField(
                                    value = jobtitle,
                                    onValueChange = { jobtitle = it },
                                    label = "Job Title",
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = companyname,
                                    onValueChange = { companyname = it },
                                    label = "Company Name",
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CategoryDropdown(
                                    selectedCategory = employmentType,
                                    onCategorySelected = { employmentType = it }
                                )


                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = applicationLink,
                                    onValueChange = { applicationLink = it },
                                    label = "Apply Here",
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = "Job Description",
                                    maxLines = 5,
                                    keyboardOptions = KeyboardOptions.Default.copy()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CustomOutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = "Address",
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                            }//End of lazy Column content
                        }//End of alzy column

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Create Job Listing  object
                                if(jobtitle.isBlank()  || companyname.isBlank() || address.isBlank() || applicationLink.isBlank()){
                                    showToast(context, "Please fill out required fields")
                                }else{
                                    val databaseRef = FirebaseHelper.databaseReference.child("JobListing").push()
                                    val postId = databaseRef.key
                                    if(postId != null){
                                        val newJobListing = JobListing(
                                            id = postId,
                                            jobTitle = jobtitle,
                                            companyName = companyname,
                                            description = description,
                                            applicationLink = applicationLink,
                                            address = address,
                                            employementType = employmentType,
                                            jobImage = base64Image
                                        )
                                        // Upload the Job listing post
                                        uploadJobListing(databaseRef, newJobListing, context)
                                        navController.navigate(Screens.Job.screen)
                                    }else{
                                        showToast(context, "Failed to create Job Listing")
                                    }

                                }
                            },
                            modifier = Modifier
                                .width(130.dp)
                                .height(60.dp),
                            shape = RoundedCornerShape(28.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(gradientBrush),
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = "Post",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                } //end of main content column
            }//end of top level column
        }//end of scaffold
    }
}

fun uploadJobListing(ref: DatabaseReference, jobListing: JobListing, context: Context) {
    ref.setValue(jobListing).addOnSuccessListener {
        showToast(context, "Job Listing uploaded successfully")
    }.addOnFailureListener {
        showToast(context, "Failed to Post Job Listing")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Full-Time", "Part-Time","Contract", "Internship", "Freelance")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(), // Ensures alignment with the text field
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF0066FF),
                unfocusedBorderColor = Color(0xFFD1D5DB)
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .exposedDropdownSize() // Ensures the dropdown matches the text field width
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = category,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = SKyberBlue
                        )
                    }
                )
            }
        }
    }
}