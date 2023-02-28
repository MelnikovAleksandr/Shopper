package ru.asmelnikov.android.shopper.domain.use_cases.words

import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.domain.repository.WordsRepository

class InsertWordsUseCase(private val wordsRepository: WordsRepository) {

    suspend operator fun invoke(word: WordsForAutoComplete) {
        wordsRepository.insertWords(word)
    }
}