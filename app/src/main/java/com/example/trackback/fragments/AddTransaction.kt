package com.example.trackback.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceControl
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAddTransactionBinding

class AddTransaction : Fragment() {
   lateinit var binding: FragmentAddTransactionBinding

   val viewModel: TransactionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentAddTransactionBinding.inflate(inflater, container, false)
        binding.addTransaction.setOnClickListener(View.OnClickListener { addNewTransaction(it) })
        binding.back.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.action_addExpense_to_mainActivity2) }
        return binding.root
    }

    private fun addNewTransaction(it: View?) {
        val category = "Food"
       val title = binding.editTitle.text.toString()
       val amount = binding.editMoney.text.toString()
       val note = binding.editNote.text.toString()
       val date = binding.editDate.text.toString()
       val transaction = Transaction(
           null,
           title = title,
           amount = amount.toDouble(),
           note = note,
           date = date,
           category = category

       )
       viewModel.addTransaction(transaction)
       Toast.makeText(context,"Transaction Added Successfully",Toast.LENGTH_SHORT).show()
    }


}