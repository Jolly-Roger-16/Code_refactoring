package com.example.practica_1.repository

import com.example.practica_1.data.dao.ItemDao
import com.example.practica_1.data.entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insert(item: Item) = itemDao.insert(item)
    suspend fun update(item: Item) = itemDao.update(item)
    suspend fun delete(item: Item) = itemDao.delete(item)
}