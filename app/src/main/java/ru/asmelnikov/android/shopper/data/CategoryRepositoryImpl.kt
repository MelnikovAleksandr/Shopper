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

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun editCategory(category: Category) {
        categoryDao.editCategory(category)
    }

    override fun getCategory(id: Int): Flow<Category> {
        return categoryDao.getCategory(id)
    }
}