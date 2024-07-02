package com.pravin.mycontact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pravin.mycontact.screens.ContactDetail
import com.pravin.mycontact.screens.ContactHome
import com.pravin.mycontact.screens.ContactList
import com.pravin.mycontact.ui.theme.MyContactTheme
import com.pravin.mycontact.viewmodel.ContactViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ContactViewModel

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ContactViewModel.ContactViewModelFactory(application))[ContactViewModel::class.java]
        setContent {
            MyContactTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                val currentWindowSize = when (windowSize.widthSizeClass){
                    WindowWidthSizeClass.Compact -> false
                    else -> true
                }
                val navController = rememberNavController()

                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                val showTop = viewModel.topUiState
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Contact Geniue") },
                            navigationIcon = { if(showTop == ContactViewModel.TopUiState.Enabled) { IconButton(onClick = { viewModel.onBackClicked() }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                    )
                            } }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = NavigationItem.ContactList.name) {
                        composable(NavigationItem.ContactList.name){
                            ContactHome(
                                modifier = Modifier.padding(paddingValues),
                                viewModel = viewModel,
                                currentWindowSize
                            )
                        }
                    }
                }

            }
        }
    }

    enum class NavigationItem{
        ContactList,
        ContactDetail
    }
}


