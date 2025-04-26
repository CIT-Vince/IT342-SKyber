package com.example.skyber.portalnavigator.Job

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.skyber.FirebaseHelper
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.JobListing
import com.example.skyber.headerbar.HeaderBar
import com.example.skyber.headerbar.NotificationHandler
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsJob(navController: NavHostController){
    val joblisting = navController.previousBackStackEntry?.savedStateHandle?.get<JobListing>("joblisting")
    var newJobtitle by remember { mutableStateOf("") }
    var newCompanyname by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("") }
    var newLocation by remember { mutableStateOf("") }
    var newAddress by remember { mutableStateOf("") }
    var newContactperson by remember { mutableStateOf("") }
    var newContact by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isEditMode by remember {mutableStateOf(false)}

    LaunchedEffect(isEditMode){
        if (isEditMode && joblisting != null) {
            newJobtitle = joblisting.jobtitle
            newCompanyname = joblisting.companyname
            newDescription = joblisting.description
            newCategory = joblisting.category
            newLocation = joblisting.location
            newAddress = joblisting.address
            newContactperson = joblisting.contactperson
            newContact = joblisting.contact
        }
    }

    if (joblisting == null) {
        // Show a loading spinner while waiting for user data
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = SKyberYellow)
        }
        return
    }else {
        val jobCategory = joblisting.category.lowercase() == "full-time"
        val statusColor = if (jobCategory) BoxGreen else SoftCardContainerBlue
        val textColor = if (jobCategory)  BoxTextGreen else SoftCardFontBlue
        Scaffold() { innerPadding ->
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
                ) {
                    Text(
                        text = "Job Listing",
                        color = SKyberBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(0.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                        .background(White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if(isEditMode){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(14.dp)
                        ) {
                            item{
                                //Text Fields here
                                TextField(
                                    value = newJobtitle,
                                    onValueChange = { newJobtitle = it },
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
                                    value = newCompanyname,
                                    onValueChange = { newCompanyname = it },
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
                                    selectedCategory = newCategory,
                                    onCategorySelected = { newCategory = it }
                                )


                                Spacer(modifier = Modifier.height(12.dp))

                                TextField(
                                    value = newContactperson,
                                    onValueChange = { newContactperson = it },
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
                                    value = newContact,
                                    onValueChange = { newContact = it },
                                    label = { Text("Contact Information") },
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
                                    value = newDescription,
                                    onValueChange = { newDescription = it },
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
                                    value = newLocation,
                                    onValueChange = { newLocation = it },
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
                                    value = newAddress,
                                    onValueChange = { newAddress = it },
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

                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val jobListingId = joblisting.id

                                        // Build updated joblisting
                                        val updatedJoblisting = JobListing(
                                            id = jobListingId,
                                            jobtitle = newJobtitle.ifEmpty { joblisting.jobtitle },
                                            companyname = newCompanyname.ifEmpty { joblisting.companyname },
                                            description = newDescription.ifEmpty { joblisting.description },
                                            contact = newContact.ifEmpty {joblisting.contact},
                                            category = newCategory.ifEmpty { joblisting.category},
                                            location = newLocation.ifEmpty { joblisting.location },
                                            contactperson = newContactperson.ifEmpty { joblisting.contactperson },
                                            address = newAddress.ifEmpty { joblisting.address }
                                        )
                                        database.child("JobListing").child(jobListingId).setValue(updatedJoblisting)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Job Listing updated successfully", Toast.LENGTH_SHORT).show()
                                                isEditMode = false
                                            }
                                            .addOnFailureListener{
                                                Toast.makeText(context, "Update unsuccessful", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                ){
                                    Text("Save Changes")
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        val database = FirebaseHelper.databaseReference
                                        val jobListingId = joblisting.id
                                        database.child("JobListing").child(jobListingId).removeValue()
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

                            }//End of lazy Column content
                        }//End of alzy column
                    }else{
                        Row(
                            modifier = Modifier.height(30.dp),
                            verticalAlignment = Alignment.Bottom
                        ){
                            Text(
                                text = joblisting.jobtitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = SKyberBlue
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(statusColor)
                                    .padding(horizontal = 8.dp)
                                    .wrapContentWidth()

                            ){
                                Text(
                                    text = joblisting.category,
                                    fontSize = 18.sp,
                                    color = textColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Company Name: ${joblisting.companyname}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Location: ${joblisting.location}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Text(
                            text = "Location: ${joblisting.address}",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )


                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Job Description",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = SKyberBlue
                        )
                        Text(
                            text = joblisting.description,
                            fontSize = 14.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Contact Information",
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = joblisting.contactperson,
                            fontSize = 16.sp,
                            color = SKyberBlue
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = joblisting.contact,
                            fontSize = 16.sp,
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
                                val joblistingId = joblisting.id
                                database.child("JobListing").child(joblistingId).removeValue()
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