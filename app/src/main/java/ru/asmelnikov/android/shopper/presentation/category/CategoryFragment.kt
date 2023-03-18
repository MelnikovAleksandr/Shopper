package ru.asmelnikov.android.shopper.presentation.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.databinding.FragmentCategoryBinding
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.utils.SwipeToDelete


@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: CategoryAdapter

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            binding.apply {
                var totalPriceValue = 0
                items.map { item ->
                    totalPriceValue += item.price
                }
                totalPrice.text = "$totalPriceValue ₽"
            }
        }

        viewModel.categoryList.observe(this.viewLifecycleOwner) { category ->
            category.let {

                var countOfDoneList = 0
                category.map { category ->
                    if (category.allItems == category.doneItems && category.allItems != 0)
                        countOfDoneList++
                }
                categoryAdapter.differ.submitList(it)
                binding.apply {
                    allListsTextView.text = category.size.toString()
                    doneListsTextView.text = countOfDoneList.toString()
                    emptyList.emptyListView.isVisible = category.isEmpty()
                    floatingActionButton.setOnClickListener {
                        val action =
                            CategoryFragmentDirections.actionCategoryFragmentToAddNewCategorySheet()
                        findNavController().navigate(action)
                    }
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

        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemDelete = categoryAdapter.differ.currentList[viewHolder.adapterPosition]
                val position = viewHolder.adapterPosition
                showDeleteDialog(position, itemDelete)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)
    }

    private fun showDeleteDialog(position: Int, category: Category) {
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Удалить элемент")
        builder.setMessage("Вы уверены, что хотите удалить этот элемент?")
        builder.setIcon(R.drawable.delete_ic)
        builder.setPositiveButton("Да") { _, _ ->
            viewModel.deleteCategory(category)
            Snackbar.make(requireView(), "Удалено", Snackbar.LENGTH_SHORT)
                .setAnchorView(floating_action_button).show()
        }
        builder.setNegativeButton("Отмена") { _, _ ->
            Snackbar.make(requireView(), "Отмена удаления", Snackbar.LENGTH_SHORT)
                .setAnchorView(floating_action_button).show()
        }
        builder.setOnDismissListener {
            categoryAdapter.notifyItemChanged(position)
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun initAdapter() {
        categoryAdapter = CategoryAdapter(object : CategoryActionListener {
            override fun onItemProductsList(category: Category) {
                val action =
                    CategoryFragmentDirections.actionCategoryFragmentToItemsListFragment(category)
                findNavController().navigate(action)
            }

            override fun onEditProductList(category: Category) {
                val action =
                    CategoryFragmentDirections.actionCategoryFragmentToEditCategorySheet(category)
                findNavController().navigate(action)
            }

            override fun onCategoryDelete(category: Category) {
                showDeleteDialog(categoryAdapter.itemCount, category)
            }

            override fun onSharedList(category: Category) {
                viewModel.getAllItemsByCategory(category).observe(viewLifecycleOwner) { items ->
                    Intent(Intent.ACTION_SEND).apply {

                        val itemsListString =
                            "${category.name}:\n " + items.map { "${it.name} - ${it.count} ${it.units.lowercase()}\n" }

                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT,
                            itemsListString.replace("\\[".toRegex(), "")
                                .replace("]".toRegex(), "")
                                .replace(",".toRegex(), "")
                        )
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Список покупок : ${category.name}"
                        )
                    }.also { intent ->
                        val chooseIntent = Intent.createChooser(
                            intent, "Поделиться списком покупок"
                        )
                        startActivity(chooseIntent)
                    }
                }
            }

        })
        recycler_view.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}