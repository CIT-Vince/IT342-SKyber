package com.example.skyber.portalnavigator.Scholarships

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.Screens
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.dataclass.User
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
fun PostScholarship(navController: NavHostController, userProfile: MutableState<User?>){
    val user = userProfile.value//passed logged in user

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contactemail by remember { mutableStateOf("") }
    var applicationlink by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

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

                            ScholarshipCategoryDropdown(
                                selectedCategory = category,
                                onCategorySelected = { category = it }
                            )


                            Spacer(modifier = Modifier.height(12.dp))

                            TextField(
                                value = contactemail,
                                onValueChange = { contactemail = it },
                                label = { Text("Contact Email") },
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
                                value = applicationlink,
                                onValueChange = { applicationlink = it },
                                label = { Text("Apply here") },
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
                                label = { Text("Description") },
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
                            if(title.isBlank() || contactemail.isBlank() || applicationlink.isBlank()  || category.isBlank()){
                                showToast(context, "Please fill out required fields")
                            }else{
                                val databaseRef = FirebaseHelper.databaseReference.child("Scholarship").push()
                                val postId = databaseRef.key
                                if(postId != null){
                                    val newScholarship = Scholarship(
                                        title = title,
                                        description = description,
                                        contactEmail = contactemail,
                                        link = applicationlink,
                                        type = category,
                                    )
                                    // Upload the Job listing post
                                    uploadJScholarship(databaseRef, newScholarship, context)
                                    navController.navigate(Screens.Scholarship.screen)
                                }else{
                                    showToast(context, "Failed to create Scholarship")
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

fun uploadJScholarship(ref: DatabaseReference, scholarship: Scholarship, context: Context) {
    ref.setValue(scholarship).addOnSuccessListener {
        showToast(context, "Scholarship uploaded successfully")
    }.addOnFailureListener {
        showToast(context, "Failed to Post Scholarship")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScholarshipCategoryDropdown(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("Private", "Public", "All")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.exposedDropdownSize(matchTextFieldWidth = true),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 80.dp), //Caveman method for matching dropdown items to the textfield on top
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}