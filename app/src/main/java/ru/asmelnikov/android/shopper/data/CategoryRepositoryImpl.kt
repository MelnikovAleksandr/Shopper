package ru.asmelnikov.android.shopper.data

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun addCategory(category: Category) {
        categoryDao.addCategory(category)
    }

    override fun getCategoryList(): Flow<List<Category>> {
        return categoryDao.getCategoryList()
    }
}