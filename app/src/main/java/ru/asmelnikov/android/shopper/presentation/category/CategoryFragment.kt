package ru.asmelnikov.android.shopper.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*
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

        viewModel.categoryList.observe(this.viewLifecycleOwner) { category ->
            category.let {
                categoryAdapter.differ.submitList(it)
            }
        }

        binding.addNewCategoryButton.setOnClickListener {
            val action = CategoryFragmentDirections.actionCategoryFragmentToAddNewCategorySheet()
            findNavController().navigate(action)
        }

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
        builder.setPositiveButton("Да") { _, _ ->
            viewModel.deleteCategory(category)
            Snackbar.make(requireView(), "Deleted", Snackbar.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Отмена") { _, _ ->
            categoryAdapter.notifyItemChanged(position)
            Snackbar.make(requireView(), "Undelete", Snackbar.LENGTH_SHORT).show()
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