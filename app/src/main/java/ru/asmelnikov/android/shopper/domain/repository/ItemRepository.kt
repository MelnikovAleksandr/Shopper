package ru.asmelnikov.android.shopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Item

interface ItemRepository {

    fun getItemsList(categoryId: Int): Flow<List<Item>>

    fun getAllItemsList(): Flow<List<Item>>

    suspend fun addItem(item: Item)

    suspend fun deleteItem(item: Item)

    fun getItem(itemId: Int): Flow<Item>

    suspend fun editItem(item: Item)
}