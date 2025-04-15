package com.example.skyber.navigationbar

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyber.R
import com.example.skyber.ui.theme.White
import com.example.skyber.ui.theme.SKyberDarkBlue


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(navController: NavHostController) {
    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .background(SKyberDarkBlue)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(100.dp)
                        .width(130.dp)
                        .padding(9.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.weight(1f))

                /*IconButton(
                    //onClick = { showModal = !showModal }
                ) {*/
                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    tint = White,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(35.dp)
                        .offset(x = (-7).dp, y = (-8).dp)
                )
                //}

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonPin,
                    tint = White,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(50.dp)
                )
                Row(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .height(40.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center

                ){
                    Text(text = "Hello, ", fontSize = 30.sp, color = White)
                    Text(text = "firstname", fontSize = 30.sp, color = White, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                        .height(40.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center

                ){
                    Icon(
                        imageVector = Icons.Filled.PinDrop,
                        tint = White,
                        contentDescription = "Location",
                        modifier = Modifier
                            .size(30.dp)

                    )
                    Text(text = " Barangay, ", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)
                    Text(text = "City name", fontSize = 25.sp, color = White, fontWeight = FontWeight.Bold)
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(horizontal = 2.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Announcements", fontSize = 22.sp, color = White, fontWeight = FontWeight.Bold)
                        Text(text = "See All", fontSize = 22.sp, color = White)
                    }

                    AnnouncementCard()

                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ){
                        Text(text = "Candidates", fontSize = 22.sp, color = White, fontWeight = FontWeight.Bold)
                    }
                    CandidateCard()
                }//end part for column for announcements and candidates

            }//Main content column
        }//end of main layout column
    }//End of scaffold
}



@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(navController = navController)
}

@Composable
fun AnnouncementCard(/*Retrieve announcement image and title */){
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .width(210.dp)
            ){
                Text(
                    text = "Lorem ipsum dolor sit amet",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

            }

            Image(
                painter = painterResource(id = R.drawable.image),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(150.dp)
                    .width(110.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun CandidateCard(/*Retrieve candidate profile image and first and last name */){
    Card(
        colors = CardDefaults.cardColors(containerColor = White),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.image),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(150.dp)
                    .width(110.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 38.dp, horizontal = 6.dp)
                    .width(210.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = "firstname, lastname",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Barangay",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

            }

        }
    }
}
//Repurpose for notifications modal
/*
@Composable
fun NotificationsIcon(modifier: Modifier = Modifier) {
    var showNotifications by remember { mutableStateOf(false) }
    var showModal by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("DOB") },
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }

}
*/

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
 */