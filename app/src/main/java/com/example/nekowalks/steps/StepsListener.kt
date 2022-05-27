package com.example.nekowalks.steps

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import com.example.nekowalks.profile.ProfileViewModel

class StepsListener(
    private val profileViewModel: Lazy<ProfileViewModel>,
) : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
                Log.d("StepsListener", "Steps: ${event.values[0]}")
                profileViewModel.value.increaseCurrentSteps(event.values[0].toInt())
                profileViewModel.value.increaseTotalSteps(event.values[0].toInt())
                profileViewModel.value.storeUserData()
                profileViewModel.value.setUserData()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("StepsListener", "Accuracy: $accuracy")
    }
}