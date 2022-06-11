package com.example.nekowalks

import android.hardware.SensorManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.shop.ShopViewModel
import com.example.nekowalks.steps.StepsListener

class MainLifeCycle(
    private val sensorManager: SensorManager,
    private val profileViewModel: Lazy<ProfileViewModel>,
    private val catViewModel: Lazy<CatViewModel>
): DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        profileViewModel.value.setUserData()
        catViewModel.value.setCatData()
//        catViewModel.value.applyStatusUpdateOneTime()
        catViewModel.value.applyStatusUpdatePeriodic()
        catViewModel.value.storeCatData()
        catViewModel.value.setCatData()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
//        catViewModel.value.applyStatusUpdateOneTime()
        profileViewModel.value.storeUserData()
        catViewModel.value.storeCatData()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        profileViewModel.value.setUserData()
        catViewModel.value.applyStatusUpdateOneTime()
        catViewModel.value.storeCatData()
        catViewModel.value.setCatData()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        sensorManager.unregisterListener(StepsListener(profileViewModel))
        profileViewModel.value.storeUserData()
        catViewModel.value.storeCatData()
    }
}