package com.example.listify.repository

import com.example.listify.data.room.ItemDao
import com.example.listify.data.room.ListDao
import com.example.listify.data.room.StoreDao
import com.example.listify.data.room.models.Item
import com.example.listify.data.room.models.ShoppingList
import com.example.listify.data.room.models.Store

class Repository(
    private val listDao: ListDao,
    private val storeDao: StoreDao,
    private val itemDao: ItemDao
) {

    val store = storeDao.getAllStore()
    val getItemsWithStoreAndList = listDao.getItemsWithStoreAndList()

    fun getItemWithStoreAndList(id: Int) =
        listDao.getItemWithStoreAndListFilteredById(id)
    fun getItemWithStoreAndListById(id: Int) =
        listDao.getItemsWithStoreAndListFilteredById(id)

    suspend fun insertitem(item: Item){
        itemDao.insert(item)
    }
    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }
    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }


    suspend fun insertList(shoppingList: ShoppingList){
        listDao.insert(shoppingList)
    }
    suspend fun updateStore(store: Store){
        storeDao.update(store)
    }

    suspend fun insertStore(store: Store){
        storeDao.insert(store)
    }
}