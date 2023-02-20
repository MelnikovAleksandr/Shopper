package ru.asmelnikov.android.shopper.domain.use_cases

import ru.asmelnikov.android.shopper.domain.use_cases.categories.AddCategoryUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.categories.GetCategoryListUseCase

data class CategoryUseCases(
    val addCategoryUseCase: AddCategoryUseCase,
    val getCategoryListUseCase: GetCategoryListUseCase
)