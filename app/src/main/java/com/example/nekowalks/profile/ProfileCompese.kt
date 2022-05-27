package com.example.nekowalks.profile

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun Profile(viewModel: ProfileViewModel) {
    viewModel.setUserData()
    val userData by viewModel.getUserData().observeAsState()
    userData?.let {
        Column {
            Text(text = "current steps: ${it[0].currentSteps}")
            Text(text = "total steps: ${it[0].totalSteps}")
        }
    }
}