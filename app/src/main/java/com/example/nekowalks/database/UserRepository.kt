package com.example.nekowalks.database

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// This is the Repository class that will be used to communicate with the database and get the data from it.
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