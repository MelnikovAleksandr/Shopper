package ru.asmelnikov.android.shopper.presentation.sheet_dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.databinding.FragmentAddNewCategorySheetBinding
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.category.CategoryViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter

@AndroidEntryPoint
class AddNewCategorySheet : BottomSheetDialogFragment() {

    private var _binding: FragmentAddNewCategorySheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModels()

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewCategorySheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordsList.observe(this.viewLifecycleOwner) { wordsList ->

            wordsList.let {

                val adapter = WordsCompleterAdapter(requireContext(), wordsList)

                binding.apply {
                    categoryNameEditText.setAdapter(adapter)
                    addButton.setOnClickListener {

                        val nameCategory = binding.categoryNameEditText.text.toString()
                        val categoryDrop = binding.dropDownAutoComplete.text.toString()

                        val word = createWord(nameCategory)
                        if (!wordsList.contains(word)) viewModel.insertNewWord(word)

                        if (nameCategory.isEmpty()) {
                            showErrorToast()
                        } else {
                            val category = createCategory(nameCategory, categoryDrop)
                            addCategory(category)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val categoriesDropDown = resources.getStringArray(R.array.categories)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, categoriesDropDown)
        binding.dropDownAutoComplete.setAdapter(arrayAdapter)
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Please enter all information", Toast.LENGTH_SHORT).show()
    }

    private fun createCategory(nameCategory: String, category: String): Category {
        return Category(
            id = 0,
            name = nameCategory,
            allItems = 0,
            doneItems = 0,
            category = category
        )
    }

    private fun createWord(word: String): WordsForAutoComplete {
        return WordsForAutoComplete(word = word)
    }

    private fun addCategory(category: Category) {
        viewModel.insertCategory(category)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}