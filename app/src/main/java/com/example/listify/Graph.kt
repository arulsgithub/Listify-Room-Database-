package com.example.listify

import android.content.Context
import com.example.listify.data.room.ShoppingListDB
import com.example.listify.repository.Repository

object Graph {

    lateinit var db: ShoppingListDB
        private set

    val repository by lazy {
        Repository(
            listDao = db.listDao(),
            storeDao = db.storeDao(),
            itemDao = db.itemDao()
        )
    }

    fun create(context: Context){
        db = ShoppingListDB.getDB(context)
    }
}