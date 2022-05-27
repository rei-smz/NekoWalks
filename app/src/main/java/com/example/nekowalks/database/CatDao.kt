package com.example.nekowalks.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM cat_data")
    fun getAllData(): List<CatData>

    @Insert
    suspend fun insert(catData: CatData)

    @Delete
    suspend fun delete(catData: CatData)

    @Update
    suspend fun update(catData: CatData)
}