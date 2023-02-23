package ru.asmelnikov.android.shopper.data

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository

class ItemsRepositoryImpl(private val itemsDao: ItemsDao) : ItemRepository {
    override fun getItemsList(categoryId: Int): Flow<List<Item>> {
        return itemsDao.getItemsList(categoryId)
    }

    override suspend fun addItem(item: Item) {
        itemsDao.addItem(item)
    }

    override suspend fun deleteItem(item: Item) {
        itemsDao.deleteItem(item)
    }

}