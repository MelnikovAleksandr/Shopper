package ru.asmelnikov.android.shopper.presentation.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.floating_action_button
import kotlinx.android.synthetic.main.fragment_category.recycler_view
import kotlinx.android.synthetic.main.fragment_item_list.*
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.databinding.FragmentItemListBinding
import ru.asmelnikov.android.shopper.domain.model.Item
import ru.asmelnikov.android.shopper.utils.SwipeToDelete
import ru.asmelnikov.android.shopper.utils.imageBinding

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

        viewModel.allItemsByCategory.observe(this.viewLifecycleOwner) { items ->

            binding.apply {
                var totalPrice = 0
                itemsAdapter.differ.submitList(items)
                items.map { item ->
                    totalPrice += item.price
                }
                costTextView.text = "$totalPrice ???"
                allCheckBox.isChecked =
                    navArgs.category.doneItems == navArgs.category.allItems && navArgs.category.allItems != 0

                allCheckBox.setOnClickListener {
                    it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
                    if (all_check_box.isChecked) {
                        items.map { item ->
                            if (!item.bought) {
                                item.bought = true
                                viewModel.updateCategoryDoneItemsValue(
                                    navArgs.category,
                                    item.bought
                                )
                            }
                            itemsAdapter.notifyDataSetChanged()
                            viewModel.editItem(item)
                        }
                    } else {
                        items.map { item ->
                            if (item.bought) {
                                item.bought = false
                                viewModel.updateCategoryDoneItemsValue(
                                    navArgs.category,
                                    item.bought
                                )
                            }
                            itemsAdapter.notifyDataSetChanged()
                            viewModel.editItem(item)
                        }
                    }
                }
            }
        }

        viewModel.categoryList.observe(this.viewLifecycleOwner) {
            binding.apply {
                floatingActionButton.setOnClickListener {
                    val action =
                        ItemsListFragmentDirections.actionItemsListFragmentToAddNewItemSheet(
                            navArgs.category
                        )
                    findNavController().navigate(action)
                }
                imageBinding(categoryImgView, navArgs.category)
                categoryTextView.text = navArgs.category.name
                categoryNameTextView.text = navArgs.category.category
                itemsCount.text = "${navArgs.category.doneItems}/${navArgs.category.allItems}"
                progressIndicator.max = navArgs.category.allItems
                progressIndicator.progress = navArgs.category.doneItems
                progressText.text = "${navArgs.category.doneItems}/${navArgs.category.allItems}"
                if (navArgs.category.doneItems == navArgs.category.allItems && navArgs.category.allItems != 0) {
                    progressIndicator.setIndicatorColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.teal_700
                        )
                    )
                    itemsCount.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.slate_grey
                        )
                    )
                } else {
                    progressIndicator.setIndicatorColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.amaranth_purple
                        )
                    )
                    itemsCount.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.amaranth_purple
                        )
                    )
                }
            }
        }
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> floating_action_button.show()
                    else -> floating_action_button.hide()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }

        recycler_view.clearOnScrollListeners()
        recycler_view.addOnScrollListener(scrollListener)
    }

    private fun showDeleteDialog(position: Int, item: Item) {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("?????????????? ??????????????")
        builder.setMessage("???? ??????????????, ?????? ???????????? ?????????????? ???????? ???????????????")
        builder.setIcon(R.drawable.delete_ic)
        builder.setPositiveButton("????") { _, _ ->
            viewModel.deleteItemOnSwipe(item, navArgs.category)
            Snackbar.make(requireView(), "?????????????? ??????????????", Snackbar.LENGTH_SHORT).setAnchorView(floating_action_button).show()
        }
        builder.setNegativeButton("????????????") { _, _ ->
            Snackbar.make(requireView(), "???????????? ????????????????", Snackbar.LENGTH_SHORT).setAnchorView(floating_action_button).show()
        }
        builder.setOnDismissListener {
            itemsAdapter.notifyItemChanged(position)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initAdapter() {
        itemsAdapter = ItemsAdapter(object : ItemActionListener {
            override fun onItemEdit(item: Item) {
                viewModel.editItem(item)
                viewModel.updateCategoryDoneItemsValue(navArgs.category, item.bought)
            }

            override fun onItemEditSheet(item: Item) {
                val action =
                    ItemsListFragmentDirections.actionItemsListFragmentToEditItemSheet(
                        navArgs.category, item
                    )
                findNavController().navigate(action)
            }

            override fun onItemDelete(item: Item) {
                showDeleteDialog(itemsAdapter.itemCount, item)
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
                    Snackbar.make(it, "??????????????", Snackbar.LENGTH_SHORT)
                        .setAnchorView(floating_action_button)
                        .setAction("????????????") {
                            viewModel.undoDeletedItem(itemDelete, navArgs.category)
                        }
                        .show()

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