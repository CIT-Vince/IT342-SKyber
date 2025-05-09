package com.example.skyber.ModularFunctions

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.skyber.dataclass.Announcement
import com.example.skyber.dataclass.CandidateProfile
import com.example.skyber.dataclass.JobListing
import com.example.skyber.dataclass.Project
import com.example.skyber.dataclass.SKProfile
import com.example.skyber.dataclass.Scholarship
import com.example.skyber.dataclass.VolunteerPost
import com.example.skyber.ui.theme.BoxGreen
import com.example.skyber.ui.theme.BoxTextGreen
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.SKyberDarkBlue
import com.example.skyber.ui.theme.SKyberYellow
import com.example.skyber.ui.theme.SoftCardContainerBlue
import com.example.skyber.ui.theme.SoftCardContainerGreen
import com.example.skyber.ui.theme.SoftCardContainerLavender
import com.example.skyber.ui.theme.SoftCardContainerMaroon
import com.example.skyber.ui.theme.SoftCardFontBlue
import com.example.skyber.ui.theme.SoftCardFontGold
import com.example.skyber.ui.theme.SoftCardFontLavender
import com.example.skyber.ui.theme.White


@Composable
fun AnnouncementCard(
    backgroundColor: Color = SoftCardContainerBlue,
    fontColor: Color = SoftCardFontBlue,
    announcement: Announcement,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Image Section
                announcement.imageData?.let { imageData ->
                    if (imageData.startsWith("http")) {
                        // If imageData is a URL, load it using Coil
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imageData),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        // If imageData is Base64, decode and display it
                        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
                        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                        bitmap?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            ) {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }

                // Content Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Title
                    Text(
                        text = announcement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = fontColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Barangay
                    Text(
                        text = announcement.barangay,
                        fontSize = 14.sp,
                        color = fontColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Posted Date
                    Text(
                        text = announcement.postedAt,
                        fontSize = 12.sp,
                        color = fontColor
                    )
                }
            }

            // Badge Section (Slightly Inset from Top-Right Corner)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp) // Inset the badge to avoid overlapping the rounded corner
                    .background(Color(0xFF0D47A1), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = announcement.category.uppercase(),
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CandidateCard(
    candidateProfile: CandidateProfile,
    onClick: () -> Unit,
    backgroundColor: Color = Color.White,
    fontColor: Color = Color(0xFF1565C0) // Blue color similar to web version
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image (circular)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (candidateProfile.candidateImage.isNullOrEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Base64Image(
                        base64String = candidateProfile.candidateImage,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content Column
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Name and Age Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Name in blue
                    Text(
                        text = "${candidateProfile.firstName} ${candidateProfile.lastName}",
                        color = fontColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Age with bullet separator
                    Text(
                        text = "Age: ${candidateProfile.age ?: "N/A"}  •",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Platform tag and View Platform button row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Platform Tag/Badge
                    val platform = candidateProfile.platform.orEmpty().ifEmpty { "SK" }
                    val tagColor = when {
                        platform.equals("SMILE", ignoreCase = true) -> Color(0xFF5B9BD5) // Light blue
                        platform.equals("ONCE", ignoreCase = true) -> Color(0xFF7B91CC) // Blue-purple
                        platform.equals("SADBOY", ignoreCase = true) -> Color(0xFF8EA9DB) // Light purple
                        else -> Color(0xFF64B5F6) // Default blue
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = tagColor,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = candidateProfile.partylist.orEmpty(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // View Platform Button
                    TextButton(
                        onClick = onClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Black // Light blue
                        )
                    ) {
                        Text(
                            text = "View Platform",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }


            }
        }
    }
}

// Sample preview data provider for CandidateProfile
class SampleCandidateProvider : PreviewParameterProvider<CandidateProfile> {
    override val values = sequenceOf(
        CandidateProfile(
            id = "preview",
            firstName = "Jaevie",
            lastName = "Bayona",
            age = "22",
            partylist = "Team Progress",
            platform = "SMILE",
            address = "Cebu City"
        ),
        CandidateProfile(
            id = "preview",
            firstName = "Ethan",
            lastName = "Enriquez",
            age = "22",
            partylist = "Team Unity",
            platform = "ONCE",
            address = "Cebu City"
        ),
        CandidateProfile(
            id = "preview",
            firstName = "Jefferson",
            lastName = "Pada",
            age = "22",
            partylist = "Team Future",
            platform = "SADBOY",
            address = "Cebu City"
        ),
        CandidateProfile(
            id = "preview",
            firstName = "Maria Christina with a Very Long Name",
            lastName = "Dela Cruz",
            age = "21",
            partylist = "Team Youth",
            platform = "SK",
            address = "Mandaue City"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CandidateCardPreview(
    @PreviewParameter(SampleCandidateProvider::class) candidateProfile: CandidateProfile
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Current Date and Time (UTC): 2025-05-07 20:49:44",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Current User's Login: CIT-Vince",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CandidateCard(
            candidateProfile = candidateProfile,
            onClick = {},
            backgroundColor = Color.White,
            fontColor = Color(0xFF1565C0)
        )
    }
}

@Composable
fun CandidateCardsGroupPreview() {
    val candidates = listOf(
        CandidateProfile(
            id = "preview1",
            firstName = "Jaevie",
            lastName = "Bayona",
            age = "22",
            platform = "SMILE"
        ),
        CandidateProfile(
            id = "preview2",
            firstName = "Ethan",
            lastName = "Enriquez",
            age = "22",
            platform = "ONCE"
        ),
        CandidateProfile(
            id = "preview3",
            firstName = "Jefferson",
            lastName = "Pada",
            age = "22",
            platform = "SADBOY"
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Sangguniang Kabataan Candidates",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1565C0),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        candidates.forEach { candidate ->
            CandidateCard(
                candidateProfile = candidate,
                onClick = {},
                backgroundColor = Color.White,
                fontColor = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(8.dp))
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
fun ProjectTransparencyCard(
    backgroundColor: Color = White,
    fontColor: Color = Black,
    project: Project,
    onClick: () -> Unit
) {
    val (statusColor, textColor) = when (project.status.lowercase()) {
        "complete", "completed", "finished" -> SoftCardContainerGreen to White
        "ongoing", "active", "in-progress" -> SoftCardContainerBlue to White
        "planning", "upcoming" -> SKyberYellow to Black
        else -> SoftCardContainerLavender to SoftCardFontLavender
    }

    // Calculate project progress percentage
    val progressPercentage = when (project.status.lowercase()) {
        "complete", "completed", "finished" -> 1f
        else -> 0.5f // Default to 50% for other statuses
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Status badge at the top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1)),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
                    .padding(12.dp)
            ) {
                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.CenterStart)
                ) {
                    Text(
                        text = project.status.uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            // Project Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Project Title - FIX: Strict overflow handling with ellipsis
                Text(
                    text = project.projectName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 1, // Limit to 1 line to prevent overlap
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date Range Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Range",
                        tint = SKyberBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${project.startDate} - ${project.endDate}",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Budget information
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE3F2FD))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "BUDGET: ₱${project.budget}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = SKyberBlue
                        )
                    }
                }

                if (project.projectManager.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Project Manager: ${project.projectManager}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Project Progress Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Project Progress",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = fontColor
                    )
                    Text(
                        text = "${(progressPercentage * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = SKyberBlue
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progressPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SKyberBlue,
                    trackColor = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // View Details - align to right
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Text(
                            text = "View Details",
                            fontSize = 14.sp,
                            color = SKyberBlue,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "View Details",
                            tint = SKyberBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
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
        "ongoing", "active" -> BoxGreen to White
        "upcoming" -> Color(0xFF757575) to White // Gray for upcoming
        else -> SoftCardContainerLavender to SoftCardFontLavender
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp), // Reduced corner radius to match image
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header with category and status badges on colored background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1)),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                // Category Badge - Top Left
                if (volunteerPost.category.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(SKyberBlue)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = volunteerPost.category.uppercase(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Status Badge - Top Right
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = volunteerPost.status.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            // Content section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Date and Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Date with icon
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = volunteerPost.eventDate,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Location with icon
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = volunteerPost.location,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Activity Title
                Text(
                    text = volunteerPost.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Description
                if (volunteerPost.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = volunteerPost.description,
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Requirements Section (replaces slots section from the image)
                if (volunteerPost.requirements.isNotEmpty()) {
                    Text(
                        text = "Requirements:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = fontColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = volunteerPost.requirements,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Contact Person and View Details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Contact Person
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Contact",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = volunteerPost.contactPerson,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // View Details
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onClick() }
                    ) {
                        Text(
                            text = "View Details",
                            fontSize = 14.sp,
                            color = SKyberBlue,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "View Details",
                            tint = SKyberBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Register link button if available
//                if (volunteerPost.registerLink.isNotEmpty()) {
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    Button(
//                        onClick = { /* Handle register link click */ },
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = SKyberBlue
//                        ),
//                        contentPadding = PaddingValues(vertical = 12.dp)
//                    ) {
//                        Text(
//                            text = "Register Now",
//                            fontSize = 14.sp,
//                            fontWeight = FontWeight.Medium,
//                            color = Color.White
//                        )
//                    }
//                }
            }
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
        "ALL" -> BoxGreen to BoxTextGreen
        "PRIVATE" -> SoftCardContainerMaroon to SoftCardFontGold
        "PUBLIC" -> SoftCardContainerBlue to SoftCardFontBlue
        else -> Color.LightGray to Color.DarkGray
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
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
            // Title and Type Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = scholarship.title,
                    fontSize = 18.sp,
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
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = scholarship.type.uppercase(),
                        fontSize = 12.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Description snippet
            if (scholarship.description.isNotEmpty()) {
                Text(
                    text = scholarship.description.take(100) + if (scholarship.description.length > 100) "..." else "",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Contact email
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Contact",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = scholarship.contactEmail,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Image preview (if available)
            scholarship.scholarImage?.let { imageData ->
                if (imageData.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Base64Image(
                            base64String = imageData,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
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
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
        shape = RoundedCornerShape(16.dp),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD))
                    .border(2.dp, SKyberBlue.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (!skProfile.skImage.isNullOrEmpty()) {
                    Base64Image(
                        base64String = skProfile.skImage,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    // Display initials if no image
                    val initials = "${skProfile.firstName?.firstOrNull() ?: ""}${skProfile.lastName?.firstOrNull() ?: ""}"
                    Text(
                        text = initials,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = SKyberBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Member Name
            Text(
                text = "${skProfile.firstName} ${skProfile.lastName}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SKyberBlue,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Position badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFD700))
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = skProfile.position?.uppercase() ?: "",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tap for more info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "More info",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tap for more info",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
