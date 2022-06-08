package com.example.nekowalks.cat

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nekowalks.ui.theme.NekoWalksTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun Cat(catViewModel: CatViewModel) {
    val catData by catViewModel.getCatData().observeAsState()
    Log.d("Cat", "Cat data: $catData")
    catData?.let {
        val currentMood by remember { mutableStateOf(it[0].mood.toFloat() / 100f) }
        val moodProcess by animateFloatAsState(
            targetValue = currentMood,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        val currentFood by remember { mutableStateOf(it[0].food.toFloat() / 100f) }
        val foodProcess by animateFloatAsState(
            targetValue = currentFood,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        val currentWater by remember { mutableStateOf(it[0].water.toFloat() / 100f) }
        val waterProcess by animateFloatAsState(
            targetValue = currentWater,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        catViewModel.applyStatusUpdateOneTime()
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "This is Cat Activity")
            Row(Modifier.padding(8.dp)) {
                Icon(Icons.Rounded.Favorite, contentDescription = null)
                Text(text = "Mood")
            }
            LinearProgressIndicator(progress = moodProcess, modifier = Modifier.padding(bottom = 16.dp))
            Row(Modifier.padding(8.dp)) {
                Icon(Icons.Rounded.Restaurant, contentDescription = null)
                Text(text = "Food")
            }
            LinearProgressIndicator(progress = foodProcess, modifier = Modifier.padding(bottom = 16.dp))
            Row(Modifier.padding(8.dp)) {
                Icon(Icons.Rounded.WaterDrop, contentDescription = null)
                Text(text = "Water")
            }
            LinearProgressIndicator(progress = waterProcess, modifier = Modifier.padding(bottom = 16.dp))
            Text(text = "Level: ${it[0].level}", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.displaySmall)
            if (it[0].nextLevelTime != -2L) {
                Text(
                    text = "Next level: ${LocalDateTime.ofInstant(Instant.ofEpochMilli(it[0].nextLevelTime), ZoneId.of("UTC+8"))}",
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                Text(text = "Stopped")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatPreview() {
    NekoWalksTheme {
        var progress by remember { mutableStateOf(0.1f) }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(progress = animatedProgress)
            Spacer(Modifier.requiredHeight(30.dp))
            OutlinedButton(
                onClick = {
                    if (progress < 1f) progress += 0.1f
                }
            ) {
                Text("Increase")
            }
        }
    }
}
