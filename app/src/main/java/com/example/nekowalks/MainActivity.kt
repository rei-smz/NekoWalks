package com.example.nekowalks

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.room.Room
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.UserData
import com.example.nekowalks.profile.Profile
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.ShopViewModel
import com.example.nekowalks.steps.StepsListener
import com.example.nekowalks.ui.theme.NekoWalksTheme
import kotlinx.coroutines.*
import java.time.Instant


class MainActivity : ComponentActivity() {
    private val shopViewModel by viewModels<ShopViewModel>()
    private lateinit var sensorManager: SensorManager
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContent {
            Main()
        }
    }

    private fun init() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
        val userData = UserData(
            id = 0u,
            totalSteps = 0u,
            currentSteps = 0u
        )
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch(Dispatchers.IO) {
            db.userDao().insert(userData)
        }
        shopViewModel.setItems(db)
        ProfileViewModel.setUserData(db)
        MainViewModel.updateSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (sensor != null) {
            sensorManager.registerListener(StepsListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(StepsListener)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
        val userData = ProfileViewModel.getUserData().value
        if (userData != null) {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch(Dispatchers.IO) {
                db.userDao().update(userData)
            }
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Cat : Screen("Cat", R.string.cat)
    object Shop : Screen("Shop", R.string.shop)
    object Profile : Screen("Profile", R.string.profile)
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    //val shopItems = ShopItems.getAllItems()
//    NekoWalksTheme {
//        // A surface container using the 'background' color from the theme
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            val navController = rememberNavController()
//            val items = listOf(
//                Screen.Cat,
//                Screen.Shop,
//                Screen.Profile
//            )
//            val icons = listOf(
//                Icons.Rounded.Home,
//                Icons.Rounded.ShoppingCart,
//                Icons.Rounded.Person
//            )
//            Scaffold(
//                topBar = {
//                    SmallTopAppBar(
//                        title = { Text("Neko Walks", style = MaterialTheme.typography.titleLarge) },
//                        actions = {
//                            Icon(painter = painterResource(id = R.drawable.ic_round_directions_walk_24), contentDescription = null)
//                            Text(text = "0", modifier = Modifier.padding(end = 16.dp))
//                        }
//                    )
//                },
//                bottomBar = {
//                    NavigationBar {
//                        val navBackStackEntry by navController.currentBackStackEntryAsState()
//                        val currentDestination = navBackStackEntry?.destination
//                        items.forEachIndexed {
//                                index, screen->
//                            NavigationBarItem(
//                                selected = currentDestination?.hierarchy?.any { it.route == screen.route} == true,
//                                onClick = {
//                                    navController.navigate(screen.route) {
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
//                                },
//                                icon = { Icon(icons[index], contentDescription = null) },
//                                label = { Text(text = screen.route) }
//                            )
//                        }
//                    }
//                }
//            ) { innerPadding ->
//                NavHost(
//                    navController = navController,
//                    startDestination = Screen.Cat.route,
//                    Modifier.padding(innerPadding)
//                ) {
//                    composable(Screen.Cat.route) {
//                        //Greeting(name = Screen.Cat.route, modifier = Modifier.padding(innerPadding))
////                        Screen.Cat()
//                    }
//                    composable(Screen.Shop.route) {
//                        //Shop(shopItems)
//                    }
//                    composable(Screen.Profile.route) {
//                        //Greeting(name = Screen.Profile.route, modifier = Modifier.padding(innerPadding))
//                        Profile()
//                    }
//                }
//            }
//        }
//    }
//}

