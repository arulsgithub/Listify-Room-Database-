package com.example.listify.ui.theme.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listify.Graph
import com.example.listify.data.room.ItemsWithStoreAndList
import com.example.listify.data.room.models.Item
import com.example.listify.repository.Repository
import com.example.listify.ui.theme.Category
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Home(
    private val repository: Repository = Graph.repository
):ViewModel() {
    var state by mutableStateOf(HomeState())
        private set

    init {
        getItems()
    }

    private fun getItems() {
        viewModelScope.launch {
            repository.getItemsWithStoreAndList.collectLatest {
                if (it.isEmpty()) {
                    println("No items found!")
                } else {
                    println("Items loaded: ${it.size}")
                }
                state = state.copy(items = it)
            }
        }
    }

    fun deleteItems(item: Item){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun onCategoryChange(category: Category){
        state = state.copy(category = category)
        filterBy(category.id)
    }

    private fun filterBy(shoppingListId: Int){
        if(shoppingListId!=10001){
            viewModelScope.launch {
                repository.getItemWithStoreAndListById(shoppingListId)
                    .collectLatest {
                        state = state.copy(items = it)
                    }
            }
        }
    }

    fun onItemCheckedChange(item: Item, isChecked: Boolean){
        viewModelScope.launch {
            repository.updateItem(
                item = item.copy(isChecked=isChecked)
            )
        }
    }
}

data class HomeState(
    val items: List<ItemsWithStoreAndList> = emptyList(),
    val category: Category = Category(),
    val isChecked: Boolean = false

)