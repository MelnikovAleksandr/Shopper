package ru.asmelnikov.android.shopper.utils

import android.view.View
import android.widget.ImageView
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.Category

fun imageBinding(view: View, category: Category) {

    val imageview = view as ImageView

    imageview.setImageResource(
        when (category.category) {
            "Аптека" -> R.drawable.medicine_ic
            "Алкоголь" -> R.drawable.alcohol_ic
            "Продукты" -> R.drawable.food_ic
            "Супермаркет" -> R.drawable.mall_ic
            "Косметика" -> R.drawable.cosmetic_ic
            "Хозяйственный" -> R.drawable.cleaning_products_ic
            "Питомцы" -> R.drawable.pet_ic
            "Сад-огород" -> R.drawable.botany_ic
            "Одежда" -> R.drawable.clothes_ic
            "Книги" -> R.drawable.books_ic_2
            "Инструменты" -> R.drawable.tools_ic
            "Доставка" -> R.drawable.delivery_ic
            "Бытовая техника" -> R.drawable.tech_ic
            "Мебель" -> R.drawable.furniture_ic
            "Спорт" -> R.drawable.sports_ic
            "Для детей" -> R.drawable.baby_ic
            "Автомобиль" -> R.drawable.car_ic
            else -> R.drawable.other_items_ic
        }
    )
}

