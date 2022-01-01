package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.MonthlyTransactionAdapter
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.Model.MonthlyTransactions
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAddTransactionBinding
import com.example.trackback.databinding.FragmentAllTransactionsBinding
import com.google.android.material.button.MaterialButton
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AllTransactions : Fragment() ,View.OnClickListener {


    lateinit var binding:FragmentAllTransactionsBinding

    private val viewModel: TransactionViewModel by viewModels()
    lateinit var mPieChart:PieChart
    private var month =""
    private var year=0
    private var monthInt =1
    private var totalExpense = 0.0
    private var totalGoal = 5000.0f
    private var totalFood = 0.0f
    private var totalShopping = 0.0f
    private var totalTransport=0.0f
    private var totalHealth = 0.0f
    private var totalOthers = 0.0f
    private var totalAcademics = 0.0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllTransactionsBinding.inflate(inflater, container, false)
        setListener()
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
        binding.transactionRecyclerView.visibility = View.VISIBLE
        binding.selectors.visibility = View.GONE
        binding.monthlyCard.visibility = View.GONE
        binding.yearSpinner.visibility = View.GONE
       binding.title.text = "All Transactions"
        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList)
        })
    }

    private fun showMonthlyTransactions() {
        year=SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()).toInt()
        val list = mutableListOf(2020)
        list.clear()
        for(i in year downTo 2020){
            list += i
        }
        val yearAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,list)
        binding.yearSpinner.setAdapter(yearAdapter)
        setMonth(binding.January,binding.January)
        showMonthsTransaction()
        binding.transactionRecyclerView.visibility = View.VISIBLE
        binding.selectors.visibility = View.VISIBLE
        binding.monthlyCard.visibility = View.VISIBLE
        binding.yearSpinner.visibility = View.VISIBLE
        binding.title.text = "Monthly Transactions"
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                year=binding.yearSpinner.selectedItem.toString().toInt()
                showMonthsTransaction()
            } // to close the onItemSelected
            override fun onNothingSelected(parent: AdapterView<*>) {
                year=binding.yearSpinner.selectedItem.toString().toInt()
                showMonthsTransaction()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showMonthsTransaction(){
        mPieChart=binding.piechart
        mPieChart.clearChart()
        totalExpense = 0.0
        totalGoal = 5000.0f
        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport=0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f
        viewModel.getMonthlyTransaction(monthInt,year).observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList.reversed())

            for(i in transactionList)
            {
                totalExpense += i.amount
                when (i.category) {
                    "Food" -> {
                        totalFood+=(i.amount.toFloat())
                    }
                    "Shopping" -> {
                        totalShopping+=(i.amount.toFloat())
                    }
                    "Transport" -> {
                        totalTransport+=(i.amount.toFloat())
                    }

                    "Health" -> {
                        totalHealth+=(i.amount.toFloat())
                    }
                    "Other" -> {
                        totalOthers+=(i.amount.toFloat())
                    }
                    "Education" -> {
                        totalAcademics+=(i.amount.toFloat())
                    }
                }
            }
            binding.expense.text = "₹${totalExpense.toInt()}"
            binding.budget.text = "₹${totalGoal.toInt()}"
            binding.date.text = "${month} ${year}"
            if (totalExpense>totalGoal){
                binding.indicator.setImageResource(R.drawable.ic_negative_transaction)
                binding.expense.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                binding.indicator.setImageResource(R.drawable.ic_positive_amount)
            }
            showPiChart()
        })
    }
    private fun showPiChart() {
        mPieChart.addPieSlice(PieModel("Food", totalFood, ContextCompat.getColor(requireContext(), R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Shopping", totalShopping, ContextCompat.getColor(requireContext(), R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Health", totalHealth, ContextCompat.getColor(requireContext(), R.color.red)))
        mPieChart.addPieSlice(PieModel("Others", totalOthers, ContextCompat.getColor(requireContext(), R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Transport", totalTransport, ContextCompat.getColor(requireContext(), R.color.violet)))
        mPieChart.addPieSlice(PieModel("Academics", totalAcademics, ContextCompat.getColor(requireContext(), R.color.green)))

        if (totalGoal>totalExpense){
            mPieChart.addPieSlice(PieModel("Empty",totalGoal-(totalExpense.toFloat()) , ContextCompat.getColor(requireContext(), R.color.background)))
        }

        mPieChart.startAnimation()
    }


    private fun showYearlyTransactions() {
        binding.title.text = "Yearly Transactions"
        year=SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime()).toInt()
        val list = mutableListOf(2020)
        list.clear()
        for(i in year downTo 2020){
            list += i
        }
        val yearAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,list)
        binding.yearSpinner.setAdapter(yearAdapter)
        binding.transactionRecyclerView.visibility = View.VISIBLE
        binding.selectors.visibility = View.GONE
        binding.monthlyCard.visibility = View.VISIBLE
        binding.yearSpinner.visibility = View.VISIBLE
        showYearlyTransaction()
        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                year=binding.yearSpinner.selectedItem.toString().toInt()
                showYearlyTransaction()
            } // to close the onItemSelected
            override fun onNothingSelected(parent: AdapterView<*>) {
                year=binding.yearSpinner.selectedItem.toString().toInt()
                showYearlyTransaction()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showYearlyTransaction(){
        mPieChart=binding.piechart
        mPieChart.clearChart()
        totalExpense = 0.0
        totalGoal = 5000.0f
        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport=0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f
        viewModel.getYearlyTransaction(year).observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList.reversed())

            for(i in transactionList)
            {
                totalExpense += i.amount
                when (i.category) {
                    "Food" -> {
                        totalFood+=(i.amount.toFloat())
                    }
                    "Shopping" -> {
                        totalShopping+=(i.amount.toFloat())
                    }
                    "Transport" -> {
                        totalTransport+=(i.amount.toFloat())
                    }

                    "Health" -> {
                        totalHealth+=(i.amount.toFloat())
                    }
                    "Other" -> {
                        totalOthers+=(i.amount.toFloat())
                    }
                    "Education" -> {
                        totalAcademics+=(i.amount.toFloat())
                    }
                }
            }
            binding.expense.text = "₹${totalExpense.toInt()}"
            binding.budget.text = "₹${totalGoal.toInt()}"
            binding.date.text = "Year: ${year}"
            if (totalExpense>totalGoal){
                binding.indicator.setImageResource(R.drawable.ic_negative_transaction)
                binding.expense.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                binding.indicator.setImageResource(R.drawable.ic_positive_amount)
            }
            showPiChart()
        })
    }


    private fun setListener() {
        binding.January.setOnClickListener(this)
        binding.February.setOnClickListener(this)
        binding.March.setOnClickListener(this)
        binding.April.setOnClickListener(this)
        binding.May.setOnClickListener(this)
        binding.June.setOnClickListener(this)
        binding.July.setOnClickListener(this)
        binding.August.setOnClickListener(this)
        binding.September.setOnClickListener(this)
        binding.October.setOnClickListener(this)
        binding.November.setOnClickListener(this)
        binding.December.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.January -> {
                setMonth(v,binding.January)
                monthInt=1
                showMonthsTransaction()
            }
            binding.February -> {
                setMonth(v,binding.February)
                monthInt=2
                showMonthsTransaction()
            }
            binding.March -> {
                setMonth(v,binding.March)
                monthInt=3
                showMonthsTransaction()
            }
            binding.April -> {
                setMonth(v,binding.April)
                monthInt=4
                showMonthsTransaction()
            }
            binding.May -> {
                setMonth(v,binding.May)
                monthInt=5
                showMonthsTransaction()
            }
            binding.June -> {
                setMonth(v,binding.June)
                monthInt=6
                showMonthsTransaction()
            }
            binding.July -> {
                setMonth(v,binding.July)
                monthInt=7
                showMonthsTransaction()
            }
            binding.August -> {
                setMonth(v,binding.August)
                monthInt=8
                showMonthsTransaction()
            }
            binding.September -> {
                setMonth(v,binding.September)
                monthInt=9
                showMonthsTransaction()
            }
            binding.October -> {
                setMonth(v,binding.October)
                monthInt=10
                showMonthsTransaction()
            }
            binding.November -> {
                setMonth(v,binding.November)
                monthInt=11
                showMonthsTransaction()
            }
            binding.December -> {
                setMonth(v,binding.December)
                monthInt=12
                showMonthsTransaction()
            }
        }
    }

    private fun setMonth(v: View, button: MaterialButton) {
        month = button.text.toString()
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textPrimary))

        when (v) {
            binding.January -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.February -> {
                removeBackground(binding.January)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.March -> {
                removeBackground(binding.February)
                removeBackground(binding.January)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.April -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.January)
                removeBackground(binding.May)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.May -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.June -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.July -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.January)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.August -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.January)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.September -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.January)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.October -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.January)
                removeBackground(binding.November)
                removeBackground(binding.December)
            }
            binding.November -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.January)
                removeBackground(binding.June)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.January)
                removeBackground(binding.December)
            }
            binding.December -> {
                removeBackground(binding.February)
                removeBackground(binding.March)
                removeBackground(binding.April)
                removeBackground(binding.May)
                removeBackground(binding.January)
                removeBackground(binding.July)
                removeBackground(binding.August)
                removeBackground(binding.September)
                removeBackground(binding.October)
                removeBackground(binding.November)
                removeBackground(binding.January)
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