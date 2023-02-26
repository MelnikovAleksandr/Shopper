package ru.asmelnikov.android.shopper.presentation.items

import ru.asmelnikov.android.shopper.domain.model.Item

interface ItemActionListener {

    fun onItemEdit(item: Item)

    fun onItemEditSheet(item: Item)

}