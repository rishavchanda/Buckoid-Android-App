package com.rishav.buckoid.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.rishav.buckoid.Database.TransactionDatabase
import com.rishav.buckoid.Model.Transaction
import com.rishav.buckoid.Repository.TransactionRepository


class TransactionViewModel(application: Application): AndroidViewModel(application) {

    val repository: TransactionRepository

    init{
        val dao = TransactionDatabase.getDatabaseInstance(application).myTransactionDao()
        repository= TransactionRepository(dao)
    }

    fun addTransaction(transaction: Transaction){
        repository.insertTransaction(transaction)
    }

    fun getTransaction(): LiveData<List<Transaction>> = repository.getAllTransaction()

    fun getMonthlyTransaction(month:Int,Year:Int): LiveData<List<Transaction>> = repository.getMonthlyTransaction(month,Year)

    fun getYearlyTransaction(year: Int): LiveData<List<Transaction>> = repository.getYearlyTransaction(year)

    fun deleteTransaction(id:Int){
        repository.deleteTransaction(id)
    }

    fun updateTransaction(transaction: Transaction){
        repository.updateTransaction(transaction)
    }




}