package ru.asmelnikov.android.shopper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete

@Database(
    entities = [Item::class, Category::class, WordsForAutoComplete::class],
    version = 1,
    exportSchema = true
)
abstract class ShopperDataBase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getItemsDao(): ItemsDao
    abstract fun getWordsDao(): WordsDao

}