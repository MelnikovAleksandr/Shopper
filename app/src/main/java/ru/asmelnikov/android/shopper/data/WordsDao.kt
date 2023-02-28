package ru.asmelnikov.android.shopper.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.asmelnikov.android.shopper.domain.model.WordsForAutoComplete

@Dao
interface WordsDao {

    @Query("SELECT * FROM words")
    fun getAllWords(): Flow<List<WordsForAutoComplete>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(word: WordsForAutoComplete)

}