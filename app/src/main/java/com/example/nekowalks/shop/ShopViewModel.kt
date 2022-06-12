package com.example.nekowalks.shop

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.ShopItem
import com.example.nekowalks.database.ShopRepository

class ShopViewModel(application: Application) {
    private val shopItems: LiveData<List<ShopItem>>
    private val repository: ShopRepository

    init {
        val db = AppDatabase.getInstance(application)
        val shopDao = db.shopDao()
        repository = ShopRepository(shopDao)
        shopItems = repository.shopItems
    }

    fun getItems(): LiveData<List<ShopItem>> {
        Log.d("ShopViewModel", "${shopItems.value}")
        return shopItems
    }
}