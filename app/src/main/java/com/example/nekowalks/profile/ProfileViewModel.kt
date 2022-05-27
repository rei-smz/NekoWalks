package com.example.nekowalks.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.UserDao
import com.example.nekowalks.database.UserData
import com.example.nekowalks.database.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    private val userData: MutableLiveData<List<UserData>>
    private val repository: UserRepository

    init {
        val db = AppDatabase.getInstance(application)
        val userDao = db.userDao()
        repository = UserRepository(userDao)
        userData = repository.userData
    }

    fun getUserData(): LiveData<List<UserData>> {
        return userData
    }

    fun setUserData() {
        repository.getUserData()
    }

    fun storeUserData() {
        repository.updateUserData()
    }

    fun increaseCurrentSteps(steps: Int) {
        if (userData.value != null) {
            userData.value!![0].currentSteps += steps
        }
        repository.userData = userData
    }

    fun decreaseCurrentSteps(steps: Int) {
        if (userData.value != null) {
            userData.value!![0].currentSteps -= steps
        }
        repository.userData = userData
    }

    fun increaseTotalSteps(steps: Int) {
        if (userData.value != null) {
            userData.value!![0].totalSteps += steps
        }
        repository.userData = userData
    }
}