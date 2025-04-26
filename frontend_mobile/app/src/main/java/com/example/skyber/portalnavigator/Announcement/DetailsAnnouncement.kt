package com.example.skyber.portalnavigator.Announcement

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Announcement
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsAnnouncement(navController: NavHostController){
    val announcement = navController.previousBackStackEntry?.savedStateHandle?.get<Announcement>("announcement")
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var newTitle by rememberSaveable { mutableStateOf("")}
    var newContent by rememberSaveable {mutableStateOf("")}
    var newCategory by rememberSaveable {mutableStateOf("")}
    var newBarangay by rememberSaveable {mutableStateOf("")}
    val context = LocalContext.current

    LaunchedEffect(isEditMode) {
        if (isEditMode && announcement != null) {
            newTitle = announcement.title
            newContent = announcement.content
            newCategory = announcement.category
            newBarangay = announcement.barangay
        }
    }


    if (announcement == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else{
        Scaffold() {  innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(SKyberDarkBlue)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderBar(
                    trailingContent = {
                        NotificationHandler()
                    }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(White),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Announcement", color = SKyberBlue, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxSize()
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if(isEditMode){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(14.dp)
                        ) {
                            item {
                                TextField(
                                    value = newTitle,
                                    onValueChange = { newTitle = it },
                                    label = { Text("Title") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                TextField(
                                    value = newBarangay,
                                    onValueChange = { newBarangay = it },
                                    label = { Text("Barangay") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                TextField(
                                    value = newCategory,
                                    onValueChange = { newCategory = it },
                                    label = { Text("Category") },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = newContent,
                                    onValueChange = { newContent = it },
                                    label = { Text("Content") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp), // Adjust height for multiline
                                    maxLines = 10
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val announcementId = announcement.id

                                        // Build updated announcement
                                        val updatedAnnouncement = Announcement(
                                            id = announcementId,
                                            title = newTitle.ifEmpty { announcement.title },
                                            content = newContent.ifEmpty { announcement.content },
                                            postedAt = announcement.postedAt, // keep original date
                                            barangay = newBarangay.ifEmpty { announcement.barangay },
                                            category = newCategory.ifEmpty { announcement.category }
                                        )
                                        database.child("Announcements").child(announcementId).setValue(updatedAnnouncement)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Announcement updated successfully", Toast.LENGTH_SHORT).show()
                                                isEditMode = false
                                            }
                                            .addOnFailureListener{
                                                Toast.makeText(context, "Update unsuccessful", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                ){
                                    Text("Save Changes")
                                }

                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val announcementId = announcement.id
                                        database.child("Announcements").child(announcementId).removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                                                isEditMode = false
                                            }
                                            .addOnFailureListener{
                                                Toast.makeText(context, "Deletion unsuccessful", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                ){
                                    Text("Delete")
                                }

                            }//end of items
                        }//end of lazy column

                    } else{
                        LazyColumn(){
                            item {
                                Text(
                                    text = announcement.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Posted on ${announcement.postedAt}",
                                    fontSize = 14.sp,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Announcement",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SKyberBlue
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = announcement.content,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp,
                                    color = SKyberBlue
                                )
                                Button(
                                    onClick = {
                                        isEditMode = true
                                    }
                                ) {
                                    Text("Edit")
                                }
                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val announcementId = announcement.id
                                        database.child("Announcements").child(announcementId).removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                                                isEditMode = false
                                            }
                                            .addOnFailureListener{
                                                Toast.makeText(context, "Deletion unsuccessful", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                ){
                                    Text("Delete")
                                }


                            }
                        }
                    }
                }
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    val navController = rememberNavController()
    DetailsAnnouncement(navController = navController)
}*/
