package com.example.trackback.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAllTransactionsBinding

class AllTransactions : Fragment() {


    lateinit var binding:FragmentAllTransactionsBinding

    private val viewModel: TransactionViewModel by viewModels()

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
    }

    private fun showYearlyTransactions() {
        binding.title.text = "Yearly Transactions"
    }



}