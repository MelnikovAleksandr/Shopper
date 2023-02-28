package ru.asmelnikov.android.shopper.data

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.domain.repository.WordsRepository

class WordsRepositoryImpl(
    private val wordsDao: WordsDao
) : WordsRepository {
    override fun getAllWords(): Flow<List<WordsForAutoComplete>> {
        return wordsDao.getAllWords()
    }

    override suspend fun insertWords(word: WordsForAutoComplete) {
        wordsDao.insertWords(word)
    }
}