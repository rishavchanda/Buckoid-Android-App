package com.example.trackback.Repository

import androidx.lifecycle.LiveData
import com.example.trackback.Dao.TransactionDao
import com.example.trackback.Model.Transaction

class TransactionRepository(val dao: TransactionDao) {

    fun getAllTransaction(): LiveData<List<Transaction>> {
        return dao.getTransaction()
    }

    fun getMonthlyTransaction(month:Int): LiveData<List<Transaction>>{
        return dao.getMonthlyTransaction(month)
    }

    fun getYearlyTransaction(year:Int): LiveData<List<Transaction>>{
        return dao.getYearlyTransaction(year)
    }

    fun insertTransaction(transaction: Transaction){
        dao.insertTransaction(transaction)
    }

    fun deleteTransaction(id:Int){
        dao.deleteTransaction(id)
    }

    fun updateTransaction(transaction: Transaction){
        dao.updateTransaction(transaction)
    }


}