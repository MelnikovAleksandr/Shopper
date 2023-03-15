package ru.asmelnikov.android.shopper.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.Category

fun imageBinding(view: View, category: Category, context: Context) {

    val imageview = view as ImageView

    val categoriesDropDown = context.resources.getStringArray(R.array.categories)

    imageview.setImageResource(
        when (category.category) {
            categoriesDropDown[0] -> R.drawable.medicine_ic
            categoriesDropDown[1] -> R.drawable.alcohol_ic
            categoriesDropDown[2] -> R.drawable.food_ic
            categoriesDropDown[3] -> R.drawable.mall_ic
            categoriesDropDown[4] -> R.drawable.cosmetic_ic
            categoriesDropDown[5] -> R.drawable.cleaning_products_ic
            categoriesDropDown[6] -> R.drawable.pet_ic
            categoriesDropDown[7] -> R.drawable.botany_ic
            categoriesDropDown[8] -> R.drawable.clothes_ic
            categoriesDropDown[9] -> R.drawable.books_ic_2
            categoriesDropDown[10] -> R.drawable.tools_ic
            categoriesDropDown[11] -> R.drawable.delivery_ic
            categoriesDropDown[12] -> R.drawable.tech_ic
            categoriesDropDown[13] -> R.drawable.furniture_ic
            categoriesDropDown[14] -> R.drawable.sports_ic
            categoriesDropDown[15] -> R.drawable.baby_ic
            categoriesDropDown[16] -> R.drawable.car_ic
            else -> R.drawable.other_items_ic
        }
    )
}

