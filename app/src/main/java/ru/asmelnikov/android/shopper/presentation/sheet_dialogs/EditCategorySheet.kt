package ru.asmelnikov.android.shopper.presentation.sheet_dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.shopper.databinding.FragmentEditCategorySheetBinding
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.category.CategoryViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter

@AndroidEntryPoint
class EditCategorySheet : BottomSheetDialogFragment() {

    private var _binding: FragmentEditCategorySheetBinding? = null
    private val binding get() = _binding!!

    private val args: EditCategorySheetArgs by navArgs()

    private val viewModel: CategoryViewModel by viewModels()

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

        binding.categoryNameEditText.setText(args.category.name)

        viewModel.wordsList.observe(this.viewLifecycleOwner) { wordsList ->

            wordsList.let {

                val adapter = WordsCompleterAdapter(requireContext(), wordsList)

                binding.categoryNameEditText.setAdapter(adapter)

                binding.addButton.setOnClickListener {

                    val nameCategory = binding.categoryNameEditText.text.toString()

                    val word = createWord(nameCategory)
                    if (!wordsList.contains(word)) viewModel.insertNewWord(word)

                    if (nameCategory.isEmpty()) {
                        showErrorToast()
                    } else {
                        val category = createCategory(args.category, nameCategory)
                        addCategory(category)
                    }
                }
            }
        }

    }

    private fun showErrorToast() {
        Toast.makeText(context, "Please enter all information", Toast.LENGTH_SHORT).show()
    }

    private fun createCategory(category: Category, nameCategory: String): Category {
        return Category(
            id = category.id,
            name = nameCategory,
            allItems = category.allItems,
            doneItems = category.doneItems
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