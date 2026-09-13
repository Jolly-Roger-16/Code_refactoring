package com.example.practica_1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practica_1.data.dao.ItemDao
import com.example.practica_1.data.entity.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "practica_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}