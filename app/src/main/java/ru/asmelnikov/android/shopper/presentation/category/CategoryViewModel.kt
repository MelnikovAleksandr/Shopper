package ru.asmelnikov.android.shopper.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.use_cases.CategoryUseCases
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    val categoryList: LiveData<List<Category>> =
        categoryUseCases.getCategoryListUseCase().asLiveData()

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

}