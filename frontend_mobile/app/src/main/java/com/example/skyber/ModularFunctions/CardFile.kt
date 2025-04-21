package com.example.skyber.ModularFunctions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.skyber.R
import com.example.skyber.dataclass.*
import com.example.skyber.ui.theme.*

@Composable
fun AnnouncementCard(backgroundColor: Color = White,
                     fontColor: Color = Black,
                     announcement: Announcement,
                     onClick: () -> Unit){
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, NewspaperFont),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .clickable { onClick() }
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
    ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                //verticalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = announcement.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${announcement.author}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = fontColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = announcement.datePosted,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = fontColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = announcement.category,
                    fontSize = 20.sp,
                    color = fontColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
        }
    }
}

@Composable
fun CandidateCard(backgroundColor: Color = White,
                  fontColor: Color = Black,
                  candidateProfile: CandidateProfile,
                  onClick: () -> Unit){

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .clickable { onClick() }
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(17.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            //verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "${candidateProfile.firstname} ${candidateProfile.lastname},",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(BoxOrange)
                        .padding(horizontal = 8.dp)
                        .wrapContentWidth()

                ) {
                    /*Text(
                        text = "${candidateProfile.status}",
                        fontSize = 22.sp,
                        color = BoxTextOrange,
                        fontWeight = FontWeight.SemiBold
                    )*/
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${candidateProfile.partylist}",
                fontSize = 24.sp,
                color = SoftCardFontGreen,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${candidateProfile.address}",
                fontSize = 22.sp,
                color = SoftCardFontGreen,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@Composable
fun ListCard(title: String, icon: ImageVector, onCardClick: () -> Unit ){
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onCardClick() }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp)
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SKyberBlue,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(90.dp)
                    .padding(horizontal = 4.dp)
            )
            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = SKyberBlue,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ProjectTransparencyCard(backgroundColor: Color = White,
                            fontColor: Color = Black,
                            project: Project,
                            onClick: () -> Unit){
    val isOngoing = project.status.lowercase() == "ongoing"
    val StatusColor = if (isOngoing) BoxOrange else BoxGreen
    val textColor = if (isOngoing) BoxTextOrange else BoxTextGreen

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .clickable { onClick() }
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
    ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(220.dp)
                    .fillMaxWidth(),
                //verticalArrangement = Arrangement.SpaceBetween,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.projectName,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = fontColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.width(26.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(StatusColor)
                            .padding(horizontal = 8.dp)
                            .wrapContentWidth()

                    ){
                        Text(
                            text = project.status,
                            fontSize = 22.sp,
                            color = textColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.projectManager,
                        fontSize = 20.sp,
                        color = fontColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(26.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(BoxGreen)
                            .padding(horizontal = 8.dp)
                            .wrapContentWidth()

                    ){
                        Text(
                            text = "₱${project.budget}",
                            fontSize = 20.sp,
                            color = BoxTextGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${project.startDate} ",
                        fontSize = 18.sp,
                        color = fontColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Text(
                        text = " ${project.endDate}",
                        fontSize = 18.sp,
                        color = fontColor,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

    }
}

@Composable
fun VolunteerCard(backgroundColor: Color = White,
                     fontColor: Color = Black,
                     volunteerPost: VolunteerPost,
                     onClick: () -> Unit){

    val isOngoing = volunteerPost.status.lowercase() == "ongoing"
    val StatusColor = if (isOngoing) BoxGreen else BoxRed
    val textColor = if (isOngoing) BoxTextGreen else SKyberRed

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(38.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        modifier = Modifier
            .clickable { onClick() }
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
    ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()//width(210.dp),
                //verticalArrangement = Arrangement.SpaceBetween,
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        //modifier = Modifier.fillMaxWidth(),
                        text = volunteerPost.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = fontColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(26.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(StatusColor)
                            .padding(horizontal = 8.dp)
                            .wrapContentWidth()

                    ){
                        Text(
                            text = volunteerPost.status,
                            fontSize = 18.sp,
                            color = textColor,
                            fontWeight = FontWeight.SemiBold,
                            overflow = TextOverflow.Ellipsis

                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${volunteerPost.contactperson} • ${volunteerPost.contact}",
                    fontSize = 20.sp,
                    color = fontColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "${volunteerPost.location} • ${volunteerPost.eventdate}",
                    fontSize = 20.sp,
                    color = fontColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
     CandidateCard(
        backgroundColor = SoftCardContainerGreen,
        fontColor = SoftCardFontGreen,
        candidateProfile = CandidateProfile(
            firstname = "Isagi",
            lastname = "Yoichi",
            partylist = "Heads Up",
            age = "19",
            //status = "Coming",
            address = "Cebu City"
        ),
        onClick = {} // Simple empty lambda for preview
    )
}

/*
@Preview(showBackground = true)
@Composable
fun Preview() {
    ProjectTransparencyCard(
        backgroundColor = SoftCardContainerBrown,
        fontColor = SoftCardFontBrown,
        project = Project(
            projectName = "Road Repair",
            status = "Ongoing",
            projectManager = "Jv Bayona",
            budget = "8000",
            startDate = "21/04/2025",
            endDate = "29/04/2025"
        ),
        onClick = {} // Simple empty lambda for preview
    )
}


@Preview(showBackground = true)
@Composable
fun VolunteerCardPreview() {
    VolunteerCard(
        backgroundColor = SoftCardContainer,
        fontColor = SoftCardFont,
        volunteerPost = VolunteerPost(
            title = "Tree Planting",
            contactperson = "Ethan",
            contact = "0921938",
            status = "Ongoing",
            location = "Cebu",
            eventdate = "04/21/2025"
        ),
        onClick = {} // Simple empty lambda for preview
    )
}
*/
