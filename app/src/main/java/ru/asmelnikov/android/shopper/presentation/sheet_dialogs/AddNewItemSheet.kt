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
import ru.asmelnikov.android.shopper.databinding.FragmentNewItemSheetBinding
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.presentation.items.ItemsViewModel

@AndroidEntryPoint
class AddNewItemSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentNewItemSheetBinding? = null
    private val binding get() = _binding!!

    private val navArgs: AddNewItemSheetArgs by navArgs()

    private val viewModel: ItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewItemSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addItemButton.setOnClickListener {
            val nameItem = binding.itemNameEditTextView.text.toString()
            val countItem = binding.countItemEditTextView.text.toString()

            if (nameItem.isEmpty() || countItem.isEmpty()) {
                showErrorToast()
            } else {
                val item = createItem(nameItem, countItem)
                addItem(item)
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(context, "Please enter all info", Toast.LENGTH_LONG).show()
    }

    private fun addItem(item: Item) {
        viewModel.insertItem(item)
        dismiss()
    }

    private fun createItem(nameItem: String, countItem: String): Item {
        return Item(
            id = 0,
            name = nameItem,
            count = countItem.toFloat(),
            bought = false,
            categoryId = navArgs.category.id,
            price = 0F
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}