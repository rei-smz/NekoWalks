package com.example.nekowalks

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.ShopViewModel
import com.example.nekowalks.steps.StepsListener
import kotlinx.coroutines.launch

class MainLifeCycle(
    private val sensorManager: SensorManager,
    private val shopViewModel: Lazy<ShopViewModel>,
    private val profileViewModel: Lazy<ProfileViewModel>,
    private val catViewModel: Lazy<CatViewModel>
): DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        profileViewModel.value.setUserData()
        catViewModel.value.setCatData()
        catViewModel.value.applyUpdatePeriodic()
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (sensor != null) {
            sensorManager.registerListener(StepsListener(profileViewModel), sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        catViewModel.value.applyUpdateOneTime()
        profileViewModel.value.storeUserData()
        catViewModel.value.storeCatData()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        profileViewModel.value.setUserData()
        catViewModel.value.applyUpdateOneTime()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        sensorManager.unregisterListener(StepsListener(profileViewModel))
        profileViewModel.value.storeUserData()
        catViewModel.value.storeCatData()
    }
}