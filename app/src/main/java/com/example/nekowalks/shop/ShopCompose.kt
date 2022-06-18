package com.example.nekowalks.shop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nekowalks.cat.CatViewModel
import com.example.nekowalks.profile.ProfileViewModel
import com.example.nekowalks.ui.theme.NekoWalksTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun Shop(
    shopViewModel: ShopViewModel,
    profileViewModel: ProfileViewModel,
    catViewModel: CatViewModel,
    snackbarHostState: SnackbarHostState
) {
    val shopItems = shopViewModel.getItems().observeAsState()
    val userData = profileViewModel.getUserData().observeAsState()
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LazyColumn(
        state = scrollState,
        modifier = Modifier.fillMaxWidth()
    ) {
        shopItems.value?.let { it ->
            items(it) { item ->
                var showDialog by remember {
                    mutableStateOf(false)
                }
                // Show the list of items.
                Row(
                    Modifier
                        .padding(16.dp)
                        .clickable { showDialog = !showDialog }
                        .fillMaxWidth()) {
                    item.type.let { it1 ->
                        when (it1) {
                            0 -> {
                                Icon(
                                    Icons.Rounded.Restaurant,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight().padding(end = 2.dp)
                                )
                            }
                            1 -> {
                                Icon(
                                    Icons.Rounded.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight().padding(end = 2.dp)
                                )
                            }
                            2 -> {
                                Icon(
                                    Icons.Rounded.WaterDrop,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxHeight().padding(end = 2.dp)
                                )
                            }
                            else -> {}
                        }
                    }
                    Column(
                        Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.Start)) {
                        item.name?.let { it1 -> Text(it1, fontWeight = FontWeight.Bold) }
                        CompositionLocalProvider(
                            LocalContentAlpha provides ContentAlpha.medium,
                        ) {
                            item.description?.let { it2 ->
                                Text(
                                    it2,
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                    Text(
                        text = item.cost.toString(),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.End)
                    )
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = {
                            Text(text = "确认购买")
                        },
                        text = {
                            Text(text = "以${item.cost}的价格购买${item.name}？")
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                userData.value?.get(0)?.let { it1 ->
                                    confirmPurchase(
                                        item.cost,
                                        item.type,
                                        item.addAmount,
                                        it1.currentSteps,
                                        scope,
                                        snackbarHostState,
                                        profileViewModel,
                                        catViewModel
                                    )
                                }
                                showDialog = false
                            }) {
                                Text(text = "是")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text(text = "否")
                            }
                        }
                    )
                }
            }
        }
    }
}

// This function is called when the user taps the item.
fun confirmPurchase(
    cost: Int,
    type: Int,
    add: Int,
    currentSteps: Int,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    profileViewModel: ProfileViewModel,
    catViewModel: CatViewModel
) {
    if (currentSteps < cost) {
        scope.launch {
            snackbarHostState.showSnackbar(
                "步数不足，购买失败"
            )
        }
    } else {
        val catData = catViewModel.getCatData().value?.get(0)
        catData?.let {
            when (type) {
                0 -> {
                    if (it.food < 100) {
                        profileViewModel.decreaseCurrentSteps(cost)
                        profileViewModel.storeUserData()
                        profileViewModel.setUserData()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "购买成功"
                            )
                        }
                        catViewModel.increaseFood(add)
                        val newNextLevel = catViewModel.checkReduce()
                        if (newNextLevel != 0 || newNextLevel != -2) {
                            catViewModel.applyReduceNextLevel(newNextLevel)
                        }
                        catViewModel.storeCatData()
                        catViewModel.setCatData()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "食物已满，购买失败"
                            )
                        }
                    }
                }
                1 -> {
                    if (it.mood < 100) {
                        profileViewModel.decreaseCurrentSteps(cost)
                        profileViewModel.storeUserData()
                        profileViewModel.setUserData()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "购买成功"
                            )
                        }
                        catViewModel.increaseMood(add)
                        val newNextLevel = catViewModel.checkReduce()
                        if (newNextLevel != 0 || newNextLevel != -2) {
                            catViewModel.applyReduceNextLevel(newNextLevel)
                        }
                        catViewModel.storeCatData()
                        catViewModel.setCatData()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "心情已满，购买失败"
                            )
                        }
                    }
                }
                else -> {
                    if (it.water < 100) {
                        profileViewModel.decreaseCurrentSteps(cost)
                        profileViewModel.storeUserData()
                        profileViewModel.setUserData()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "购买成功"
                            )
                        }
                        catViewModel.increaseWater(add)
                        val newNextLevel = catViewModel.checkReduce()
                        if (newNextLevel != 0 || newNextLevel != -2) {
                            catViewModel.applyReduceNextLevel(newNextLevel)
                        }
                        catViewModel.storeCatData()
                        catViewModel.setCatData()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "水已满，购买失败"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatPreview() {
    NekoWalksTheme {
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {
                Icon(Icons.Rounded.Favorite, contentDescription = null)
                Text(text = "aaaaa", modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start))
                Text(text = "bbbbb", modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.End))
            }
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {
                Column() {
                    Text(text = "aaaaa")
                }
                Column(Modifier.padding(end = 10.dp)) {
                    Text(text = "bbbbb")
                }
            }
        }
    }
}
