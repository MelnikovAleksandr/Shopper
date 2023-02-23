package ru.asmelnikov.android.shopper.domain.use_cases.items

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository

class GetItemsListUseCase(private val itemRepository: ItemRepository) {

    operator fun invoke(categoryId: Int): Flow<List<Item>> {
        return itemRepository.getItemsList(categoryId)
    }

}