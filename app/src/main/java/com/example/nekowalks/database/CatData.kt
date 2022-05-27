package com.example.nekowalks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_data")
data class CatData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "mood_value") var mood: Int,
    @ColumnInfo(name = "food_value") var food: Int,
    @ColumnInfo(name = "water_value") var water: Int,
    @ColumnInfo(name = "level") var level: Int,
    @ColumnInfo(name = "next_level") var nextLevelTime: Long, // -2代表停止生长
    @ColumnInfo(name = "next_status") var nextStatusUpdateTime: Long
)
