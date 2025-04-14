package com.example.skyber

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberRed
import com.example.skyber.ui.theme.SKyberYellow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SKyberDarkBlue)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
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

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = SKyberYellow,
            unfocusedIndicatorColor = SKyberYellow,
            focusedLabelColor = SKyberYellow,
            unfocusedLabelColor = SKyberYellow
        )
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Login Button
        Button(
            onClick = {
                navController.navigate(Screens.Home.screen)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SKyberRed,
                contentColor = Color.White
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(600.dp))
                .fillMaxWidth()
        ) {
            Text(
                text = "Login",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        //Signup Link
        TextButton(onClick = {
            navController.navigate(Screens.SignUp.screen)
        }) {
            Text("Don't have an account? Sign up", color = SKyberYellow )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}