package com.example.trackback.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.trackback.Model.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM `Transaction`")
    fun getTransaction(): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Transaction)

    @Query("DELETE FROM `TRANSACTION` WHERE id=:id")
    fun deleteTransaction(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTransaction(transaction: Transaction)

}