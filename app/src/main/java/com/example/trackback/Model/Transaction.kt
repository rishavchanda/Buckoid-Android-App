package com.example.trackback.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Transaction")
class Transaction (

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,

    var category: String,
    var title: String,
    var amount: Double,
    var date: String,
    var note: String

    )