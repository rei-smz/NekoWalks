package com.example.nekowalks.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_data")
    fun getAllData(): List<UserData>

    @Insert
    suspend fun insert(userData: UserData)

    @Delete
    suspend fun delete(userData: UserData)

    @Update
    suspend fun update(userData: UserData)
}