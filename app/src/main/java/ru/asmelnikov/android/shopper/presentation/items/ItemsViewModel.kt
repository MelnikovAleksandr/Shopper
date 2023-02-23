package ru.asmelnikov.android.shopper.presentation.items

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.use_cases.ItemsUseCases
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
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

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemsUseCases.deleteItemUseCase(item)
        }
    }


    companion object {
        private const val CATEGORY_STATE_KEY = "category"
    }
}