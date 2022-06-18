package com.example.nekowalks.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Profile(viewModel: ProfileViewModel) {
    viewModel.setUserData()
    val userData by viewModel.getUserData().observeAsState()
    userData?.let {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "current steps: ${it[0].currentSteps}", style = MaterialTheme.typography.displaySmall)
            Text(text = "total steps: ${it[0].totalSteps}", style = MaterialTheme.typography.displaySmall)
        }
    }
}
