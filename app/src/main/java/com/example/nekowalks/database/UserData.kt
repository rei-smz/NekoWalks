package com.example.nekowalks.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserData (
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "total_steps") var totalSteps: Int,
    @ColumnInfo(name = "current_steps") var currentSteps: Int,
)