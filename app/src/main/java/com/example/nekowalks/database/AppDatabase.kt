package com.example.nekowalks.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserData::class, ShopItems::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shopDao(): ShopDao
}