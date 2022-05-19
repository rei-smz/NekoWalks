package com.example.nekowalks.database

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user_data")
    suspend fun getAllData(): List<UserData>

    @Insert
    suspend fun insert(userData: UserData)

    @Delete
    suspend fun delete(userData: UserData)

    @Update
    suspend fun update(userData: UserData)
}