package com.example.nekowalks.shop

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    LazyColumn(state = scrollState) {
        shopItems.value?.let {

            items(it) { item ->
                var showDialog by remember {
                    mutableStateOf(false)
                }
                Row(Modifier.clickable { showDialog = !showDialog }) {
                    Column() {
                        item.name?.let { it1 -> Text(it1, fontWeight = FontWeight.Bold) }
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            item.description?.let { it2 ->
                                Text(it2, style = MaterialTheme.typography.body2)
                            }
                        }
                    }
                    Column() {
                        Text(text = item.cost.toString(), style = MaterialTheme.typography.body1)
                    }
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
                            Text(text = "以${item.cost}的价格购买${item.description}？")
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
                        catViewModel.applyUpdateOneTime(true)
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
                        catViewModel.applyUpdateOneTime(true)
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
                        catViewModel.applyUpdateOneTime(true)
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

    }
}
