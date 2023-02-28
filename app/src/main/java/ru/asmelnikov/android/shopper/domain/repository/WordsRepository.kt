package ru.asmelnikov.android.shopper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete


interface WordsRepository {

    fun getAllWords(): Flow<List<WordsForAutoComplete>>

    suspend fun insertWords(word: WordsForAutoComplete)

}