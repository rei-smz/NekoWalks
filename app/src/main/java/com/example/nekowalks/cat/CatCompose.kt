package com.example.nekowalks.cat

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nekowalks.ui.theme.NekoWalksTheme

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
        catViewModel.applyUpdateOneTime()
        Column {
            Text(text = "This is Cat Activity")
            Text(text = "Mood")
            LinearProgressIndicator(progress = moodProcess)
            Text(text = "Food")
            LinearProgressIndicator(progress = foodProcess)
            Text(text = "Water")
            LinearProgressIndicator(progress = waterProcess)
            Text(text = "Level: ${it[0].level}")
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
