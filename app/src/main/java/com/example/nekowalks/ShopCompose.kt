package com.example.nekowalks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Shop(shopItems: List<ShopItem>) {
    Column() {
        //Text(text = "Shop item 1")
        //Text(text = "Shop item 2")
        shopItems.forEachIndexed {
            index, shopItem ->
            Text(text = shopItem.name)
            Text(text = shopItem.description)
            Spacer(modifier = Modifier.padding(top = 10.dp))
        }
    }
}

fun getShopItem(): List<ShopItem> {
    return listOf(
        ShopItem("item1", 10u, "This is item 1"),
        ShopItem("item2", 20u, "This is item 2")
    )
}
