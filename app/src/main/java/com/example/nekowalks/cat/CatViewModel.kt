package com.example.nekowalks.cat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.CatData
import com.example.nekowalks.profile.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CatViewModel: ViewModel() {
    private val catData: MutableLiveData<CatData> by lazy {
        MutableLiveData<CatData>()
    }

    fun getCatData(): LiveData<CatData> {
        return catData
    }

    fun setCatData(db: AppDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            catData.value = db.catDao().getAllData()[0]
        }
    }

    fun increaseMood(value: UInt) {
        catData.value!!.mood += value
    }

    fun decreaseMood(value: UInt) {
        catData.value!!.mood -= value
    }

    fun increaseFood(value: UInt) {
        catData.value!!.food += value
    }

    fun decreaseFood(value: UInt) {
        catData.value!!.food -= value
    }

    fun increaseWater(value: UInt) {
        catData.value!!.water += value
    }

    fun decreaseWater(value: UInt) {
        catData.value!!.water -= value
    }

    fun setStatusUpdateTime(time: Long) {
        catData.value!!.nextStatusUpdateTime = time
    }

    fun setLevelUpTime(time: Long) {
        catData.value!!.nextLevelTime = time
    }
}