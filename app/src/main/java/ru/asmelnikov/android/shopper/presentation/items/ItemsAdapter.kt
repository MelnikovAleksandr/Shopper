package ru.asmelnikov.android.shopper.presentation.items

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_items_category.view.*
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.Item

class ItemsAdapter(
    private val itemActionListener: ItemActionListener
) :
    RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val callBack = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        return ItemsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_items_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            item_name_text_view.text = item.name
            item_count_text_view.text = item.count.toInt().toString()
            item_cost_text_view.text = "${item.price} ₽"
            item_units_text_view.text = item.units
            checkbox.isChecked = item.bought

            menu_button.setOnClickListener {
                popupMenu(it, item)
            }

            checkbox.setOnClickListener {
                it?.apply { isEnabled = false; postDelayed({ isEnabled = true }, 400) }
                item.bought = checkbox.isChecked
                itemActionListener.onItemEdit(item)

            }

            main_item.setOnLongClickListener {
                itemActionListener.onItemEditSheet(item)
                true
            }
        }
    }

    private fun popupMenu(view: View, item: Item) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.category_item_popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_category -> {
                    itemActionListener.onItemEditSheet(item)
                    true
                }
                R.id.delete_category -> {
                    itemActionListener.onItemDelete(item)
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