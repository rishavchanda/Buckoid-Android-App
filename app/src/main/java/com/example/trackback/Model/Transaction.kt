package com.example.trackback.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Transaction")
@Parcelize
class Transaction (

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,

    var category: String,
    var title: String,
    var amount: Double,
    var date: String,
    var note: String

    ) : Parcelable