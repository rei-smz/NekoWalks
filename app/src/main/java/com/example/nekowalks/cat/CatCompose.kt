package com.example.nekowalks.cat

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nekowalks.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun Cat(catViewModel: CatViewModel) {
    val catData = catViewModel.getCatData().observeAsState().value?.get(0)
    Log.d("Cat", "Cat data: $catData")
    catData?.let {
        val currentMood = it.mood.toFloat() / 100f
        val moodProcess by animateFloatAsState(
            targetValue = currentMood,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        val currentFood = it.food.toFloat() / 100f
        val foodProcess by animateFloatAsState(
            targetValue = currentFood,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        val currentWater = it.water.toFloat() / 100f
        val waterProcess by animateFloatAsState(
            targetValue = currentWater,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )

        val nextLevelTime = it.nextLevelTime
        val level = it.level
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                propagateMinConstraints = true
            ) {
                CatImage(Modifier.fillMaxSize())
            }
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
            Text(text = "Level: $level", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.displaySmall)
            if (nextLevelTime != -2L) {
                Text(
                    text = "Next level: ${LocalDateTime.ofInstant(Instant.ofEpochMilli(nextLevelTime), ZoneId.of("UTC+8"))}",
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
fun CatImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.left_ear),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.right_ear),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.left_ear_inside),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.right_ear_inside),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.head),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.mouth),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.White),
    )
    Image(
        painter = painterResource(id = R.drawable.nose),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.White),
    )
    Image(
        painter = painterResource(id = R.drawable.left_eye),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.White),
    )
    Image(
        painter = painterResource(id = R.drawable.right_eye),
        contentDescription = null,
        colorFilter = ColorFilter.tint(Color.White),
    )

    Image(
        painter = painterResource(id = R.drawable.collar),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary),
    )
    Image(
        painter = painterResource(id = R.drawable.body),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.foot1),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.foot2),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.foot3),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.foot4),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
    )
    Image(
        painter = painterResource(id = R.drawable.leg1),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.leg2),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.leg2_shadow),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
    )
    Image(
        painter = painterResource(id = R.drawable.leg3),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.leg4),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.tail),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
    )
    Image(
        painter = painterResource(id = R.drawable.tail_shadow),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary),
    )
    Image(
        painter = painterResource(id = R.drawable.tail_cap),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
    )
}
