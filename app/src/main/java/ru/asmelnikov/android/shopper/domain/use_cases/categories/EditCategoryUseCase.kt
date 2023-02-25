package ru.asmelnikov.android.shopper.domain.use_cases.categories

import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository

class EditCategoryUseCase(private val categoryRepository: CategoryRepository) {

    suspend operator fun invoke(category: Category) {
        categoryRepository.editCategory(category)
    }
}