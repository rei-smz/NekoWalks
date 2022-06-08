package com.example.nekowalks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData::class, ShopItem::class, CatData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun shopDao(): ShopDao
    abstract fun catDao(): CatDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tmpInstance = INSTANCE
            if (tmpInstance != null) {
                return tmpInstance
            }

            synchronized(this) {
                val newInstance = Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
                    .createFromAsset("database/data.db")
                    .build()
                INSTANCE = newInstance
                return newInstance
            }
        }
    }
}