package ru.asmelnikov.android.shopper.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.asmelnikov.android.shopper.data.CategoryDao
import ru.asmelnikov.android.shopper.data.CategoryRepositoryImpl
import ru.asmelnikov.android.shopper.data.ItemsDao
import ru.asmelnikov.android.shopper.data.ShopperDataBase
import ru.asmelnikov.android.shopper.domain.repository.CategoryRepository
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.categories.AddCategoryUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.categories.GetCategoryListUseCase
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
    fun provideShopperCategoryDao(appDataBase: ShopperDataBase): CategoryDao {
        return appDataBase.getCategoryDao()
    }

    @Provides
    fun provideShopperItemsDao(appDataBase: ShopperDataBase): ItemsDao {
        return appDataBase.getItemsDao()
    }

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: CategoryRepository): CategoryUseCases {
        return CategoryUseCases(

            addCategoryUseCase = AddCategoryUseCase(repository),
            getCategoryListUseCase = GetCategoryListUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: ShopperDataBase): CategoryRepository {
        return CategoryRepositoryImpl(db.getCategoryDao())
    }
}