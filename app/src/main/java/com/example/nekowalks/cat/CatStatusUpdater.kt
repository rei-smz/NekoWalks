package com.example.nekowalks.cat

import android.content.Context
import android.os.Build
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.Duration
import java.time.Instant

private const val KEY_CAT_DATA = "KEY_CAT_DATA"

class CatStatusUpdater(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private var food = -1
    private var mood = -1
    private var water = -1
    private var nextStatusUpdate = - 1L
    override fun doWork(): Result {
        food = inputData.getInt(KEY_CAT_DATA + "_FOOD", -1)
        mood = inputData.getInt(KEY_CAT_DATA + "_MOOD", -1)
        water = inputData.getInt(KEY_CAT_DATA + "_WATER", -1)
        nextStatusUpdate = inputData.getLong(KEY_CAT_DATA + "_STATUS", -1L)
        if (food == -1 || mood == -1 || water == -1 || nextStatusUpdate == -1L) {
            return Result.failure()
        }
        checkCatStatus()
        val outputData = createOutputData()
        return Result.success(outputData)
    }

    private fun checkCatStatus() {
        val currentTime = Instant.now().toEpochMilli()
        if (nextStatusUpdate < currentTime) {
            val duration = Duration.ofMillis(currentTime - nextStatusUpdate)
            val days: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                duration.toDaysPart()
            } else {
                duration.toDays()
            }
            if (food - 10 * (days + 1).toInt() >= 0) {
                food -= 10 * (days + 1).toInt()
            }
            if (mood - 10 * (days + 1).toInt() >= 0) {
                mood -= 10 * (days + 1).toInt()
            }
            if (water - 10 * (days + 1).toInt() >= 0) {
                water -= 10 * (days + 1).toInt()
            }
            nextStatusUpdate += Duration.ofDays(days + 1).toMillis()
        }
    }

    private fun createOutputData(): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putInt(KEY_CAT_DATA + "_FOOD", food)
        dataBuilder.putInt(KEY_CAT_DATA + "_MOOD", mood)
        dataBuilder.putInt(KEY_CAT_DATA + "_WATER", water)
        dataBuilder.putLong(KEY_CAT_DATA + "_STATUS", nextStatusUpdate)
        return dataBuilder.build()
    }
}
