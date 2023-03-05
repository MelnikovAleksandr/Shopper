package ru.asmelnikov.android.shopper.presentation.category

import ru.asmelnikov.android.shopper.domain.model.Category

interface CategoryActionListener {

    fun onItemProductsList(category: Category)

    fun onEditProductList(category: Category)

    fun onCategoryDelete(category: Category)
}