package com.example.nekowalks.steps

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.nekowalks.profile.ProfileViewModel

// This class is used to get the steps from the sensor
class StepsListener(
    private val profileViewModel: Lazy<ProfileViewModel>,
) : SensorEventListener {
    private var lastValue = -1
    // This method is called when the sensor is ready to get the data from the sensor.
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val value = event.values[0].toInt()
                if (value != 0 && lastValue != -1 && value != lastValue) {
                    // Use the view model to update the steps
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
    }
}