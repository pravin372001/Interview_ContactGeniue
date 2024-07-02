package com.pravin.mycontact.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.pravin.mycontact.ui.theme.MyContactTheme
import com.pravin.mycontact.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun ContactHome(
    modifier: Modifier = Modifier,
    viewModel: ContactViewModel,
    isExpandedWindow: Boolean,
    ) {
    val context = LocalContext.current
    val windowLayoutInfo = remember { mutableStateOf<WindowLayoutInfo?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    // Perform a side-effect to observe window layout info
    LaunchedEffect(Unit) {
        val windowInfoTracker = WindowInfoTracker.getOrCreate(context)

        lifecycleOwner.lifecycleScope.launch {
            windowInfoTracker.windowLayoutInfo(context as Activity).collect { layoutInfo ->
                windowLayoutInfo.value = layoutInfo
            }
        }
    }

    val halfOpenState = remember {
        derivedStateOf {
            windowLayoutInfo.value?.displayFeatures
                ?.filterIsInstance<FoldingFeature>()
                ?.any { it.state == FoldingFeature.State.HALF_OPENED } == true
        }
    }

    when(viewModel.contactUiState) {
        ContactViewModel.ContactUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        ContactViewModel.ContactUiState.Error -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No Internet")
                Text(text = "Please check your internet connection")
                Button(onClick = { viewModel.getContacts() }) {
                    Text(text = "Retry")
                }
            }
        }
        ContactViewModel.ContactUiState.Success -> {
            ContactHomeContent(
                modifier = modifier,
                viewModel = viewModel,
                isExpandedWindow = halfOpenState.value
            )
        }
    }

}

@Composable
fun ContactHomeContent(
    modifier: Modifier,
    viewModel: ContactViewModel,
    isExpandedWindow: Boolean,
) {
    val selectedContact = viewModel.result.observeAsState()
    val contact = viewModel.contacts.observeAsState()
    val state = rememberLazyListState()
    if(isExpandedWindow){
        viewModel.onTopChange(ContactViewModel.TopUiState.Disabled)
        Row {
            contact.value?.let { check ->
                ContactList(
                    modifier = modifier,
                    contact = check,
                    onNavigate = {
                        viewModel.onContactClicked(it)
                    },
                    isExpandedWindow = isExpandedWindow,
                    state = state
                )
            } ?: run {
                Text(text = "Loading contacts...", modifier = modifier)
            }
            VerticalDivider(
                modifier = modifier
            )
            ContactDetail(
                modifier = modifier,
                contact = selectedContact.value ?: return
            )
        }
    } else {
        if (selectedContact.value != null) {
            viewModel.onTopChange(ContactViewModel.TopUiState.Enabled)
            ContactDetail(
                modifier = modifier,
                contact = selectedContact.value!!
            )
            BackHandler {
                viewModel.onBackClicked()
            }
        } else{
            viewModel.onTopChange(ContactViewModel.TopUiState.Disabled)
            contact.value?.let { check ->
                ContactList(
                    modifier = modifier,
                    contact = check,
                    onNavigate = {
                        viewModel.onContactClicked(it)
                    },
                    isExpandedWindow = isExpandedWindow,
                    state = state
                )
            }
        }
    }
}

@Composable
fun ContactHomePreview() {
    MyContactTheme {
//        ContactHome(
//            viewModel = ContactViewModel()
//        )
    }
}