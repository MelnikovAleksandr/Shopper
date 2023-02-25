package ru.asmelnikov.android.shopper.domain.use_cases.items

import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository

class EditItemUseCase(private val itemRepository: ItemRepository) {

    suspend operator fun invoke(item: Item) {
        itemRepository.editItem(item)
    }
}