package ru.asmelnikov.android.shopper.presentation.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            count_text_view.text = item.count.toString()
            check_box.isChecked = item.bought

            check_box.setOnClickListener {
                item.bought = check_box.isChecked
                itemActionListener.onItemEdit(item)
            }

            item_constraint_layout.setOnLongClickListener {
                itemActionListener.onItemEditSheet(item)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}