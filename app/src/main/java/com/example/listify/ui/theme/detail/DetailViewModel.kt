package com.example.listify.ui.theme.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.listify.Graph
import com.example.listify.data.room.models.Item
import com.example.listify.data.room.models.ShoppingList
import com.example.listify.data.room.models.Store
import com.example.listify.repository.Repository
import com.example.listify.ui.theme.Category
import com.example.listify.ui.theme.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date


class DetailViewModel
constructor(
    private val repository: Repository = Graph.repository,
    private val itemId: Int
): ViewModel() {

    var state by mutableStateOf(DetailState())
        private set
    init {
        getStore()
        addItemList()
        if(itemId != -1){
            viewModelScope.launch {
                repository.getItemWithStoreAndList(itemId)
                    .collectLatest {itemsWithStoreAndList ->
                        // Assuming you expect only one item for the given itemId
                        val it = itemsWithStoreAndList.firstOrNull()
                        if (it != null) {
                            state = state.copy(
                                item = it.item.itemName,
                                store = it.store.storeName,
                                date = it.item.date,
                                category = Utils.category.find {c->
                                    c.id == it.shoppingList.id
                                } ?: Category(),
                                qty = it.item.qty
                            )
                        }
                    }
            }
        }
    }
    init {
        state = if(itemId!=-1){
            state.copy(isUpdatingItem = true)
        }
        else{
            state.copy(isUpdatingItem = false)
        }
    }


    val isFieldNotEmpty: Boolean
        get() = state.item.isNotEmpty() &&
                state.store.isNotEmpty() &&
                state.qty.isNotEmpty()

    fun onItemChange(newItem: String){
        state = state.copy(item = newItem)

    }
    fun onStoreChange(newStore: String){
        state = state.copy(store = newStore)

    }
    fun onQtyChange(newqty: String){
        state = state.copy(qty = newqty)

    }
    fun onDateChange(newDate: Date){
        state = state.copy(date = newDate)

    }
    fun onCategoryChange(newValue: Category){
        state = state.copy(category = newValue)
    }

    fun onScreenDialogDismissed(newValue: Boolean){
        state = state.copy(isScreenDialogDismissed=newValue)
    }

    private fun addItemList(){
        viewModelScope.launch {
            Utils.category.forEach{
                repository.insertList(
                    ShoppingList(id = it.id, name = it.title))
            }
        }
    }

    fun updateShoppingListItem(id: Int){
        viewModelScope.launch {
            repository.updateItem(
                Item(
                    itemName = state.item,
                    listId = state.category.id,
                    date = state.date,
                    qty = state.qty,
                    storeFkId = state.storeList.find {
                        it.storeName == state.store
                    }?.id?:0,
                    isChecked = false,
                    id = id
                )
            )
        }
    }

    fun addShoppingListItem(){
        viewModelScope.launch {
            repository.insertitem(
                Item(
                    itemName = state.item,
                    listId = state.category.id,
                    date = state.date,
                    qty = state.qty,
                    storeFkId = state.storeList.find {
                        it.storeName == state.store
                    }?.id?:0,
                    isChecked = false
                )
            )
        }
    }

    fun addStore(){
        viewModelScope.launch {
            repository.insertStore(
                Store(
                    storeName = state.store,
                    listFkId = state.category.id
                )
            )
        }
    }

    fun getStore(){
        viewModelScope.launch {
            repository.store.collectLatest {
                state = state.copy(storeList = it)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(
    private val id: Int
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(itemId = id) as T
    }
}

data class DetailState(
    val storeList: List<Store> = emptyList(),
    val store: String = "",
    val item: String = "",
    val date: Date = Date(),
    val qty: String = "",
    val isScreenDialogDismissed: Boolean = true,
    val isUpdatingItem: Boolean = false,
    val category: Category = Category()
)