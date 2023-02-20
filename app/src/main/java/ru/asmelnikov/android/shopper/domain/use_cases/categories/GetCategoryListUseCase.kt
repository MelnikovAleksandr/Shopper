package ru.asmelnikov.android.shopper.domain.use_cases.categories

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository

class GetCategoryListUseCase(private val categoryRepository: CategoryRepository) {

    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getCategoryList()
    }
}