package ru.asmelnikov.android.shopper.domain.use_cases.items

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository

class GetItemUseCase(private val itemRepository: ItemRepository) {

    operator fun invoke(itemId: Int): Flow<Item> {
        return itemRepository.getItem(itemId)
    }
}