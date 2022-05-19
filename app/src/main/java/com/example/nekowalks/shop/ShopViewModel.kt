package com.example.nekowalks.shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nekowalks.database.AppDatabase
import com.example.nekowalks.database.ShopItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopViewModel: ViewModel() {
    private val shopItems: MutableLiveData<List<ShopItem>> by lazy {
        MutableLiveData<List<ShopItem>>()
    }

    fun getItems(): MutableLiveData<List<ShopItem>> {
        return shopItems
    }

    fun setItems(db: AppDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            shopItems.value = db.shopDao().getAll()
        }
    }
}