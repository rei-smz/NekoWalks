package com.example.nekowalks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItem(
    @PrimaryKey val id: Int,
    val name: String?,
    val cost: Int,
    val description: String?,
    val type: Int,
    @ColumnInfo(name = "add_amount") val addAmount: Int
)