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
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.databinding.FragmentNewItemSheetBinding
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.items.ItemsViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter

@AndroidEntryPoint
class AddNewItemSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentNewItemSheetBinding? = null
    private val binding get() = _binding!!

    private val navArgs: AddNewItemSheetArgs by navArgs()

    private val viewModel: ItemsViewModel by viewModels()

    private lateinit var category: Category

    private var countOfItems = 1

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewItemSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        category = navArgs.category

        binding.apply {
            itemsCountTextView.text = countOfItems.toString()

            plusCountButton.setOnClickListener {
                if (countOfItems < 1000)
                    countOfItems++
                itemsCountTextView.text = countOfItems.toString()
            }

            minusCountButton.setOnClickListener {
                if (countOfItems > 1)
                    countOfItems--
                itemsCountTextView.text = countOfItems.toString()
            }


            slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
                itemsCountTextView.text = value.toInt().toString()
                countOfItems = value.toInt()

            })
            viewModel.wordsList.observe(viewLifecycleOwner) { wordsList ->
                wordsList.let {
                    val adapter = WordsCompleterAdapter(requireContext(), wordsList)
                    itemNameEditText.setAdapter(adapter)

                    addButton.setOnClickListener {
                        val nameItem = itemNameEditText.text.toString()
                        val countItem = itemsCountTextView.text.toString()
                        var costItem = itemCostEditText.text.toString()
                        val units = dropDownAutoComplete.text.toString()

                        val word = createWord(nameItem)
                        if (!wordsList.contains(word)) viewModel.insertNewWord(word)

                        if (nameItem.isEmpty() || countItem.isEmpty()) {
                            showErrorToast()
                        } else {
                            costItem = costItem.ifEmpty { "0" }
                            val item = createItem(
                                nameItem = nameItem,
                                countItem = countItem,
                                cost = costItem.toInt(),
                                unit = units
                            )
                            addItem(item)
                            viewModel.updateInsertCategoryAllItemsAmount(category)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val categoriesDropDown = resources.getStringArray(R.array.units)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, categoriesDropDown)
        binding.dropDownAutoComplete.setAdapter(arrayAdapter)
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Please enter all info", Toast.LENGTH_LONG).show()
    }

    private fun addItem(item: Item) {
        viewModel.insertItem(item)
        dismiss()
    }

    private fun createWord(word: String): WordsForAutoComplete {
        return WordsForAutoComplete(word = word)
    }

    private fun createItem(nameItem: String, countItem: String, cost: Int, unit: String): Item {
        return Item(
            id = 0,
            name = nameItem,
            count = countItem.toInt(),
            bought = false,
            categoryId = navArgs.category.id,
            price = cost,
            units = unit
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}