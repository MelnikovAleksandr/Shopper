package ru.asmelnikov.android.shopper.presentation.items

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.ItemsUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.WordsUseCases
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    private val itemsUseCases: ItemsUseCases,
    private val categoryUseCase: CategoryUseCases,
    private val wordsUseCases: WordsUseCases,
    state: SavedStateHandle
) : ViewModel() {

    val category = state.get<Category>(CATEGORY_STATE_KEY)

    val allItems: LiveData<List<Item>> =
        itemsUseCases.getItemsListUseCase(category?.id ?: 0).asLiveData()

    val wordsList: LiveData<List<WordsForAutoComplete>> =
        wordsUseCases.getAllWordsUseCase().asLiveData()

    fun insertNewWord(word: WordsForAutoComplete) {
        viewModelScope.launch {
            wordsUseCases.insertWordsUseCase(word)
        }
    }

    fun insertItem(item: Item) {
        viewModelScope.launch {
            itemsUseCases.addItemUseCase(item)
        }
    }

    fun editItem(item: Item) {
        viewModelScope.launch {
            itemsUseCases.editItemUseCase(item)
        }
    }

    fun updateCategoryDoneItemsValue(category: Category, bought: Boolean) {
        category.apply {
            if (bought) doneItems += 1 else doneItems -= 1
        }
        updateCategoryList(category)
    }

    fun deleteItemOnSwipe(item: Item, category: Category) = viewModelScope.launch {
        itemsUseCases.deleteItemUseCase(item)
        updateCategoryListOnItemsSwipe(category, item.bought)
    }

    fun undoDeletedItem(item: Item, category: Category) = viewModelScope.launch {
        itemsUseCases.addItemUseCase(item)
        updateCategoryListOnItemsSwipeUndo(category, item.bought)
    }

    fun updateInsertCategoryAllItemsAmount(category: Category) {
        category.apply { allItems += 1 }
        updateCategoryList(category)
    }

    private fun updateCategoryList(category: Category) = viewModelScope.launch {
        categoryUseCase.editCategoryUseCase(category)
    }

    private fun updateCategoryListOnItemsSwipe(
        category: Category,
        bought: Boolean
    ) {
        category.apply {
            if (bought) {
                allItems -= 1
                doneItems -= 1
            } else {
                allItems -= 1
            }
        }
        updateCategoryList(category)
    }

    private fun updateCategoryListOnItemsSwipeUndo(
        category: Category,
        bought: Boolean
    ) {
        category.apply {
            if (bought) {
                allItems += 1
                doneItems += 1
            } else {
                allItems += 1
            }
        }
        updateCategoryList(category)
    }

    companion object {
        private const val CATEGORY_STATE_KEY = "category"
    }
}