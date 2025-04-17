package com.example.skyber.userprofilescreens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.dataclass.User
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditProfile(navController: NavHostController, userProfile: MutableState<User?>, refreshUserProfile: () -> Unit) {
    val user = userProfile.value
    var newFirstname by remember { mutableStateOf("") }
    var newLastname by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
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
                    .fillMaxSize()
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


                Column(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.PersonPin,
                        tint = White,
                        contentDescription = "User Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top
                    ) {

                        // Display the current user name and double check if null kay mo crash
                        Text(
                            text = user.firstname ?: "User",
                            fontSize = 30.sp,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = user.lastname  ?: "User",
                            fontSize = 30.sp,
                            color = White,
                            fontWeight = FontWeight.Bold
                        )

                    }

                }

                //Main Content
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            White,
                            shape = RoundedCornerShape(
                                topStart = 60.dp,
                                topEnd = 60.dp,
                                bottomStart = 0.dp,
                                bottomEnd = 0.dp
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(20.dp)
                    ) {
                        Text(
                            "  EDIT PROFILE",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween

                        ) {
                            TextField(
                                value = newFirstname,
                                onValueChange = { newFirstname = it },
                                label = { Text("First Name") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .height(60.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberRed,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberRed
                                )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            TextField(
                                value = newLastname,
                                onValueChange = { newLastname = it },
                                label = { Text("Last Name") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .height(60.dp)
                                    .weight(1f)
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(Color.White),
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedIndicatorColor = SKyberYellow,
                                    unfocusedIndicatorColor = SKyberRed,
                                    focusedLabelColor = SKyberYellow,
                                    unfocusedLabelColor = SKyberRed
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        TextField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(40.dp))
                                .background(Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = SKyberYellow,
                                unfocusedIndicatorColor = SKyberRed,
                                focusedLabelColor = SKyberYellow,
                                unfocusedLabelColor = SKyberRed
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextField(
                            value = oldPassword,
                            onValueChange = { oldPassword = it },
                            label = { Text("Password") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(40.dp))
                                .background(Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = SKyberYellow,
                                unfocusedIndicatorColor = SKyberRed,
                                focusedLabelColor = SKyberYellow,
                                unfocusedLabelColor = SKyberRed,
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        TextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(40.dp))
                                .background(Color.White),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = SKyberYellow,
                                unfocusedIndicatorColor = SKyberRed,
                                focusedLabelColor = SKyberYellow,
                                unfocusedLabelColor = SKyberRed
                            )
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Button(
                            onClick = {
                                val auth = FirebaseHelper.auth//get auth from firebase singleton
                                val database = FirebaseHelper.databaseReference//Firebase Realtime db reference gikan sa singleton file
                                val uid = FirebaseHelper.auth.currentUser?.uid


                                if (uid != null) {
                                    database.child("users").child(uid).get().addOnSuccessListener {
                                        val currentUser = userProfile.value

                                        if (currentUser != null && currentUser.password == oldPassword) {
                                            val updatedUser = User(
                                                id = currentUser.id,
                                                firstname = newFirstname.ifEmpty { currentUser.firstname },
                                                lastname = newLastname.ifEmpty { currentUser.lastname },
                                                email = newEmail.ifEmpty { currentUser.email },
                                                password = newPassword.ifEmpty { currentUser.password },
                                                dob = currentUser.dob,
                                                gender = currentUser.gender,
                                                role = currentUser.role
                                            )
                                            FirebaseHelper.databaseReference.child("users")
                                                .child(currentUser.id!!).setValue(updatedUser)
                                                .addOnSuccessListener {
                                                    refreshUserProfile()
                                                    Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                                                }.addOnFailureListener {
                                                    Toast.makeText(
                                                        context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                                }

                                        } else {
                                            Toast.makeText(context, "Old Password Doesn't Match", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SKyberBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(600.dp))
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Confirm Changes",
                                color = Color.White
                            )
                        }

                    }//End of main content Column
                }//End of main content box


            }//end of scaffold
        }
    }
}//end of function

/*
@Preview(showBackground = true)
@Composable
fun PreviewEditProfile(){
    val navController = rememberNavController()
    EditProfile(navController = navController)
}
*/

