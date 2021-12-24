package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAddTransactionBinding

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.view.*
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*


class AddTransaction : Fragment(), View.OnClickListener {
   private lateinit var binding: FragmentAddTransactionBinding
   private var category = ""

   private val viewModel: TransactionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentAddTransactionBinding.inflate(inflater, container, false)
        setListner(binding)
        datePicker(binding)
        binding.addTransaction.setOnClickListener{ addNewTransaction() }
        binding.back.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.action_addExpense_to_mainActivity2) }
        return binding.root
    }

    private fun setListner(binding: FragmentAddTransactionBinding) {
        binding.food.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transport.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.academics.setOnClickListener(this)

    }



    private fun addNewTransaction() {
       val title = binding.editTitle.text.toString()
       val amount = binding.editMoney.text.toString()
       val note = binding.editNote.text.toString()
       val date = binding.editDate.text.toString()

       if (title == "" || amount == "" || note == "" || date == "" || category == ""){
           Toast.makeText(context, "Enter all required details", Toast.LENGTH_SHORT).show()
       }else {
           val transaction = Transaction(
               null,
               title = title,
               amount = amount.toDouble(),
               note = note,
               date = date,
               category = category

           )
           viewModel.addTransaction(transaction)
           Toast.makeText(context, "Transaction Added Successfully", Toast.LENGTH_SHORT).show()
           Navigation.findNavController(binding.root)
               .navigate(R.id.action_addExpense_to_mainActivity2)
       }


    }


    @SuppressLint("SimpleDateFormat")
    fun datePicker(binding:FragmentAddTransactionBinding){
        val cal = Calendar.getInstance()
        binding.editDate.setText(SimpleDateFormat("dd MMMM  yyyy").format(System.currentTimeMillis()))
        val dateSetListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd MMMM  yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.editDate.setText(sdf.format(cal.time))

        }

        binding.editDate.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.food -> {
                setCategory(v,binding.food)
            }
            binding.shopping -> {
                setCategory(v,binding.shopping)
            }
            binding.transport -> {
                setCategory(v,binding.transport)
            }
            binding.health -> {
                setCategory(v,binding.health)
            }
            binding.others -> {
                setCategory(v,binding.others)
            }
            binding.academics -> {
                setCategory(v,binding.academics)
            }
        }
    }

    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mtrl_btn_text_btn_bg_color_selector))
        button.setIconTintResource(R.color.purple_200)
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        when (v) {
            binding.food -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.shopping -> {
                removeBackground(binding.food)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.transport -> {
                removeBackground(binding.shopping)
                removeBackground(binding.food)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.health -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.food)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.others -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.food)
                removeBackground(binding.academics)
            }
            binding.academics -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.food)
            }
        }
    }

    private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }


}

