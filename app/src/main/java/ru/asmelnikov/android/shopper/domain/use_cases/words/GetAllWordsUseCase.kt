package ru.asmelnikov.android.shopper.domain.use_cases.words

import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete
import ru.asmelnikov.android.shopper.domain.repository.WordsRepository

class GetAllWordsUseCase(private val wordsRepository: WordsRepository) {

    operator fun invoke(): Flow<List<WordsForAutoComplete>> {
        return wordsRepository.getAllWords()
    }
}