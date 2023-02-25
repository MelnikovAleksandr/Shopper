package ru.asmelnikov.android.shopper.domain.use_cases.categories

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository

class GetCategoryUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(id: Int): Flow<Category> {
        return categoryRepository.getCategory(id)
    }
}