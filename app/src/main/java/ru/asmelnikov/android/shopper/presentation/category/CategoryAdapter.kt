package ru.asmelnikov.android.shopper.presentation.category

import android.graphics.Paint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_category.view.*
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.utils.imageBinding

class CategoryAdapter(private val categoryActionListener: CategoryActionListener) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val callBack = object : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.itemView.apply {

            val animation: Animation =
                android.view.animation.AnimationUtils.loadAnimation(context, R.anim.anim)
            startAnimation(animation)

            imageBinding(category_img, category, context)

            progress_indicator.max = category.allItems
            progress_indicator.progress = category.doneItems
            category_item_name.text = category.name
            progress_text_view.text = "${category.doneItems}/${category.allItems}"
            if (category.doneItems == category.allItems && category.allItems != 0) {
                progress_indicator.setIndicatorColor(
                    ContextCompat.getColor(
                        context,
                        R.color.indicator_done_green
                    )
                )
                category_item_name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                progress_indicator.setIndicatorColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bright_pink
                    )
                )
                category_item_name.paintFlags = 0
            }

            menu_button.setOnClickListener {
                popupMenu(it, category)
            }

            setOnClickListener {
                categoryActionListener.onItemProductsList(category)
            }

            setOnLongClickListener {
                categoryActionListener.onEditProductList(category)
                true
            }

            shared_button.setOnClickListener {
                categoryActionListener.onSharedList(category)
            }
        }
    }

    private fun popupMenu(view: View, category: Category) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.category_item_popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_category -> {
                    categoryActionListener.onEditProductList(category)
                    true
                }
                R.id.delete_category -> {
                    categoryActionListener.onCategoryDelete(category)
                    true
                }
                else -> false
            }
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.gravity = Gravity.END
        popupMenu.show()
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}