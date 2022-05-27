package com.example.nekowalks

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nekowalks.cat.Cat
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.profile.Profile
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.Shop
import com.example.nekowalks.shop.ShopViewModel
import com.example.nekowalks.ui.theme.NekoWalksTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Main(
    profileViewModel: ProfileViewModel,
    catViewModel: CatViewModel,
    shopViewModel: ShopViewModel
) {
    val userData = profileViewModel.getUserData().observeAsState().value?.get(0)
    NekoWalksTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val items = listOf(
                Screen.Cat,
                Screen.Shop,
                Screen.Profile
            )
            val icons = listOf(
                Icons.Rounded.Home,
                Icons.Rounded.ShoppingCart,
                Icons.Rounded.Person
            )
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    userData?.currentSteps?.let { MyTopBar(steps = it) }
                },
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(icons[index], contentDescription = null) },
                                label = { Text(text = screen.route) }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Cat.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Cat.route) {
                        Cat(catViewModel)
                    }
                    composable(Screen.Shop.route) {
                        Shop(
                            snackbarHostState = snackbarHostState,
                            catViewModel = catViewModel,
                            shopViewModel = shopViewModel,
                            profileViewModel = profileViewModel
                        )
                    }
                    composable(Screen.Profile.route) {
                        Profile(profileViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MyTopBar(steps: Int) {
    SmallTopAppBar(
        title = { Text("Neko Walks", style = MaterialTheme.typography.titleLarge) },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_directions_walk_24),
                contentDescription = null
            )
            Text(text = steps.toString(), modifier = Modifier.padding(end = 16.dp))
        }
    )
}
