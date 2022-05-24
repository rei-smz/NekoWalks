package com.example.nekowalks.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ProfileViewModel: ViewModel() {
    private val userData: MutableLiveData<UserData> by lazy {
        MutableLiveData<UserData>()
    }

    fun getUserData(): LiveData<UserData> {
        return userData
    }

    fun setUserData(db: AppDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            userData.value = db.userDao().getAllData()[0]
        }
    }

    fun increaseCurrentSteps(steps: UInt) {
        userData.value!!.currentSteps += steps
    }

    fun decreaseCurrentSteps(steps: UInt) {
        userData.value!!.currentSteps -= steps
    }

    fun increaseTotalSteps(steps: UInt) {
        userData.value!!.totalSteps += steps
    }
}