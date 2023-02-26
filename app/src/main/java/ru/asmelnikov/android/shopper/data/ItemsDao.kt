package ru.asmelnikov.android.shopper.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.Item

@Dao
interface ItemsDao {

    @Query("SELECT* FROM item WHERE categoryId = :categoryId ORDER BY bought")
    fun getItemsList(categoryId: Int): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id - :id")
    fun getItem(id: Int): Flow<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun editItem(item: Item)

}