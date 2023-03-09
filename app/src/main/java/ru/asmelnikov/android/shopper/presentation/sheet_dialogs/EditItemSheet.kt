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
import ru.asmelnikov.android.shopper.databinding.FragmentEditItemSheetBinding
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.items.ItemsViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter
import kotlin.math.roundToInt

@AndroidEntryPoint
class EditItemSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentEditItemSheetBinding? = null
    private val binding get() = _binding!!

    private val args: EditItemSheetArgs by navArgs()

    private val viewModel: ItemsViewModel by viewModels()

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditItemSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var countOfItems = args.item.count.toInt()

        binding.apply {
            itemNameEditText.setText(args.item.name)
            itemsCountTextView.text = countOfItems.toString()
            itemCostEditText.setText(args.item.price.toString())
            dropDownAutoComplete.setText(args.item.units)

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

            slider.value = roundedTo100(countOfItems)

            slider.addOnChangeListener(Slider.OnChangeListener { _, value, _ ->
                itemsCountTextView.text = value.toInt().toString()
                countOfItems = value.toInt()
            })

            viewModel.wordsList.observe(viewLifecycleOwner) { wordsList ->

                wordsList.let {
                    val adapter = WordsCompleterAdapter(requireContext(), wordsList)
                    itemNameEditText.setAdapter(adapter)
                    editButton.setOnClickListener {
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
                                cost = costItem.toFloat(),
                                unit = units
                            )
                            editItem(item)
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
        Toast.makeText(context, "Please enter all information", Toast.LENGTH_SHORT).show()
    }

    private fun createItem(nameItem: String, countItem: String, cost: Float, unit: String): Item {
        return Item(
            id = args.item.id,
            name = nameItem,
            count = countItem.toFloat(),
            bought = args.item.bought,
            categoryId = args.item.categoryId,
            price = cost,
            units = unit
        )
    }

    private fun createWord(word: String): WordsForAutoComplete {
        return WordsForAutoComplete(word = word)
    }

    private fun editItem(item: Item) {
        viewModel.editItem(item)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun roundedTo100(from: Int) = ((from / 100).toDouble().roundToInt() * 100).toFloat()

}