package com.example.nekowalks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData (
    @PrimaryKey val id: UInt,
    @ColumnInfo(name = "total_steps") var totalSteps: UInt,
    @ColumnInfo(name = "current_steps") var currentSteps: UInt,
)