package ru.asmelnikov.android.shopper.presentation.sheet_dialogs

import android.content.Context
import android.widget.Toast
import ru.asmelnikov.android.shopper.R

fun showErrorToast(context: Context) {
    Toast.makeText(context, context.getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show()
}