package ru.asmelnikov.android.shopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Category

interface CategoryRepository {

    suspend fun addCategory(category: Category)

    fun getCategoryList(): Flow<List<Category>>

    suspend fun deleteCategory(category: Category)

    suspend fun editCategory(category: Category)

    fun getCategory(id: Int): Flow<Category>
}