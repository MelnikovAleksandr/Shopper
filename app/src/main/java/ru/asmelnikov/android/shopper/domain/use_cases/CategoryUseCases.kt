package ru.asmelnikov.android.shopper.domain.use_cases

import ru.asmelnikov.android.shopper.domain.use_cases.categories.*

data class CategoryUseCases(
    val addCategoryUseCase: AddCategoryUseCase,
    val getCategoryListUseCase: GetCategoryListUseCase,
    val deleteCategoryUseCase: DeleteCategoryUseCase,
    val editCategoryUseCase: EditCategoryUseCase,
    val getCategoryUseCase: GetCategoryUseCase
)