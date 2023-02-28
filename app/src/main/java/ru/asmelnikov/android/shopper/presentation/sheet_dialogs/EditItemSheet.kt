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
import kotlinx.android.synthetic.main.layout_count_selector.*
import ru.asmelnikov.android.shopper.databinding.FragmentEditItemSheetBinding
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.presentation.items.ItemsViewModel
import ru.asmelnikov.android.shopper.utils.WordsCompleterAdapter

@AndroidEntryPoint
class EditItemSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentEditItemSheetBinding? = null
    private val binding get() = _binding!!

    private val args: EditItemSheetArgs by navArgs()

    private val viewModel: ItemsViewModel by viewModels()

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

        var countOfItems = args.item.count

        plus_image_view.setOnClickListener {
            if (countOfItems < 99)
                countOfItems++
            count_text_view.text = countOfItems.toString()
        }

        minus_image_view.setOnClickListener {
            if (countOfItems > 1)
                countOfItems--
            count_text_view.text = countOfItems.toString()
        }


        binding.apply {
            itemNameEditTextView.setText(args.item.name)
            countView.countTextView.text = args.item.count.toString()
        }

        viewModel.wordsList.observe(this.viewLifecycleOwner) { wordsList ->

            wordsList.let {
                val adapter = WordsCompleterAdapter(requireContext(), wordsList)
                binding.itemNameEditTextView.setAdapter(adapter)
                binding.addItemButton.setOnClickListener {
                    val nameItem = binding.itemNameEditTextView.text.toString()
                    val countItem = binding.countView.countTextView.text.toString()

                    val word = createWord(nameItem)
                    if (!wordsList.contains(word)) viewModel.insertNewWord(word)

                    if (nameItem.isEmpty() || countItem.isEmpty()) {
                        showErrorToast()
                    } else {
                        val item = createItem(nameItem, countItem)
                        editItem(item)
                    }
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Please enter all information", Toast.LENGTH_SHORT).show()
    }

    private fun createItem(nameItem: String, countItem: String): Item {
        return Item(
            id = args.item.id,
            name = nameItem,
            count = countItem.toFloat(),
            bought = args.item.bought,
            categoryId = args.item.categoryId,
            price = 0F
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

}