package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eazegraph.lib.models.PieModel

import android.graphics.Color
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.FullScreenActivity
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentDashboardBinding

import org.eazegraph.lib.charts.PieChart



class Dashboard : Fragment() {

    lateinit var bindDashboard: FragmentDashboardBinding

    val viewModel: TransactionViewModel by viewModels()
    var totalExpense = 0.0
    var totalGoal = 5000.0f
    var totalFood = 0.0f
    var totalShopping = 0.0f
    var totalTransport=0.0f
    var totalHealth = 0.0f
    var totalOthers = 0.0f
    var totalAcademics = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)

        getData(binding)

        binding.addNew.setOnClickListener(View.OnClickListener {
            requireActivity().run{
                startActivity(Intent(this, FullScreenActivity::class.java))
                //finish()
            }
        })
        return binding.root
    }

    private fun getData(binding: FragmentDashboardBinding) {
        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList)

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
                    "Others" -> {
                        totalOthers+=(i.amount.toFloat())
                    }
                    "Academics" -> {
                        totalAcademics+=(i.amount.toFloat())
                    }
                }
            }
            binding.expense.setText("₹"+totalExpense.toString())
            binding.budget.setText("₹"+totalGoal.toString())
            if (totalExpense>totalGoal){
                binding.indicator.setImageResource(R.drawable.ic_negative_transaction)
                binding.expense.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            }else{
                binding.indicator.setImageResource(R.drawable.ic_positive_amount)
            }
            showPiChart(binding)
        })

    }

    private fun showPiChart(binding: FragmentDashboardBinding) {
        val mPieChart = binding.piechart as PieChart

        mPieChart.addPieSlice(PieModel("Food", totalFood, ContextCompat.getColor(requireContext(), R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Shopping", totalShopping, ContextCompat.getColor(requireContext(), R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Transport", totalTransport, ContextCompat.getColor(requireContext(), R.color.violet)))
        mPieChart.addPieSlice(PieModel("Health", totalHealth, ContextCompat.getColor(requireContext(), R.color.red)))
        mPieChart.addPieSlice(PieModel("Others", totalOthers, ContextCompat.getColor(requireContext(), R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Academics", totalAcademics, ContextCompat.getColor(requireContext(), R.color.green)))

        if (totalGoal>totalExpense){
            mPieChart.addPieSlice(PieModel("Left",totalGoal-(totalExpense.toFloat()) , ContextCompat.getColor(requireContext(), R.color.background)))
        }
        //mPieChart.addPieSlice(PieModel("Academics", 100F, ContextCompat.getColor(requireContext(), R.color.cardBackground)))

        mPieChart.startAnimation()

    }


}