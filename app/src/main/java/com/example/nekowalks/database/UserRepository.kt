package com.example.nekowalks.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class UserRepository(private val userDao: UserDao) {

    var userData = MutableLiveData<List<UserData>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun updateUserData() {
        coroutineScope.launch(Dispatchers.IO) {
            userData.value?.get(0)?.let { userDao.update(it) }
        }
    }

    fun getUserData() {
        coroutineScope.launch(Dispatchers.Main) {
            userData.value = asyncGet()
        }
    }

    private suspend fun asyncGet(): List<UserData> = coroutineScope.async(Dispatchers.IO) {
        return@async userDao.getAllData()
    }.await()
}