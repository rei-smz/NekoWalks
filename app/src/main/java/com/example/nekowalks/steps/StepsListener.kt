package com.example.nekowalks.steps

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import com.example.nekowalks.MainViewModel
import com.example.nekowalks.profile.ProfileViewModel

object StepsListener: SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                ProfileViewModel.increaseCurrentSteps(event.values[0].toUInt())
                ProfileViewModel.increaseTotalSteps(event.values[0].toUInt())
                MainViewModel.updateSteps()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}