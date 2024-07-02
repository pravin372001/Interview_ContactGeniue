package com.pravin.mycontact.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pravin.mycontact.remote.model.Contact
import com.pravin.mycontact.remote.model.Result
import com.pravin.mycontact.ui.theme.MyContactTheme

@Composable
fun ContactList(
    modifier: Modifier,
    contact: Contact,
    onNavigate:(Result)-> Unit,
    isExpandedWindow: Boolean,
    state: LazyListState
) {
    ContactListContent(
        modifier = modifier,
        contact = contact,
        onNavigate = onNavigate,
        isExpandedWindow = isExpandedWindow,
        state = state
    )
}

@Composable
fun ContactListContent(
    modifier: Modifier,
    contact: Contact,
    onNavigate:(Result)-> Unit,
    isExpandedWindow: Boolean,
    state: LazyListState
) {
    LazyColumn(
        modifier = if (isExpandedWindow) {
            modifier.size(height = 700.dp, width = 420.dp).padding(8.dp)
        } else {
            modifier.fillMaxSize(1f).padding(8.dp)
        },
        state = state
    ) {
        items(contact.results) {
            Box(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.surface
                    )
                ) {
                    ContactItem(
                        contact = it,
                        imageUrl = it.picture.medium,
                        name = it.name.first + " " + it.name.last,
                        onNavigate = onNavigate
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun ContactListPreview() {
    MyContactTheme {

    }
}