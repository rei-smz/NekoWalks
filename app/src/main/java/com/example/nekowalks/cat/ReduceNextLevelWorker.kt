package com.example.nekowalks.cat

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.Duration
import java.time.Instant

private const val KEY_CAT_DATA = "KEY_CAT_DATA"

class ReduceNextLevelWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    private var newNextLevel: Int = -1
    private var newNextLevelTime: Long = -1
    private var nextLevelTime: Long = -1
    override fun doWork(): Result {
        newNextLevel = inputData.getInt(KEY_CAT_DATA + "_NewNextLevelTime", -1)
        nextLevelTime = inputData.getLong(KEY_CAT_DATA + "_NextLevelTime", -1)
        if (newNextLevel == -1 || nextLevelTime == -1L) {
            return Result.failure()
        }
        reduceNextLevel()
        val output = createOutputData()
        return Result.success(output)
    }

    private fun reduceNextLevel() {
        val currentTime = Instant.now().toEpochMilli()
        when (newNextLevel) {
            1 -> {
                if (nextLevelTime == -2L || nextLevelTime > currentTime + Duration.ofDays(1).toMillis()) {
                    newNextLevelTime = currentTime + Duration.ofDays(1).toMillis()
                }
            }
            2 -> {
                if (nextLevelTime == -2L || nextLevelTime > currentTime + Duration.ofDays(2).toMillis()) {
                    newNextLevelTime = currentTime + Duration.ofDays(2).toMillis()
                }
            }
            3 -> {
                if (nextLevelTime == -2L || nextLevelTime > currentTime + Duration.ofDays(3).toMillis()) {
                    newNextLevelTime = currentTime + Duration.ofDays(3).toMillis()
                }
            }
            4 -> {
                if (nextLevelTime == -2L || nextLevelTime > currentTime + Duration.ofDays(4).toMillis()) {
                    newNextLevelTime = currentTime + Duration.ofDays(4).toMillis()
                }
            }
        }
    }

    private fun createOutputData(): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putLong(KEY_CAT_DATA + "_NewNextLevelTime", newNextLevelTime)
        return dataBuilder.build()
    }
}