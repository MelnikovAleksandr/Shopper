package ru.asmelnikov.android.shopper.utils

import android.content.Context
import android.widget.ArrayAdapter
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete

internal class WordsCompleterAdapter(
    context: Context,
    words: List<WordsForAutoComplete>
) : ArrayAdapter<String>(
    context,
    android.R.layout.select_dialog_item,
    words.map { it.word }
)