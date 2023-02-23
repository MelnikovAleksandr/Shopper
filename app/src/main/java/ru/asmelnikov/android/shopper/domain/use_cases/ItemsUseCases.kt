package ru.asmelnikov.android.shopper.domain.use_cases

import ru.asmelnikov.android.shopper.domain.use_cases.items.AddItemUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.items.DeleteItemUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.items.GetItemsListUseCase

data class ItemsUseCases(
    val getItemsListUseCase: GetItemsListUseCase,
    val deleteItemUseCase: DeleteItemUseCase,
    val addItemUseCase: AddItemUseCase
)