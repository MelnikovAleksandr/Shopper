package ru.asmelnikov.android.shopper.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.asmelnikov.android.shopper.data.CategoryRepositoryImpl
import ru.asmelnikov.android.shopper.data.ItemsRepositoryImpl
import ru.asmelnikov.android.shopper.data.ShopperDataBase
import ru.asmelnikov.android.shopper.data.WordsRepositoryImpl
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository
import ru.asmelnikov.android.shopper.domain.repository.WordsRepository
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.ItemsUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.WordsUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.categories.*
import ru.asmelnikov.android.shopper.domain.use_cases.items.*
import ru.asmelnikov.android.shopper.domain.use_cases.words.InsertWordsUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.words.GetAllWordsUseCase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShopperDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ShopperDataBase::class.java,
            "shopper_database"
        ).build()

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(

            addCategoryUseCase = AddCategoryUseCase(repository),
            getCategoryListUseCase = GetCategoryListUseCase(repository),
            deleteCategoryUseCase = DeleteCategoryUseCase(repository),
            editCategoryUseCase = EditCategoryUseCase(repository),
            getCategoryUseCase = GetCategoryUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideItemsUseCases(repository: ItemRepository): ItemsUseCases {
        return ItemsUseCases(
            getItemsListUseCase = GetItemsListUseCase(repository),
            addItemUseCase = AddItemUseCase(repository),
            deleteItemUseCase = DeleteItemUseCase(repository),
            getItemUseCase = GetItemUseCase(repository),
            editItemUseCase = EditItemUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWordsUseCases(repository: WordsRepository): WordsUseCases {
        return WordsUseCases(
            insertWordsUseCase = InsertWordsUseCase(repository),
            getAllWordsUseCase = GetAllWordsUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: ShopperDataBase): CategoryRepository {
        return CategoryRepositoryImpl(db.getCategoryDao())
    }

    @Provides
    @Singleton
    fun provideItemsRepository(db: ShopperDataBase): ItemRepository {
        return ItemsRepositoryImpl(db.getItemsDao())
    }

    @Provides
    @Singleton
    fun provideWordsRepository(db: ShopperDataBase): WordsRepository {
        return WordsRepositoryImpl(db.getWordsDao())
    }
}