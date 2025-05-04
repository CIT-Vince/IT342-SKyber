package com.example.skyber.portalnavigator.Scholarships

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.PlainText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardContainerMaroon
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.SoftCardFontGold
import com.example.skyber.ui.theme.gradientBrush

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScholarship(navController: NavHostController) {
    val scholarship =
        navController.previousBackStackEntry?.savedStateHandle?.get<Scholarship>("scholarship")
    var newScholarshipTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newLink by remember { mutableStateOf("") }
    var newContactEmail by remember { mutableStateOf("") }
    var newType by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current
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
        if (isEditMode && scholarship != null) {
            newScholarshipTitle = scholarship.title
            newDescription = scholarship.description
            newContactEmail = scholarship.contactEmail
            newLink = scholarship.link
            newType = scholarship.type
        }
    }


    if (scholarship == null) {  // Show a loading spinner while waiting for user data
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
        val category = scholarship.type.lowercase()
        val (statusColor, textColor) = when (category) {
            "all" -> BoxGreen to BoxTextGreen
            "private" -> SoftCardContainerMaroon to SoftCardFontGold
            "public" -> SoftCardContainerBlue to SoftCardFontBlue
            else -> Color.LightGray to Color.DarkGray
        }
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

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    //.padding(innerPadding),
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
                        if (isEditMode) {//Edit mode self explanatory
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                                    .padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                item {
                                    //Text Fields here
                                    CustomOutlinedTextField(
                                        value = newScholarshipTitle,
                                        onValueChange = { newScholarshipTitle = it },
                                        label = "Scholarship Title",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newDescription,
                                        onValueChange = { newDescription = it },
                                        label = "Description",
                                        maxLines = 5
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newLink,
                                        onValueChange = { newLink = it },
                                        label = "Apply Here",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newContactEmail,
                                        onValueChange = { newContactEmail = it },
                                        label = "Contact Email",
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    ScholarshipCategoryDropdown(
                                        selectedCategory = newType,
                                        onCategorySelected = { newType = it }
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val scholarshipId = scholarship.id
                                            // Build updated Scholarship
                                            val updatedScholarship = Scholarship(
                                                id = scholarshipId,
                                                title = newScholarshipTitle.ifEmpty { scholarship.title },
                                                description = newDescription.ifEmpty { scholarship.description },
                                                link = newLink.ifEmpty { scholarship.link },
                                                contactEmail = newContactEmail.ifEmpty { scholarship.contactEmail },
                                                type = newType.ifEmpty { scholarship.type }
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

                                    Spacer(modifier = Modifier.height(18.dp))

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
                                                text = scholarship.title,
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
                                                    text = scholarship.type,
                                                    fontSize = 20.sp,
                                                    color = textColor,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(12.dp))

                                            Text(
                                                text = "Contact Email: ${scholarship.contactEmail}",
                                                fontSize = 14.sp,
                                                color = SKyberBlue
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Apply at: ${scholarship.link}",
                                                color = SKyberBlue,
                                                fontSize = 14.sp,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.clickable {
                                                    clipboardManager.setText(AnnotatedString(scholarship.link))
                                                    Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                                                }
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))

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
                                                    text = scholarship.description,
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

                                            Spacer(modifier = Modifier.height(12.dp))

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
