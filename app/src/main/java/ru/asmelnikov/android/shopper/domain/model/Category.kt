package ru.asmelnikov.android.shopper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    var doneItems: Int,
    var allItems: Int,
    var category: String
) : Parcelable