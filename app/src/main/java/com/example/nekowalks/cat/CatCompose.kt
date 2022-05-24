package com.example.nekowalks.cat

import android.widget.ProgressBar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nekowalks.ui.theme.NekoWalksTheme

@Composable
fun Cat() {
    var currentProgress by remember { mutableStateOf(0.1f) }
    val animatedProcess by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    Column {
        Text(text = "This is Cat Activity")
        LinearProgressIndicator(progress = animatedProcess)
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
