package com.example.nekowalks.cat

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.time.*
import java.time.temporal.TemporalAmount
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit

class CatStatusUpdater(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        checkCatStatus()
        return Result.success()
    }
}

fun checkCatStatus() {
    val catData = CatViewModel.getCatData().value
    val currentTime = Instant.now().toEpochMilli()
    val updateTime = catData!!.nextStatusUpdateTime
    if (updateTime < currentTime) {
        val duration = Duration.ofMillis(currentTime - updateTime)
        val days: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            duration.toDaysPart()
        } else {
            duration.toDays()
        }
        CatViewModel.decreaseFood(10u * (days + 1).toUInt())
        CatViewModel.decreaseMood(10u * (days + 1).toUInt())
        CatViewModel.decreaseWater(10u * (days + 1).toUInt())
        CatViewModel.setStatusUpdateTime(updateTime + Duration.ofDays(days + 1).toMillis())
    }
}
