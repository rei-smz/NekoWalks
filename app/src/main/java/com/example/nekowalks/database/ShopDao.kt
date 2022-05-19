package com.example.nekowalks.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop_items")
    suspend fun getAll(): List<ShopItem>
}