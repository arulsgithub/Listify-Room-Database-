package com.example.listify.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.listify.data.room.models.Item
import com.example.listify.data.room.models.ShoppingList
import com.example.listify.data.room.models.Store
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("select * from items")
    fun getAllItems(): Flow<List<Item>>

    @Query("select * from items where item_id=:itemId")
    fun getItem(itemId: Int): Flow<Item>
}

@Dao
interface StoreDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(store: Store)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(store: Store)

    /*@Delete
    suspend fun delete(item: Item)*/

    @Query("select * from stores")
    fun getAllStore(): Flow<List<Store>>

    @Query("select * from stores where store_id=:storeId")
    fun getItem(storeId: Int): Flow<Store>
}

@Dao
interface ListDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingList: ShoppingList)

    @Query(
        """select * from items as i inner join shopping_list as s on i.listId = s.list_id 
        inner join stores as st on i.storeFkId = st.store_id"""
    )
    fun getItemsWithStoreAndList(): Flow<List<ItemsWithStoreAndList>>

    @Query(
        """select * from items as i inner join shopping_list as s on i.listId = s.list_id 
        inner join stores as st on i.storeFkId = st.store_id where s.list_id =:listId"""
    )
    fun getItemsWithStoreAndListFilteredById(listId: Int): Flow<List<ItemsWithStoreAndList>>

    @Query(
        """select * from items as i inner join shopping_list as s on i.listId = s.list_id 
        inner join stores as st on i.storeFkId = st.store_id where i.item_id =:itemId"""
    )
    fun getItemWithStoreAndListFilteredById(itemId: Int): Flow<List<ItemsWithStoreAndList>>
}

data class ItemsWithStoreAndList(

    @Embedded val item: Item,
    @Embedded val shoppingList: ShoppingList,
    @Embedded val store: Store
)
