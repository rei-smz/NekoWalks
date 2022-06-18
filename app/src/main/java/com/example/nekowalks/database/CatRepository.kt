package com.example.nekowalks.database

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// This is the Repository class that will be used to communicate with the database and get the data from it.
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

    // This function will be used to get the data from the database.
    private suspend fun asyncGet(): List<CatData> = coroutineScope.async(Dispatchers.IO) {
        return@async catDao.getAllData()
    }.await()
}