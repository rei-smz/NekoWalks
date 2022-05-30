package com.example.nekowalks.cat

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.*
import java.time.temporal.TemporalAmount
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit

private const val KEY_CAT_DATA = "KEY_CAT_DATA"

class CatStatusUpdater(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private var food = -1
    private var mood = -1
    private var water = -1
    private var nextStatusUpdate = - 1L
    private var nextLevelUp = -1L
    private var level = -1
    private var isChangeNextLevelUp = false
    override fun doWork(): Result {
        food = inputData.getInt(KEY_CAT_DATA + "_FOOD", -1)
        mood = inputData.getInt(KEY_CAT_DATA + "_MOOD", -1)
        water = inputData.getInt(KEY_CAT_DATA + "_WATER", -1)
        nextStatusUpdate = inputData.getLong(KEY_CAT_DATA + "_STATUS", -1L)
        nextLevelUp = inputData.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
        level = inputData.getInt(KEY_CAT_DATA + "_LEVEL", -1)
        isChangeNextLevelUp = inputData.getBoolean(KEY_CAT_DATA + "_ChangeNextLevelUp", false)
        if (food == -1 || mood == -1 || water == -1 || nextStatusUpdate == -1L || nextLevelUp == -1L || level == -1) {
            return Result.failure()
        }
        checkCatStatus()
        checkNextLevelUp()
        if (isChangeNextLevelUp || nextLevelUp == 0L) {
            changeNextLevelUp()
        }
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

    private fun checkNextLevelUp() {
        val currentTime = Instant.now().toEpochMilli()
        if (nextLevelUp != -2L && nextLevelUp != 0L && nextLevelUp < currentTime) {
            level += 1
            changeNextLevelUp()
        }
    }

    private fun changeNextLevelUp() {
        val currentTime = Instant.now().toEpochMilli()
        if (food == 0 || mood == 0 || water == 0) {
            nextLevelUp = -2L
        } else if ((food in 26..50) || (mood in 26..50) || (water in 26..50)) {
            if (nextLevelUp == -2L || nextLevelUp == 0L) {
                nextLevelUp = currentTime + Duration.ofDays(3).toMillis()
            } else {
                nextLevelUp += Duration.ofDays(3).toMillis()
            }
        } else if ((food in 51..75) || (mood in 51..75) || (water in 51..75)) {
            if (nextLevelUp == -2L || nextLevelUp == 0L) {
                nextLevelUp = currentTime + Duration.ofDays(2).toMillis()
            } else {
                nextLevelUp += Duration.ofDays(2).toMillis()
            }
        } else {
            if (nextLevelUp == -2L || nextLevelUp == 0L) {
                nextLevelUp = currentTime + Duration.ofDays(1).toMillis()
            } else {
                nextLevelUp += Duration.ofDays(1).toMillis()
            }
        }
    }

    private fun createOutputData(): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putInt(KEY_CAT_DATA + "_FOOD", food)
        dataBuilder.putInt(KEY_CAT_DATA + "_MOOD", mood)
        dataBuilder.putInt(KEY_CAT_DATA + "_WATER", water)
        dataBuilder.putLong(KEY_CAT_DATA + "_STATUS", nextStatusUpdate)
        dataBuilder.putLong(KEY_CAT_DATA + "_LevelUp", nextLevelUp)
        dataBuilder.putInt(KEY_CAT_DATA + "_LEVEL", level)
        return dataBuilder.build()
    }
}

