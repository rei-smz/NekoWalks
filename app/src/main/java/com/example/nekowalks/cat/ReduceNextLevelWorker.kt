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
    override fun doWork(): Result {
        newNextLevel = inputData.getInt("newNextLevel", -1)
        if (newNextLevel == -1) {
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
                newNextLevelTime = currentTime + Duration.ofDays(1).toMillis()
            }
            2 -> {
                newNextLevelTime = currentTime + Duration.ofDays(2).toMillis()
            }
            3 -> {
                newNextLevelTime = currentTime + Duration.ofDays(3).toMillis()
            }
        }
    }

    private fun createOutputData(): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putLong(KEY_CAT_DATA + "_NewNextLevelTime", newNextLevelTime)
        return dataBuilder.build()
    }
}