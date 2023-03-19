package ru.asmelnikov.android.shopper.presentation.sheet_dialogs

import android.content.Context
import android.widget.ArrayAdapter
import ru.asmelnikov.android.shopper.R

fun createArrayAdapterCategories(context: Context): ArrayAdapter<String> {
    val categoriesDropDown = context.resources.getStringArray(R.array.categories)
    return ArrayAdapter(context, R.layout.dropdown_item, categoriesDropDown)
}

fun createArrayAdapterUnits(context: Context): ArrayAdapter<String> {
    val categoriesDropDown = context.resources.getStringArray(R.array.units)
    return ArrayAdapter(context, R.layout.dropdown_item, categoriesDropDown)
}