package com.example.nekowalks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat_data")
data class CatData(
    @PrimaryKey val id: UInt,
    @ColumnInfo(name = "mood_value") var mood: UInt,
    @ColumnInfo(name = "food_value") var food: UInt,
    @ColumnInfo(name = "water_value") var water: UInt,
    @ColumnInfo(name = "level") var level: UInt,
    @ColumnInfo(name = "next_level") var nextLevelTime: Long,
    @ColumnInfo(name = "next_status") var nextStatusUpdateTime: Long
)
