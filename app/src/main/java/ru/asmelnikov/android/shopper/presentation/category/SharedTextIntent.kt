package ru.asmelnikov.android.shopper.presentation.category

import android.content.Context
import android.content.Intent
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.Category
import ru.asmelnikov.android.shopper.domain.model.Item

fun sharedText(items: List<Item>, category: Category, context: Context) {
    Intent(Intent.ACTION_SEND).apply {

        val itemsListString =
            "${category.name}:\n " + items.map { "${it.name} - ${it.count} ${it.units.lowercase()}\n" }

        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            itemsListString.replace("\\[".toRegex(), "")
                .replace("]".toRegex(), "")
                .replace(",".toRegex(), "")
        )
        putExtra(
            Intent.EXTRA_SUBJECT,
            "${context.getString(R.string.shared_list_title)} ${category.name}"
        )
    }.also { intent ->
        val chooseIntent = Intent.createChooser(
            intent, context.getString(R.string.shared_list)
        )
        context.startActivity(chooseIntent)
    }
}