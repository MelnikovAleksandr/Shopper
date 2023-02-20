package ru.asmelnikov.android.shopper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategoryList(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

}