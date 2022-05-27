package com.example.nekowalks.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shop_items")
    fun getAll(): LiveData<List<ShopItem>>
}