package com.example.skyber.Cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skyber.R
import com.example.skyber.ui.theme.SKyberBlue
import com.example.skyber.ui.theme.White


@Composable
fun AnnouncementCard(backgroundColor: Color = White, fontColor: Color = Black/*Retrieve announcement image and title */){
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(38.dp),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(2.dp)
        ){
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(210.dp),
                verticalArrangement = Arrangement.Center,
            ){
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Lorem ipsum dolor sit amet",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = fontColor
                )

            }

            Image(
                painter = painterResource(id = R.drawable.image),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(120.dp)
                    .width(100.dp)
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


@Composable
fun ListCard(title: String, icon: ImageVector, onCardClick: () -> Unit ){
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .height(80.dp)
            .clickable{ onCardClick()}
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
                    .padding(horizontal =  4.dp)
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