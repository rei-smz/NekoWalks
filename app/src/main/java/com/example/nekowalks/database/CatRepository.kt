package com.example.nekowalks.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class CatRepository(private val catDao: CatDao) {
    var catData = MutableLiveData<List<CatData>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateCatData() {
        coroutineScope.launch(Dispatchers.IO) {
            catData.value?.get(0)?.let {
                catDao.update(it)
            }
        }
    }

    fun getCatData() {
        coroutineScope.launch(Dispatchers.Main) {
            catData.value = asyncGet()
        }
    }

    private suspend fun asyncGet(): List<CatData> = coroutineScope.async(Dispatchers.IO) {
        return@async catDao.getAllData()
    }.await()
}