package ru.asmelnikov.android.shopper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item

@Database(
    entities = [Item::class, Category::class],
    version = 1,
    exportSchema = true
)
abstract class ShopperDataBase : RoomDatabase() {

    abstract fun getCategoryDao() : CategoryDao
    abstract fun getItemsDao() : ItemsDao

}