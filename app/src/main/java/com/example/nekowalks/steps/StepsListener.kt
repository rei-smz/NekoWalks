package com.example.nekowalks.steps

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import com.example.nekowalks.profile.ProfileViewModel

class StepsListener(
    private val profileViewModel: Lazy<ProfileViewModel>,
) : SensorEventListener {
    private var lastValue = -1
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val value = event.values[0].toInt()
                Log.d("StepsListener", "Steps: $value")
                if (value != 0 && lastValue != -1 && value != lastValue) {
                    profileViewModel.value.increaseCurrentSteps(value - lastValue)
                    profileViewModel.value.increaseTotalSteps(value - lastValue)
                    profileViewModel.value.storeUserData()
                    profileViewModel.value.setUserData()
                }
                lastValue = value
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("StepsListener", "Accuracy: $accuracy")
    }
}