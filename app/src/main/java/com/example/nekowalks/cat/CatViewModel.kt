package com.example.nekowalks.cat

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.CatDao
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

    private fun createInputDataForUpdater(): Data {
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
        catData.value?.get(0)?.nextLevelTime?.let {
            dataBuilder.putLong(KEY_CAT_DATA + "_LevelUp", it)
        }
        catData.value?.get(0)?.level?.let {
            dataBuilder.putInt(KEY_CAT_DATA + "_LEVEL", it)
        }
        return dataBuilder.build()
    }

    internal fun applyUpdatePeriodic() {
        val t = createInputDataForUpdater()
        Log.d("CatViewModel", "${catData.value?.get(0)}")
        val workerRequest = PeriodicWorkRequestBuilder<CatStatusUpdater>(1, TimeUnit.HOURS)
            .setInputData(t)
            .build()
        workerManager.enqueue(workerRequest)

        workerManager.getWorkInfoByIdLiveData(workerRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newFood = workOutput.getInt(KEY_CAT_DATA + "_FOOD", -1)
                val newMood = workOutput.getInt(KEY_CAT_DATA + "_MOOD", -1)
                val newWater = workOutput.getInt(KEY_CAT_DATA + "_WATER", -1)
                val newStatus = workOutput.getLong(KEY_CAT_DATA + "_STATUS", -1L)
                val newLevelUp = workOutput.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
                val newLevel = workOutput.getInt(KEY_CAT_DATA + "_LEVEL", -1)
                if (newFood != -1) {
                    setFood(newFood)
                }
                if (newMood != -1) {
                    setMood(newMood)
                }
                if (newWater != -1) {
                    setWater(newWater)
                }
                if (newLevel != -1) {
                    setLevel(newLevel)
                }
                if (newStatus != -1L) {
                    setStatusUpdateTime(newStatus)
                }
                if (newLevelUp != -1L) {
                    setLevelUpTime(newLevelUp)
                }
            }
        })
    }

    internal fun applyUpdateOneTime() {
        val workerRequest = OneTimeWorkRequestBuilder<CatStatusUpdater>()
            .setInputData(createInputDataForUpdater())
            .build()
        workerManager.enqueue(workerRequest)

        workerManager.getWorkInfoByIdLiveData(workerRequest.id).observe(lifecycleOwner, Observer {
            if (it.state.isFinished) {
                val workOutput = it.outputData
                val newFood = workOutput.getInt(KEY_CAT_DATA + "_FOOD", -1)
                val newMood = workOutput.getInt(KEY_CAT_DATA + "_MOOD", -1)
                val newWater = workOutput.getInt(KEY_CAT_DATA + "_WATER", -1)
                val newStatus = workOutput.getLong(KEY_CAT_DATA + "_STATUS", -1L)
                val newLevelUp = workOutput.getLong(KEY_CAT_DATA + "_LevelUp", -1L)
                val newLevel = workOutput.getInt(KEY_CAT_DATA + "_LEVEL", -1)
                if (newFood != -1) {
                    setFood(newFood)
                }
                if (newMood != -1) {
                    setMood(newMood)
                }
                if (newWater != -1) {
                    setWater(newWater)
                }
                if (newLevel != -1) {
                    setLevel(newLevel)
                }
                if (newStatus != -1L) {
                    setStatusUpdateTime(newStatus)
                }
                if (newLevelUp != -1L) {
                    setLevelUpTime(newLevelUp)
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
