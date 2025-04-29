package com.example.skyber.volunteerhubscreens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.ModularFunctions.DatePickerField
import com.example.skyber.ModularFunctions.convertMillisToDate
import com.example.skyber.Screens
import com.example.skyber.dataclass.User
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.portalnavigator.ProjectTransparency.showToast
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White
import com.google.firebase.database.DatabaseReference

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostVolunteerHub(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user
    //var eventId by remember {mutableStateOf("")}
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var eventdate by remember { mutableStateOf("") }
    var contactperson by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var requirements by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (user == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else {
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
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
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
                                value = contactperson,
                                onValueChange = { contactperson = it },
                                label = { Text("Contact Person") },
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
                                label = { Text("Event Description") },
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

                            DatePickerField(
                                label = "Start Date",
                                selectedDate = eventdate,
                                onDateSelected = { millis ->
                                    eventdate = convertMillisToDate(millis)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))


                            TextField(
                                value = category,
                                onValueChange = {  category = it },
                                label = { Text("Category") },
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
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = location,
                                onValueChange = { location = it },
                                label = { Text("Location") },
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
                                value = requirements,
                                onValueChange = { requirements = it },
                                label = { Text("Requirements") },
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
                            // Create Project Post object
                            if (title.isBlank() || contactperson.isBlank() || eventdate.isBlank() || location.isBlank()) {
                                showToast(context, "Please fill out required fields")
                            }else{
                                val databaseRef = FirebaseHelper.databaseReference.child("VolunteerHubEvent").push()
                                val postId = databaseRef.key
                                if(postId != null){
                                    val newVolunteerPost = VolunteerPost(
                                        id = postId,
                                        title = title,
                                        description = description,
                                        category = category,
                                        location = location,
                                        eventDate = eventdate,
                                        contactPerson = contactperson,
                                        contactEmail = email,
                                        status = "Ongoing",
                                        requirements = requirements
                                    )
                                    // Upload the project post and show the toast
                                    uploadVolunteerPost(databaseRef,newVolunteerPost, context)
                                    navController.navigate(Screens.VolunteerHub.screen)
                                }else{
                                    showToast(context, "Failed to create post")
                                }

                            }
                        }) {
                        Text("Post Event")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                } //end of main content column
            }//end of top level column
        }//end of scaffold
    }
}

fun uploadVolunteerPost(ref: DatabaseReference, volunteerPost: VolunteerPost, context: Context) {
    ref.setValue(volunteerPost).addOnSuccessListener {
        showToast(context, "Volunteer Event uploaded successfully")
    }.addOnFailureListener {
        showToast(context, "Failed to Post Volunteer Event")
    }
}