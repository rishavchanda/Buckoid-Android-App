package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.FullScreenActivity
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentDashboardBinding
import org.eazegraph.lib.models.PieModel


class Dashboard : Fragment() {

    private val viewModel: TransactionViewModel by viewModels()
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
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)

        getData(binding)

        binding.addNew.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, FullScreenActivity::class.java))
                //finish()
            }
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun getData(binding: FragmentDashboardBinding) {
        totalExpense = 0.0
        totalGoal = 5000.0f
        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport=0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f
        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
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
        val mPieChart = binding.piechart

        mPieChart.addPieSlice(PieModel("Food", totalFood, ContextCompat.getColor(requireContext(), R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Shopping", totalShopping, ContextCompat.getColor(requireContext(), R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Health", totalHealth, ContextCompat.getColor(requireContext(), R.color.red)))
        mPieChart.addPieSlice(PieModel("Others", totalOthers, ContextCompat.getColor(requireContext(), R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Transport", totalTransport, ContextCompat.getColor(requireContext(), R.color.violet)))
        mPieChart.addPieSlice(PieModel("Academics", totalAcademics, ContextCompat.getColor(requireContext(), R.color.green)))

        if (totalGoal>totalExpense){
            mPieChart.addPieSlice(PieModel("Left",totalGoal-(totalExpense.toFloat()) , ContextCompat.getColor(requireContext(), R.color.background)))
        }

        mPieChart.startAnimation()

    }


}