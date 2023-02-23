package ru.asmelnikov.android.shopper.presentation.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    ): View? {
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

        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemDelete = categoryAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.deleteCategory(itemDelete)
                view?.let {
                    Snackbar.make(it, "Deleted", Snackbar.LENGTH_SHORT).apply {
                        setAction("Undo") {
                            viewModel.insertCategory(itemDelete)
                        }
                        show()
                    }
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}