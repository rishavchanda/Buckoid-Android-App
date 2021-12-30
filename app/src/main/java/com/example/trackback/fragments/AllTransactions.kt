package com.example.trackback.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.MonthlyTransactionAdapter
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.Model.MonthlyTransactions
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAllTransactionsBinding

class AllTransactions : Fragment() {


    lateinit var binding:FragmentAllTransactionsBinding

    private val viewModel: TransactionViewModel by viewModels()
    lateinit var monthTransactionList:ArrayList<MonthlyTransactions>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllTransactionsBinding.inflate(inflater, container, false)
        when(binding.toggleSelector.checkedButtonId) {
            R.id.all -> showAllTransactions()
            R.id.monthly -> showMonthlyTransactions()
            R.id.yearly -> showYearlyTransactions()
        }
        binding.toggleSelector.addOnButtonCheckedListener{ toggleSelector,checkedId,isChecked ->
            if(isChecked){
                when(checkedId) {
                    R.id.all -> showAllTransactions()
                    R.id.monthly -> showMonthlyTransactions()
                    R.id.yearly -> showYearlyTransactions()
                }
            }

        }
        return binding.root
    }


    private fun showAllTransactions() {
       binding.title.text = "All Transactions"
        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList)
        })
    }


    private fun showMonthlyTransactions() {
        binding.title.text = "Monthly Transactions"
        monthTransactionList = ArrayList()
        var c=0

        for (i in 12 downTo 1) {
            var totalExpense = 0.0
            var totalGoal = 5000.0f
            var totalFood = 0.0f
            var totalShopping = 0.0f
            var totalTransport=0.0f
            var totalHealth = 0.0f
            var totalOthers = 0.0f
            var totalAcademics = 0.0f
            viewModel.getMonthlyTransaction(i,2021).observe(viewLifecycleOwner, { transactionList ->
                if (!transactionList.isEmpty()) {
                    for (j in transactionList) {
                        totalExpense += j.amount
                        when (j.category) {
                            "Food" -> {
                                totalFood += (j.amount.toFloat())
                            }
                            "Shopping" -> {
                                totalShopping += (j.amount.toFloat())
                            }
                            "Transport" -> {
                                totalTransport += (j.amount.toFloat())
                            }

                            "Health" -> {
                                totalHealth += (j.amount.toFloat())
                            }
                            "Other" -> {
                                totalOthers += (j.amount.toFloat())
                            }
                            "Education" -> {
                                totalAcademics += (j.amount.toFloat())
                            }
                        }
                    }
                    monthTransactionList.add(c,MonthlyTransactions(
                        "",
                        i,
                        2021,
                        5000.0,
                        totalExpense,
                        totalFood.toDouble(),
                        totalShopping.toDouble(),
                        totalTransport.toDouble(),
                        totalHealth.toDouble(),
                        totalOthers.toDouble(),
                        totalAcademics.toDouble()
                    ))
                    c++

                }
                if(i==12) {
                    Toast.makeText(
                        context,
                        monthTransactionList.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.transactionRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    binding.transactionRecyclerView.adapter =
                        MonthlyTransactionAdapter(requireContext(), monthTransactionList)
                }

            })
        }
    }

    private fun showYearlyTransactions() {
        binding.title.text = "Yearly Transactions"
    }



}