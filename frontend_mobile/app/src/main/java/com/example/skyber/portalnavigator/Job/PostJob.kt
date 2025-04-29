package com.example.skyber.portalnavigator.Job

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.Screens
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.ProjectTransparency.showToast
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.example.skyber.volunteerhubscreens.uploadVolunteerPost
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostJob(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user
    //var eventId by remember {mutableStateOf("")}
    var jobtitle by remember { mutableStateOf("") }
    var companyname by remember {mutableStateOf("")}
    var description by remember { mutableStateOf("") }
    var employmentType by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var applicationLink by remember { mutableStateOf("") }

    val context = LocalContext.current

        if (user == null){
            // Show a loading spinner while waiting for user data
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SKyberYellow)
            }
            return
        }else{
            Scaffold(){ innerPadding->
                Column(
                    modifier = Modifier
                        .background(SKyberDarkBlue)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    HeaderBar(
                        trailingContent = {
                            NotificationHandler()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(0.dp)
                            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                            .background(White),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(14.dp)
                        ) {
                            item{
                                //Text Fields here
                                TextField(
                                    value = jobtitle,
                                    onValueChange = { jobtitle = it },
                                    label = { Text("Job Title") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = companyname,
                                    onValueChange = { companyname = it },
                                    label = { Text("Company Name") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(20.dp)),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                CategoryDropdown(
                                    selectedCategory = employmentType,
                                    onCategorySelected = { employmentType = it }
                                )


                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = applicationLink,
                                    onValueChange = { applicationLink = it },
                                    label = { Text("Apply Here") },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text("Job Description") },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .padding(0.dp),
                                    maxLines = 10
                                    ,
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = { Text("Address") },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                            }//End of lazy Column content
                        }//End of alzy column

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SKyberBlue,
                                contentColor = Color.White
                            ),
                            onClick = {
                                // Create Job Listing  object
                                if(jobtitle.isBlank()  || companyname.isBlank() || address.isBlank() || applicationLink.isBlank()){
                                    showToast(context, "Please fill out required fields")
                                }else{
                                    val databaseRef = FirebaseHelper.databaseReference.child("JobListing").push()
                                    val postId = databaseRef.key
                                    if(postId != null){
                                        val newJobListing = JobListing(
                                            jobTitle = jobtitle,
                                            companyName = companyname,
                                            description = description,
                                            //contact = contact,
                                            //location = location,
                                            applicationLink = applicationLink,
                                            address = address,
                                            employmentType = employmentType,
                                        )
                                        // Upload the Job listing post
                                        uploadJobListing(databaseRef, newJobListing, context)
                                        navController.navigate(Screens.Job.screen)
                                    }else{
                                        showToast(context, "Failed to create Job Listing")
                                    }

                                }
                            }) {
                            Text("Post Job Listing")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
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
    val categories = listOf("Full-Time", "Part-Time")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedCategory,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = SKyberYellow,
                unfocusedIndicatorColor = SKyberYellow,
                focusedLabelColor = SKyberYellow,
                unfocusedLabelColor = SKyberYellow
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}
