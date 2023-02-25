package ru.asmelnikov.android.shopper.domain.use_cases

import ru.asmelnikov.android.shopper.domain.use_cases.items.*

data class ItemsUseCases(
    val getItemsListUseCase: GetItemsListUseCase,
    val deleteItemUseCase: DeleteItemUseCase,
    val addItemUseCase: AddItemUseCase,
    val getItemUseCase: GetItemUseCase,
    val editItemUseCase: EditItemUseCase
)