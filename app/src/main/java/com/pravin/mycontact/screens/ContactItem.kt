package com.pravin.mycontact.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.pravin.mycontact.remote.model.Contact
import com.pravin.mycontact.remote.model.Result
import com.pravin.mycontact.ui.theme.MyContactTheme

@Composable
fun ContactItem(
    imageUrl: String,
    name: String,
    contact: Result,
    onNavigate:(Result)-> Unit
) {
    ContactItemContent(
        imageUrl = imageUrl,
        name = name,
        contact = contact,
        onNavigate = onNavigate
    )
}

@Composable
private fun ContactItemContent(
    modifier: Modifier = Modifier,
    imageUrl: String ,
    name: String,
    contact: Result,
    onNavigate:(Result)-> Unit
) {
    Box(
        modifier = modifier
            .clickable {
                onNavigate(contact)
            }
        ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
            ) {
            Log.d("ContactItemContent", "imageUrl: $imageUrl")
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

        }
    }
}


@Preview
@Composable
private fun ContactItemPreview() {
    MyContactTheme {
//        ContactItemContent(
//            imageUrl = "",
//            name = "",
//            onClick = { return },
//            onNavigate = {}
//        )
    }
}