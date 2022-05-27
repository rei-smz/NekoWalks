package com.example.nekowalks

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.cat.CatViewModelFactory
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.CatRepository
import com.example.nekowalks.database.ShopRepository
import com.example.nekowalks.database.UserRepository
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.ShopViewModel


class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private val shopViewModel = lazy {
        ShopViewModel(application)
    }
    private val profileViewModel = lazy {
        //ProfileViewModel(application)
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(ProfileViewModel::class.java)
    }
    private val catViewModel = lazy {
        //CatViewModel(application, lifecycleOwner = this)
        CatViewModelFactory(application, lifecycleOwner = this).create(CatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lifecycle.addObserver(
            MainLifeCycle(
                sensorManager,
                shopViewModel,
                profileViewModel,
                catViewModel
            )
        )
        setContent {
            Main(profileViewModel.value, catViewModel.value, shopViewModel.value)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Cat : Screen("Cat", R.string.cat)
    object Shop : Screen("Shop", R.string.shop)
    object Profile : Screen("Profile", R.string.profile)
}
