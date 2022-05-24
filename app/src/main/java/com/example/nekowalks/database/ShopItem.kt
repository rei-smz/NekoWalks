package com.example.nekowalks.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItem(
    @PrimaryKey val id: UInt,
    val name: String?,
    val cost: UInt,
    val description: String?,
)