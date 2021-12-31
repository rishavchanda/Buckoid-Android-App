package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class AllTransactions : Fragment() ,View.OnClickListener {


    lateinit var binding:FragmentAllTransactionsBinding

    private val viewModel: TransactionViewModel by viewModels()
    lateinit var mPieChart:PieChart

    private var month =""
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
       binding.title.text = "All Transactions"
        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList)
        })
    }


    private fun showMonthlyTransactions() {
        setMonth(binding.January,binding.January)
        showMonthsTransaction(1,2021)
        binding.transactionRecyclerView.visibility = View.VISIBLE
        binding.selectors.visibility = View.VISIBLE
        binding.monthlyCard.visibility = View.VISIBLE
        binding.title.text = "Monthly Transactions"
    }

    @SuppressLint("SetTextI18n")
    private fun showMonthsTransaction(months:Int, year:Int){
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
        viewModel.getMonthlyTransaction(months,year).observe(viewLifecycleOwner,{ transactionList ->
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
                showMonthsTransaction(1,2021)
            }
            binding.February -> {
                setMonth(v,binding.February)
                showMonthsTransaction(2,2021)
            }
            binding.March -> {
                setMonth(v,binding.March)
                showMonthsTransaction(3,2021)
            }
            binding.April -> {
                setMonth(v,binding.April)
                showMonthsTransaction(4,2021)
            }
            binding.May -> {
                setMonth(v,binding.May)
                showMonthsTransaction(5,2021)
            }
            binding.June -> {
                setMonth(v,binding.June)
                showMonthsTransaction(6,2021)
            }
            binding.July -> {
                setMonth(v,binding.July)
                showMonthsTransaction(7,2021)
            }
            binding.August -> {
                setMonth(v,binding.August)
                showMonthsTransaction(8,2021)
            }
            binding.September -> {
                setMonth(v,binding.September)
                showMonthsTransaction(9,2021)
            }
            binding.October -> {
                setMonth(v,binding.October)
                showMonthsTransaction(10,2021)
            }
            binding.November -> {
                setMonth(v,binding.November)
                showMonthsTransaction(11,2021)
            }
            binding.December -> {
                setMonth(v,binding.December)
                showMonthsTransaction(12,2021)
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