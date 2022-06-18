package com.example.nekowalks.cat

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.CatData
import com.example.nekowalks.database.CatRepository
import java.util.concurrent.TimeUnit

private const val KEY_CAT_DATA = "KEY_CAT_DATA"

class CatViewModel(application: Application, private val lifecycleOwner: LifecycleOwner): AndroidViewModel(application) {
    private val catData: MutableLiveData<List<CatData>>
    private val repository: CatRepository

    init {
        val db = AppDatabase.getInstance(application)
        val catDao = db.catDao()
        repository = CatRepository(catDao)
        catData = repository.catData
    }

    private val workerManager = WorkManager.getInstance(application)

    private fun createInputDataForStatusUpdater(): Data {
        val dataBuilder = Data.Builder()
        catData.value?.get(0)?.food?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_FOOD", it)
        }
        catData.value?.get(0)?.mood?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_MOOD", it)
        }
        catData.value?.get(0)?.water?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_WATER", it)
        }
        catData.value?.get(0)?.nextStatusUpdateTime?.let {
            dataBuilder.putLong(KEY_CAT_DATA + "_STATUS", it)
        }
        return dataBuilder.build()
    }

    private fun createInputDataForLevelUpUpdater(): Data {
        val dataBuilder = Data.Builder()
        catData.value?.get(0)?.food?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_FOOD", it)
        }
        catData.value?.get(0)?.mood?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_MOOD", it)
        }
        catData.value?.get(0)?.water?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_WATER", it)
        }
        catData.value?.get(0)?.nextLevelTime?.let {
            dataBuilder.putLong(KEY_CAT_DATA + "_LevelUp", it)
        }
        catData.value?.get(0)?.level?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_LEVEL", it)
        }
        return dataBuilder.build()
    }

    private fun createInputDataForReduceWorker(newNextLevel: Int, nextLevelTime: Long): Data {
        val dataBuilder = Data.Builder()
        dataBuilder.putInt(KEY_CAT_DATA + "_NewNextLevel", newNextLevel)
        dataBuilder.putLong(KEY_CAT_DATA + "_NextLevelTime", nextLevelTime)
        return dataBuilder.build()
    }

    internal fun applyStatusUpdatePeriodic() {
        val statusUpdaterRequest = PeriodicWorkRequestBuilder<CatStatusUpdater>(1, TimeUnit.HOURS)
            .setInputData(createInputDataForStatusUpdater())
            .build()
        val levelUpdaterRequest = PeriodicWorkRequestBuilder<CatLevelUpdater>(12, TimeUnit.HOURS)
            .setInputData(createInputDataForLevelUpUpdater())
            .build()
        workerManager.enqueue(listOf(statusUpdaterRequest, levelUpdaterRequest))

        workerManager.getWorkInfoByIdLiveData(statusUpdaterRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newFood = workOutput.getInt(KEY_CAT_DATA + "_FOOD", -1)
                val newMood = workOutput.getInt(KEY_CAT_DATA + "_MOOD", -1)
                val newWater = workOutput.getInt(KEY_CAT_DATA + "_WATER", -1)
                val newStatus = workOutput.getLong(KEY_CAT_DATA + "_STATUS", -1L)
                if (newFood != -1) {
                    setFood(newFood)
                }
                if (newMood != -1) {
                    setMood(newMood)
                }
                if (newWater != -1) {
                    setWater(newWater)
                }
                if (newStatus != -1L) {
                    setStatusUpdateTime(newStatus)
                }
            }
        })

        workerManager.getWorkInfoByIdLiveData(levelUpdaterRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newLevel = workOutput.getInt(KEY_CAT_DATA + "_LEVEL", -1)
                val newLevelUp = workOutput.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
                if (newLevel != -1) {
                    setLevel(newLevel)
                }
                if (newLevelUp != -1L) {
                    setLevelUpTime(newLevelUp)
                }
            }
        })
    }

    internal fun applyStatusUpdateOneTime() {
        val statusUpdaterRequest = OneTimeWorkRequestBuilder<CatStatusUpdater>()
            .setInputData(createInputDataForStatusUpdater())
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        val levelUpdaterRequest = OneTimeWorkRequestBuilder<CatLevelUpdater>()
            .setInputData(createInputDataForLevelUpUpdater())
            .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        workerManager.beginWith(statusUpdaterRequest).then(levelUpdaterRequest).enqueue()

        workerManager.getWorkInfoByIdLiveData(statusUpdaterRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newFood = workOutput.getInt(KEY_CAT_DATA + "_FOOD", -1)
                val newMood = workOutput.getInt(KEY_CAT_DATA + "_MOOD", -1)
                val newWater = workOutput.getInt(KEY_CAT_DATA + "_WATER", -1)
                val newStatus = workOutput.getLong(KEY_CAT_DATA + "_STATUS", -1L)
                if (newFood != -1) {
                    setFood(newFood)
                }
                if (newMood != -1) {
                    setMood(newMood)
                }
                if (newWater != -1) {
                    setWater(newWater)
                }
                if (newStatus != -1L) {
                    setStatusUpdateTime(newStatus)
                }
            }
        })

        workerManager.getWorkInfoByIdLiveData(levelUpdaterRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newLevel = workOutput.getInt(KEY_CAT_DATA + "_LEVEL", -1)
                val newLevelUp = workOutput.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
                if (newLevel != -1) {
                    setLevel(newLevel)
                }
                if (newLevelUp != -1L) {
                    setLevelUpTime(newLevelUp)
                }
            }
        })
    }

    fun checkReduce(): Int {
        catData.value?.get(0)?.let {
            val food = it.food
            val mood = it.mood
            val water = it.water
            if (food == 0 || mood == 0 || water == 0) {
                return -2
            }
            if ((food in 1..25) || (mood in 1..25) || (water in 1..25)) {
                return 4
            }
            if ((food in 26..50) || (mood in 26..50) || (water in 26..50)) {
                return 3
            }
            if ((food in 51..75) || (mood in 51..75) || (water in 51..75)) {
                return 2
            }
            return 1
        }
        return 0
    }

    internal fun applyReduceNextLevel(newNextLevel: Int) {
        val reduceWorker = OneTimeWorkRequestBuilder<ReduceNextLevelWorker>()
            .setInputData(createInputDataForReduceWorker(newNextLevel, catData.value?.get(0)?.nextLevelTime ?: -1L))
            .build()
        workerManager.enqueue(reduceWorker)

        workerManager.getWorkInfoByIdLiveData(reduceWorker.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newNextLevelTime = workOutput.getLong(KEY_CAT_DATA + "_NewNextLevelTime", -1L)
                if (newNextLevelTime != -1L) {
                    setLevelUpTime(newNextLevelTime)
                }
            }
        })
    }

    fun getCatData(): LiveData<List<CatData>> {
        return catData
    }

    fun setCatData() {
        repository.getCatData()
    }

    fun storeCatData() {
        repository.updateCatData()
    }

    fun increaseMood(value: Int) {
        if (catData.value!![0].mood + value <= 100) {
            catData.value!![0].mood += value
        } else {
            catData.value!![0].mood = 100
        }
        repository.catData = catData
    }

    fun increaseFood(value: Int) {
        if (catData.value!![0].food + value <= 100) {
            catData.value!![0].food += value
        } else {
            catData.value!![0].food = 100
        }
        repository.catData = catData
    }

    fun increaseWater(value: Int) {
        if (catData.value!![0].water + value <= 100) {
            catData.value!![0].water += value
        } else {
            catData.value!![0].water = 100
        }
        repository.catData = catData
    }

    private fun setStatusUpdateTime(time: Long) {
        catData.value!![0].nextStatusUpdateTime = time
        repository.catData = catData
    }

    private fun setLevelUpTime(time: Long) {
        catData.value!![0].nextLevelTime = time
        repository.catData = catData
    }

    private fun setFood(value: Int) {
        catData.value!![0].food = value
        repository.catData = catData
    }

    private fun setMood(value: Int) {
        catData.value!![0].mood = value
        repository.catData = catData
    }

    private fun setWater(value: Int) {
        catData.value!![0].water = value
        repository.catData = catData
    }

    private fun setLevel(value: Int) {
        catData.value!![0].level = value
        repository.catData = catData
    }
}

class CatViewModelFactory(private val application: Application, private val lifecycleOwner: LifecycleOwner): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
            return CatViewModel(application, lifecycleOwner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
