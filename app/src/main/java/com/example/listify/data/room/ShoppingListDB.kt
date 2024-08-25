package com.example.listify.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.listify.data.room.converters.DateConverter
import com.example.listify.data.room.models.Item
import com.example.listify.data.room.models.ShoppingList
import com.example.listify.data.room.models.Store

@TypeConverters(DateConverter::class)
@Database(
    entities = [ShoppingList::class, Item::class, Store::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingListDB : RoomDatabase() {

    abstract fun listDao(): ListDao
    abstract fun itemDao(): ItemDao
    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile
        private var INSTANCE: ShoppingListDB? = null

        fun getDB(context: Context): ShoppingListDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingListDB::class.java,
                    "shopping_list_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
