package com.example.practica_1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.practica_1.data.entity.Item
import com.example.practica_1.repository.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: ItemRepository) : ViewModel() {
    val allItems: StateFlow<List<Item>> = repository.allItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var selectedItem by mutableStateOf<Item?>(null)
        private set

    fun selectItem(item: Item) { selectedItem = item }
    fun clearSelectedItem() { selectedItem = null }

    fun insertItem(name: String, description: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            val item = Item(name = name, description = description, quantity = quantity, price = price)
            repository.insert(item)
        }
    }

    fun updateItem(id: Int, name: String, description: String, quantity: Int, price: Double) {
        viewModelScope.launch {
            val item = Item(id, name, description, quantity, price)
            repository.update(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}

class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}