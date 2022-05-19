package com.example.nekowalks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nekowalks.profile.ProfileViewModel

object MainViewModel: ViewModel() {
    private val steps: MutableLiveData<UInt> by lazy {
        MutableLiveData<UInt>().also {
            it.value = 0u
        }
    }

    fun getSteps(): LiveData<UInt> {
        return steps
    }

    fun updateSteps() {
        this.steps.value = ProfileViewModel.getUserData().value?.currentSteps
    }
}