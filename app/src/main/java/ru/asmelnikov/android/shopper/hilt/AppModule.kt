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
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository
import ru.asmelnikov.android.shopper.domain.repository.ItemRepository
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.ItemsUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.categories.*
import ru.asmelnikov.android.shopper.domain.use_cases.items.AddItemUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.items.DeleteItemUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.items.GetItemUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.items.GetItemsListUseCase
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
            getItemUseCase = GetItemUseCase(repository)
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
}