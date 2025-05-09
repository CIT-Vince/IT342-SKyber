package com.example.skyber.navigationbar.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.Base64Image
import com.example.skyber.ModularFunctions.CustomOutlinedTextField
import com.example.skyber.ModularFunctions.ImageUtils
import com.example.skyber.ModularFunctions.ParticleSystem
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.User
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlueGradient
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.gradientBrush
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsAnnouncement(navController: NavHostController, userProfile: MutableState<User>) {
    val user = userProfile.value
    val announcement = navController.previousBackStackEntry?.savedStateHandle?.get<Announcement>("announcement")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("") }
    var newContent by rememberSaveable { mutableStateOf("") }
    var newCategory by rememberSaveable { mutableStateOf("") }
    var newBarangay by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var newBase64Image by remember { mutableStateOf<String?>(null) }
    val currentDateTime = remember {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())
    }

    // Launch image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            newBase64Image = ImageUtils.bitmapToBase64(bitmap)
        }
    }

    LaunchedEffect(isEditMode) {
        if (isEditMode && announcement != null) {
            newTitle = announcement.title
            newContent = announcement.content
            newCategory = announcement.category
            newBarangay = announcement.barangay
        }
    }

    if (announcement == null) {
        // Show a loading spinner while waiting for announcement data
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
                    .background(Color(0xFFF5F5F5))
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Blue header bar similar to the web design
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color(0xFF1565C0))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { navController.popBackStack() }
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "Announcement Details",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        if (isEditMode) {
                            // Edit Mode
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(16.dp)
                                ) {
                                    // Image Preview
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(220.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (newBase64Image != null) {
                                            Base64Image(
                                                base64String = newBase64Image,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(8.dp))
                                            )
                                        } else if (!announcement.imageData.isNullOrEmpty()) {
                                            Base64Image(
                                                base64String = announcement.imageData,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(8.dp))
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
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
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
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Edit Fields
                                    CustomOutlinedTextField(
                                        value = newTitle,
                                        onValueChange = { newTitle = it },
                                        label = "Title"
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newBarangay,
                                        onValueChange = { newBarangay = it },
                                        label = "Barangay"
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newCategory,
                                        onValueChange = { newCategory = it },
                                        label = "Category"
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    CustomOutlinedTextField(
                                        value = newContent,
                                        onValueChange = { newContent = it },
                                        label = "Content",
                                        modifier = Modifier.height(200.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Action Buttons
                                    Button(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val announcementId = announcement.id

                                            val updatedAnnouncement = Announcement(
                                                id = announcementId,
                                                title = newTitle.ifEmpty { announcement.title },
                                                content = newContent.ifEmpty { announcement.content },
                                                postedAt = announcement.postedAt,
                                                barangay = newBarangay.ifEmpty { announcement.barangay },
                                                category = newCategory.ifEmpty { announcement.category },
                                                imageData = newBase64Image ?: announcement.imageData
                                            )
                                            database.child("Announcements").child(announcementId)
                                                .setValue(updatedAnnouncement)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        context,
                                                        "Announcement updated successfully",
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
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                                    ) {
                                        Text(
                                            text = "Save Changes",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Delete Button
                                    OutlinedButton(
                                        onClick = {
                                            val database = FirebaseHelper.databaseReference
                                            val announcementId = announcement.id
                                            database.child("Announcements").child(announcementId)
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
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = Color.White,
                                            contentColor = Color(0xFFD32F2F)
                                        ),
                                        border = BorderStroke(1.dp, Color(0xFFD32F2F))
                                    ) {
                                        Text(
                                            text = "Delete Announcement",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        } else {
                            // View Mode (similar to web design)
                            // Banner Image
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                ) {
                                    if (!announcement.imageData.isNullOrEmpty()) {
                                        Base64Image(
                                            base64String = announcement.imageData,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFF1565C0)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = announcement.category.uppercase(),
                                                color = Color.White,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            // Announcement Card
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Category Tag
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = Color(0xFFE3F2FD),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = announcement.category.uppercase(),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1565C0)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Title and Date Row
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = announcement.title,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF212121),
                                                modifier = Modifier.weight(1f)
                                            )

                                            Text(
                                                text = announcement.postedAt.split(" ")[0], // Just the date part
                                                fontSize = 12.sp,
                                                color = Color(0xFF757575)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Barangay
                                        Text(
                                            text = "Barangay ${announcement.barangay}",
                                            fontSize = 14.sp,
                                            color = Color(0xFF1565C0),
                                            fontWeight = FontWeight.Medium
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Content
                                        Text(
                                            text = announcement.content,
                                            fontSize = 16.sp,
                                            color = Color(0xFF424242),
                                            lineHeight = 24.sp
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Share Button
                                        OutlinedButton(
                                            onClick = {
                                                val sendIntent = Intent()
                                                sendIntent.action = Intent.ACTION_SEND
                                                sendIntent.putExtra(Intent.EXTRA_TEXT, "${announcement.title}\n\n${announcement.content}")
                                                sendIntent.type = "text/plain"
                                                val shareIntent = Intent.createChooser(sendIntent, "Share Announcement")
                                                context.startActivity(shareIntent)
                                            },
                                            modifier = Modifier.wrapContentWidth(),
                                            shape = RoundedCornerShape(4.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color(0xFF1565C0)
                                            ),
                                            border = BorderStroke(1.dp, Color(0xFF1565C0))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "Share",
                                                tint = Color(0xFF1565C0)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = "Share")
                                        }
                                    }
                                }
                            }

                            // Related Announcements Section
                            item {
                                val relatedAnnouncements = remember { mutableStateListOf<Announcement>() }
                                var isLoadingRelated by remember { mutableStateOf(true) }

                                LaunchedEffect(announcement) {
                                    isLoadingRelated = true
                                    FirebaseHelper.databaseReference.child("Announcements")
                                        .limitToLast(10) // Get more than needed to ensure we have enough after filtering
                                        .get()
                                        .addOnSuccessListener { snapshot ->
                                            relatedAnnouncements.clear()

                                            // Temporary list to hold and process announcements
                                            val tempList = mutableListOf<Announcement>()

                                            // Add all announcements except current one to temp list
                                            snapshot.children.forEach { child ->
                                                val relatedAnnouncement = child.getValue(Announcement::class.java)
                                                if (relatedAnnouncement != null && relatedAnnouncement.id != announcement.id) {
                                                    tempList.add(relatedAnnouncement)
                                                }
                                            }

                                            // Sort by posting date (newest first) and take only 2
                                            val sortedAnnouncements = tempList
                                                .sortedByDescending { it.postedAt }
                                                .take(2)

                                            // Add to the state list
                                            relatedAnnouncements.addAll(sortedAnnouncements)

                                            isLoadingRelated = false
                                        }
                                        .addOnFailureListener { exception ->
                                            // Log the error for debugging
                                            Log.e("DetailsAnnouncement", "Error fetching announcements: ${exception.message}")
                                            isLoadingRelated = false
                                        }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Other Announcements",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF212121)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (isLoadingRelated) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                color = SKyberBlue,
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    } else if (relatedAnnouncements.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "No other announcements available",
                                                fontSize = 14.sp,
                                                color = Color(0xFF757575)
                                            )
                                        }
                                    } else {
                                        // Display related announcements - limited to 2 most recent
                                        relatedAnnouncements.forEach { relatedAnnouncement ->
                                            RelatedAnnouncementItem(
                                                title = relatedAnnouncement.title,
                                                date = relatedAnnouncement.postedAt.split(" ")[0], // Just the date part
                                                category = relatedAnnouncement.category,
                                                announcementId = relatedAnnouncement.id,
                                                onClick = {
                                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                                        "announcement", relatedAnnouncement
                                                    )

                                                    // Refresh the current screen by popping and navigating back
                                                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                                                    if (currentRoute != null) {
                                                        navController.popBackStack()
                                                        navController.navigate(currentRoute)
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }

                            // Admin Update Button (if user is admin)
                            if (user != null && user.role == "ADMIN") {
                                item {
                                    Button(
                                        onClick = { isEditMode = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .padding(horizontal = 16.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Edit Announcement",
                                            fontSize = 16.sp,
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

@Composable
fun RelatedAnnouncementItem(
    title: String,
    date: String,
    category: String,
    announcementId: String? = null, // Add ID parameter for navigation
    onClick: () -> Unit // Add click handler
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }, // Make entire card clickable
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.take(1),
                    color = Color(0xFF1565C0),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = date,
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                }
            }
        }
    }
}
// Simplified preview that doesn't try to mock navigation components
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewDetailsAnnouncementStandalone() {
    // Create a simple UI preview that mimics the DetailsAnnouncement screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFF1565C0))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Announcement Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color(0xFF1565C0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NOTICE",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Announcement Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Category Tag
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFFE3F2FD),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "NOTICE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1565C0)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Title and Date
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Updated Title",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121),
                                    modifier = Modifier.weight(1f)
                                )

                                Text(
                                    text = "5/2/2025",
                                    fontSize = 12.sp,
                                    color = Color(0xFF757575)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Barangay
                            Text(
                                text = "Barangay Bulacao",
                                fontSize = 14.sp,
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Content
                            Text(
                                text = "Please be informed that there will be a scheduled power interruption in Barangay Bulacao on Monday, May 6, 2025, from 8:00 AM to 5:00 PM. This is due to maintenance work by Visayan Electric Company on the main distribution line serving the area. We advise all residents to plan accordingly and take necessary precautions during this period. We apologize for the inconvenience and thank you for your understanding.",
                                fontSize = 16.sp,
                                color = Color(0xFF424242),
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Share Button
                            OutlinedButton(
                                onClick = { /* Preview - no action */ },
                                modifier = Modifier.wrapContentWidth(),
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF1565C0)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF1565C0))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Color(0xFF1565C0)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "Share")
                            }
                        }
                    }
                }

                // Related Announcements
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Other Announcements",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        RelatedAnnouncementItem(
                            title = "Barangay Bulacao Clean-Up Drive",
                            date = "5/2/2025",
                            category = "EVENT",
                                    onClick = {}
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        RelatedAnnouncementItem(
                            title = "Liga ng mga Barangay Basketball Tournament",
                            date = "5/2/2025",
                            category = "EVENT",
                            onClick = {}
                        )
                    }
                }

                // Admin Button
                item {
                    Button(
                        onClick = { /* Preview - no action */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Edit Announcement",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}