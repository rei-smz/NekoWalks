package com.example.nekowalks

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.text.BoringLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.cat.CatViewModelFactory
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.CatRepository
import com.example.nekowalks.database.ShopRepository
import com.example.nekowalks.database.UserRepository
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.ShopViewModel
import com.example.nekowalks.steps.StepsListener
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions


class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private val shopViewModel = lazy {
        ShopViewModel(application)
    }
    private val profileViewModel = lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(ProfileViewModel::class.java)
    }
    private val catViewModel = lazy {
        CatViewModelFactory(application, lifecycleOwner = this).create(CatViewModel::class.java)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        XXPermissions.with(this).permission(Permission.ACTIVITY_RECOGNITION).request(
            object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
                        if (sensor != null) {
                            sensorManager.registerListener(StepsListener(profileViewModel), sensor, SensorManager.SENSOR_DELAY_NORMAL)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "需要运动权限", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        Toast.makeText(this@MainActivity, "需要运动权限", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "需要运动权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
        lifecycle.addObserver(
            MainLifeCycle(
                sensorManager,
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
