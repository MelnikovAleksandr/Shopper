package ru.asmelnikov.android.shopper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoryId"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
@Parcelize
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val categoryId: Int,
    val count: Int,
    val price: Int,
    val units: String,
    var bought: Boolean,
) : Parcelable