package com.example.skyber.skprofilescreens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.skyber.Screens
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.User
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
fun PostSKcandidates(navController: NavHostController, userProfile: MutableState<User?>) {
    val user = userProfile.value//passed logged in user

    var platform by remember { mutableStateOf("") }
    var firstname by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var partylist by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
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
    } else {
        Scaffold() { innerPadding ->
            Column(
                modifier = Modifier
                    .background(SKyberDarkBlue)
                    .fillMaxSize(),
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
                        .fillMaxHeight()
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            //Text Fields here
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                TextField(
                                    value = firstname,
                                    onValueChange = { firstname = it },
                                    label = { Text("First Name") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(40.dp))
                                        .background(Color.White),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                TextField(
                                    value = lastname,
                                    onValueChange = { lastname = it },
                                    label = { Text("Last Name") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(40.dp))
                                        .background(Color.White),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = age,
                                onValueChange = { age = it },
                                label = { Text("Age") },
                                modifier = Modifier
                                    //.weight(1f)
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            /*Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                TextField(
                                    value = status,
                                    onValueChange = { status = it },
                                    label = { Text("Status") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(40.dp))
                                        .background(Color.White),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = SKyberYellow,
                                        unfocusedIndicatorColor = SKyberYellow,
                                        focusedLabelColor = SKyberYellow,
                                        unfocusedLabelColor = SKyberYellow
                                    )
                                )*/

                                //Spacer(modifier = Modifier.width(8.dp))

                            //}


                            TextField(
                                value = platform,
                                onValueChange = { platform = it },
                                label = { Text("Platforms") },
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

                            /*Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(20.dp)),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberYellow,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberYellow
                                )
                            )*/

                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = partylist,
                                onValueChange = { partylist = it },
                                label = { Text("Party List") },
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
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Address") },
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

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SKyberBlue,
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    // Create SK candidate  object
                                    val ageNumber = age.toIntOrNull()
                                    if  (firstname.isBlank() || lastname.isBlank() || address.isBlank() || partylist.isBlank() || email.isBlank()){
                                        showToast(context, "Please fill out required fields")
                                    }else if (ageNumber == null || ageNumber <= 0)  {
                                        showToast(context, "Please enter a valid age")
                                    } else {
                                        val databaseRef = FirebaseHelper.databaseReference.child("CandidateProfile").push()
                                        val candidateId = databaseRef.key
                                        val newCandidateProfile = CandidateProfile(
                                            id = candidateId,
                                            firstName = firstname,
                                            lastName = lastname,
                                            //email = email,
                                            age = age,
                                            partyList = partylist,
                                            platform = platform,
                                            address = address,
                                            //status = status
                                        )

                                        uploadCandidateProfile(newCandidateProfile, context, navController, databaseRef)
                                    }
                                }) {
                                Text("Post Candidate Profile")
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                        }//End of lazy Column content
                    }//End of alzy column
                }
            }
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun uploadCandidateProfile(candidateProfile: CandidateProfile, context: Context, navController: NavHostController, databaseRef: DatabaseReference) {
    databaseRef.setValue(candidateProfile)
            .addOnSuccessListener {
                showToast(context, "Candidate Profile uploaded successfully")// Show success toast when announcement is uploaded successfully
                navController.navigate(Screens.SKcandidates.screen)
                }
            .addOnFailureListener { error ->
                showToast(context, "Failed to Upload Candidate Profile")    // Show failure toast when something goes wrong
    }
}

