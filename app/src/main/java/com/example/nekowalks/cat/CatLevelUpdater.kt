package com.example.nekowalks.cat

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.Duration
import java.time.Instant

private const val KEY_CAT_DATA = "KEY_CAT_DATA"

class CatLevelUpdater(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private var nextLevelUp = -1L
    private var level = -1
    private var food = -1
    private var mood = -1
    private var water = -1
    override fun doWork(): Result {
        food = inputData.getInt(KEY_CAT_DATA + "_FOOD", -1)
        mood = inputData.getInt(KEY_CAT_DATA + "_MOOD", -1)
        water = inputData.getInt(KEY_CAT_DATA + "_WATER", -1)
        nextLevelUp = inputData.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
        level = inputData.getInt(KEY_CAT_DATA + "_LEVEL", -1)
        if (nextLevelUp == -1L || level == -1 || food == -1 || mood == -1 || water == -1) {
            return Result.failure()
        }
        checkNextLevelUp()
        val output = createOutputData()
        return Result.success(output)
    }

    private fun checkNextLevelUp() {
        val currentTime = Instant.now().toEpochMilli()
        if (nextLevelUp != -2L && nextLevelUp != 0L && nextLevelUp < currentTime) {
            level += 1
            changeNextLevelUp(currentTime)
        }
    }

    private fun changeNextLevelUp(currentTime: Long) {
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
        dataBuilder.putInt(KEY_CAT_DATA + "_LEVEL", level)
        dataBuilder.putLong(KEY_CAT_DATA + "_LevelUp", nextLevelUp)
        return dataBuilder.build()
    }
}