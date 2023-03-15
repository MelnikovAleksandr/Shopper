package ru.asmelnikov.android.shopper.utils

import android.content.Context
import android.widget.ArrayAdapter
import ru.asmelnikov.android.shopper.R
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete

internal class WordsCompleterAdapter(
    context: Context,
    words: List<WordsForAutoComplete>
) : ArrayAdapter<String>(
    context,
    R.layout.custom_select_dialog,
    words.map { it.word }
)