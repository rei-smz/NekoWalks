package com.example.nekowalks.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Shop(viewModel: ShopViewModel = viewModel()) {
    val items = viewModel.getItems().observeAsState()
    Column() {
        items.value?.let {
            it.forEachIndexed {
                    index, shopItem ->
                Text(text = shopItem.name.toString())
                Text(text = shopItem.cost.toString())
                Text(text = shopItem.description.toString())
                Spacer(modifier = Modifier.padding(top = 10.dp))
            }
        }
    }
}

