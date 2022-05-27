package com.example.nekowalks.database

import androidx.lifecycle.LiveData

class ShopRepository(shopDao: ShopDao) {
    val shopItems: LiveData<List<ShopItem>> = shopDao.getAll()
}