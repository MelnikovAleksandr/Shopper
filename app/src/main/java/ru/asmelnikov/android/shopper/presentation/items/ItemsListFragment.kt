package ru.asmelnikov.android.shopper.presentation.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import ru.asmelnikov.android.shopper.databinding.FragmentItemListBinding
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.utils.SwipeToDelete

@AndroidEntryPoint
class ItemsListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private val navArgs: ItemsListFragmentArgs by navArgs()

    private lateinit var itemsAdapter: ItemsAdapter

    private val viewModel: ItemsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.categoryNameText.text = navArgs.category.name

        viewModel.allItems.observe(this.viewLifecycleOwner) {
            it.let {
                itemsAdapter.differ.submitList(it)
            }
        }

        binding.addNewItemButton.setOnClickListener {
            val action =
                ItemsListFragmentDirections.actionItemsListFragmentToAddNewItemSheet(navArgs.category)
            findNavController().navigate(action)
        }

    }

    private fun initAdapter() {
        itemsAdapter = ItemsAdapter(object : ItemActionListener {
            override fun onItemEdit(item: Item) {
                viewModel.editItem(item)
                viewModel.updateCategoryDoneItemsValue(navArgs.category, item.bought)
            }
        })
        recycler_view.apply {
            adapter = itemsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemDelete = itemsAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.deleteItemOnSwipe(itemDelete, navArgs.category)
                view?.let {
                    Snackbar.make(it, "Deleted", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            viewModel.undoDeletedItem(itemDelete, navArgs.category)
                        }
                        show()
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}