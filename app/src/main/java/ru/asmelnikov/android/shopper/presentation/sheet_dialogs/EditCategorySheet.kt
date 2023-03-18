package ru.asmelnikov.android.shopper.presentation.sheet_dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.databinding.FragmentEditCategorySheetBinding
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.category.CategoryViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter

@AndroidEntryPoint
class EditCategorySheet : BottomSheetDialogFragment() {

    private var _binding: FragmentEditCategorySheetBinding? = null
    private val binding get() = _binding!!

    private val args: EditCategorySheetArgs by this.navArgs()

    private val viewModel: CategoryViewModel by viewModels()

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCategorySheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.wordsList.observe(this.viewLifecycleOwner) { wordsList ->

            wordsList.let {

                val adapter = WordsCompleterAdapter(requireContext(), wordsList)
                val categoriesDropDown = resources.getStringArray(R.array.categories)
                val arrayAdapter =
                    ArrayAdapter(requireContext(), R.layout.dropdown_item, categoriesDropDown)

                binding.apply {
                    dropDownAutoComplete.setText(args.category.category)
                    dropDownAutoComplete.setAdapter(arrayAdapter)

                    categoryNameEditText.setAdapter(adapter)
                    categoryNameEditText.setText(args.category.name)
                    addButton.setOnClickListener {

                        val nameCategory = binding.categoryNameEditText.text.toString()
                        val categoryDrop = binding.dropDownAutoComplete.text.toString()

                        val word = createWord(nameCategory)
                        if (!wordsList.contains(word)) viewModel.insertNewWord(word)

                        if (nameCategory.isEmpty()) {
                            showErrorToast()
                        } else {
                            val category = createCategory(args.category, nameCategory, categoryDrop)
                            addCategory(category)
                        }
                    }
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(context, getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show()
    }

    private fun createCategory(
        category: Category,
        nameCategory: String,
        categoryName: String
    ): Category {
        return Category(
            id = category.id,
            name = nameCategory,
            allItems = category.allItems,
            doneItems = category.doneItems,
            category = categoryName
        )
    }

    private fun createWord(word: String): WordsForAutoComplete {
        return WordsForAutoComplete(word = word)
    }

    private fun addCategory(category: Category) {
        viewModel.editCategory(category)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}