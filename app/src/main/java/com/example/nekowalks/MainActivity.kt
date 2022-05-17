package com.example.nekowalks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nekowalks.ui.theme.NekoWalksTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                        Icons.Rounded.Person,
                    )
                    Scaffold(
                        topBar = {
                            SmallTopAppBar(
                                title = { Text("Neko Walks") },
                                actions = {
                                    Icon(painter = painterResource(id = R.drawable.ic_round_directions_walk_24), contentDescription = null)
                                    Text(text = "0", modifier = Modifier.padding(end = 16.dp))
                                }
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEachIndexed {
                                    index, screen->
                                    NavigationBarItem(
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route} == true,
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
                        },
                        content = { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Cat.route,
                                Modifier.padding(innerPadding)
                            ) {
                                composable(Screen.Cat.route) {
                                    Greeting(name = Screen.Cat.route, modifier = Modifier.padding(innerPadding))
                                }
                                composable(Screen.Shop.route) {
                                    Greeting(name = Screen.Shop.route, modifier = Modifier.padding(innerPadding))
                                }
                                composable(Screen.Profile.route) {
                                    Greeting(name = Screen.Profile.route, modifier = Modifier.padding(innerPadding))
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier,
) {
    Text(text = "Hello $name!", modifier = modifier)
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Cat : Screen("Cat", R.string.cat)
    object Shop : Screen("Shop", R.string.shop)
    object Profile : Screen("Profile", R.string.profile)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
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
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = { Text("Neko Walks") },
                        actions = {
                            Icon(painter = painterResource(id = R.drawable.ic_round_directions_walk_24), contentDescription = null)
                            Text(text = "0", modifier = Modifier.padding(end = 16.dp))
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEachIndexed {
                                index, screen->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route} == true,
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
                },
                content = { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Cat.route,
                        Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Cat.route) {
                            Greeting(name = Screen.Cat.route, modifier = Modifier.padding(innerPadding))
                        }
                        composable(Screen.Shop.route) {
                            Greeting(name = Screen.Shop.route, modifier = Modifier.padding(innerPadding))
                        }
                        composable(Screen.Profile.route) {
                            Greeting(name = Screen.Profile.route, modifier = Modifier.padding(innerPadding))
                        }
                    }
                }
            )
        }
    }
}
