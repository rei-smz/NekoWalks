package com.example.nekowalks.shop

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nekowalks.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.math.cos


@Composable
fun Shop(viewModel: ShopViewModel = viewModel(), snackbarHostState: SnackbarHostState) {
    val shopItems = viewModel.getItems().observeAsState()
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

                                confirmPurchase(item.cost, scope, snackbarHostState)
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

fun confirmPurchase(cost: UInt, scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    val currentStep = ProfileViewModel.getUserData().value?.currentSteps
    if (currentStep != null) {
        if (currentStep < cost) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "步数不足，购买失败"
                )
            }
        } else {
            ProfileViewModel.decreaseCurrentSteps(cost)
            scope.launch {
                snackbarHostState.showSnackbar(
                    "购买成功"
                )
            }
        }
    }
}

