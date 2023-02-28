package ru.asmelnikov.android.shopper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "words")
@Parcelize
data class WordsForAutoComplete(
    @PrimaryKey
    val word: String
) : Parcelable
