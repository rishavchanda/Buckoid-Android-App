package com.rishav.buckoid.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rishav.buckoid.Model.Transaction

@Dao
interface TransactionDao {

    @Query("SELECT * FROM `Transaction` ORDER BY year DESC,month DESC,day DESC,category DESC")
    fun getTransaction(): LiveData<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE month=:month AND year=:year")
    fun getMonthlyTransaction(month: Int,year: Int): LiveData<List<Transaction>>

    @Query("SELECT * FROM `Transaction` WHERE year=:year")
    fun getYearlyTransaction(year: Int): LiveData<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Transaction)

    @Query("DELETE FROM `TRANSACTION` WHERE id=:id")
    fun deleteTransaction(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTransaction(transaction: Transaction)

}