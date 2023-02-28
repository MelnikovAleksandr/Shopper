package ru.asmelnikov.android.shopper.domain.use_cases

import ru.asmelnikov.android.shopper.domain.use_cases.words.InsertWordsUseCase
import ru.asmelnikov.android.shopper.domain.use_cases.words.GetAllWordsUseCase

data class WordsUseCases(
    val insertWordsUseCase: InsertWordsUseCase,
    val getAllWordsUseCase: GetAllWordsUseCase

)
