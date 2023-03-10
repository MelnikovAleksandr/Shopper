package ru.asmelnikov.android.shopper.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import ru.asmelnikov.android.shopper.domain.use_cases.WordsUseCases
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val wordsUseCases: WordsUseCases
) : ViewModel() {

    val categoryList: LiveData<List<Category>> =
        categoryUseCases.getCategoryListUseCase().asLiveData()

    val wordsList: LiveData<List<WordsForAutoComplete>> =
        wordsUseCases.getAllWordsUseCase().asLiveData()

    fun insertNewWord(word: WordsForAutoComplete) {
        viewModelScope.launch {
            wordsUseCases.insertWordsUseCase(word)
        }
    }

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            categoryUseCases.addCategoryUseCase(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryUseCases.deleteCategoryUseCase(category)
        }
    }

    fun editCategory(category: Category) {
        viewModelScope.launch {
            categoryUseCases.editCategoryUseCase(category)
        }
    }
}