package com.example.skyber.ModularFunctions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.NewspaperFont
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardContainerGreen
import com.example.skyber.ui.theme.SoftCardContainerLavender
import com.example.skyber.ui.theme.SoftCardContainerMaroon
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.SoftCardFontGold
import com.example.skyber.ui.theme.SoftCardFontGreen
import com.example.skyber.ui.theme.SoftCardFontLavender
import com.example.skyber.ui.theme.White

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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.barangay,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = fontColor,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = announcement.postedAt,
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
fun CandidateCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    candidateProfile: CandidateProfile,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        shape = RoundedCornerShape(24.dp), // Reduced corner radius for a modern look
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp) // Adjusted padding for better alignment
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Consistent spacing between elements
        ) {
            Text(
                text = "${candidateProfile.firstName} ${candidateProfile.lastName}",
                fontSize = 24.sp, // Slightly smaller font for better balance
                fontWeight = FontWeight.Bold,
                color = fontColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${candidateProfile.partylist}",
                fontSize = 20.sp, // Consistent font size
                color = SoftCardFontGreen,
                fontWeight = FontWeight.Medium, // Adjusted weight for better readability
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
    val (statusColor, textColor) = when (project.status.lowercase()) {
        "ongoing", "active", "in-progress" -> SoftCardContainerGreen to SoftCardFontGreen
        "planning" , "upcoming"-> SKyberYellow to Black
        "finished" -> BoxGreen to BoxTextGreen
        else -> SoftCardContainerLavender to SoftCardFontLavender
    }

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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(statusColor)
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
fun VolunteerCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    volunteerPost: VolunteerPost,
    onClick: () -> Unit
) {
    val (statusColor, textColor) = when (volunteerPost.status.lowercase()) {
        "ongoing", "active" -> BoxGreen to BoxTextGreen
        "upcoming" -> SKyberYellow to Black
        else -> SoftCardContainerLavender to SoftCardFontLavender
    }

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
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = volunteerPost.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(22.dp))
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = volunteerPost.status,
                        fontSize = 16.sp,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${volunteerPost.contactPerson} • ${volunteerPost.contactEmail}",
                fontSize = 18.sp,
                color = fontColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${volunteerPost.location} • ${volunteerPost.eventDate}",
                fontSize = 18.sp,
                color = fontColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun JobListingCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    joblisting: JobListing,
    onClick: () -> Unit
) {
    val (statusColor, textColor) = when (joblisting.employementType.lowercase()) {
        "full-time" -> BoxGreen to BoxTextGreen
        "part-time" -> SKyberYellow to Black
        else -> SoftCardContainerLavender to SoftCardFontLavender
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = joblisting.companyName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = joblisting.employementType,
                        fontSize = 14.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Text(
                text = joblisting.jobTitle,
                fontSize = 20.sp,
                color = fontColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = joblisting.address,
                fontSize = 18.sp,
                color = fontColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}



@Composable
fun ScholarshipCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    scholarship: Scholarship,
    onClick: () -> Unit
) {
    val category = scholarship.type.uppercase()
    val (statusColor, textColor) = when (category) {
        "All" -> BoxGreen to BoxTextGreen
        "PRIVATE" -> SoftCardContainerMaroon to SoftCardFontGold
        "PUBLIC" -> SoftCardContainerBlue to SoftCardFontBlue
        else -> Color.LightGray to Color.DarkGray
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(24.dp), // Reduced corner radius for a modern look
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = scholarship.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .defaultMinSize(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = scholarship.type.uppercase(),
                        fontSize = 16.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}


@Composable
fun MemberCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    skProfile: SKProfile,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, SKyberDarkBlue),
        shape = RoundedCornerShape(24.dp), // Reduced corner radius for a modern look
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp) // Adjusted padding for better alignment
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Consistent spacing between elements
        ) {
            Text(
                text = "${skProfile.firstName} ${skProfile.lastName}",
                fontSize = 24.sp, // Slightly smaller font for better balance
                fontWeight = FontWeight.Bold,
                color = fontColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${skProfile.position}",
                fontSize = 20.sp, // Consistent font size
                color = SoftCardFontGreen,
                fontWeight = FontWeight.Medium, // Adjusted weight for better readability
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
