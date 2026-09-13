package com.example.practica_1.data.dao

import androidx.room.*
import com.example.practica_1.data.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemById(id: Int): Item?
}