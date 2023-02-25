package ru.asmelnikov.android.shopper.presentation.items

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.ItemsUseCases
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    private val categoryUseCase: CategoryUseCases,
    state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>(CATEGORY_STATE_KEY)

    val allItems: LiveData<List<Item>> =
        itemsUseCases.getItemsListUseCase(category?.id ?: 0).asLiveData()

    fun insertItem(item: Item) {
        viewModelScope.launch {
            itemsUseCases.addItemUseCase(item)
        }
    }

    fun updateCategoryItemsAmount(category: Category) {
        category.apply { allItems += 1 }
        viewModelScope.launch {
            categoryUseCase.editCategoryUseCase(category)
        }
    }

    fun deleteItem(item: Item, category: Category) {
        viewModelScope.launch {
            itemsUseCases.deleteItemUseCase(item)
        }
        updateCategoryItemAfterDelete(category)
    }

    private fun updateCategoryItemAfterDelete(category: Category) {
        category.apply { allItems -= 1 }
        viewModelScope.launch {
            categoryUseCase.editCategoryUseCase(category)
        }
    }


    companion object {
        private const val CATEGORY_STATE_KEY = "category"
    }
}