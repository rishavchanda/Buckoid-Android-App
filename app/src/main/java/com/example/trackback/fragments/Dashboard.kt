package com.example.trackback.fragments

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val mPieChart = binding.piechart as PieChart

        mPieChart.addPieSlice(PieModel("Food", 25F, ContextCompat.getColor(requireContext(), R.color.red)))
        mPieChart.addPieSlice(PieModel("Shopping", 15F, ContextCompat.getColor(requireContext(), R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Transport", 35F, ContextCompat.getColor(requireContext(), R.color.violet)))
        mPieChart.addPieSlice(PieModel("Health", 20F, ContextCompat.getColor(requireContext(), R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Others", 35F, ContextCompat.getColor(requireContext(), R.color.green)))
        mPieChart.addPieSlice(PieModel("Academics", 20F, ContextCompat.getColor(requireContext(), R.color.yellow)))
        //mPieChart.addPieSlice(PieModel("Academics", 100F, ContextCompat.getColor(requireContext(), R.color.cardBackground)))

        mPieChart.startAnimation()

        viewModel.getTransaction().observe(viewLifecycleOwner,{ transactionList ->
            binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionRecyclerView.adapter = TransactionAdapter(requireContext(),transactionList)

            for(i in transactionList)
            {
                totalExpense += i.amount
            }
            binding.expense.setText("₹"+totalExpense.toString())
        })

        binding.addNew.setOnClickListener(View.OnClickListener {
            requireActivity().run{
                startActivity(Intent(this, FullScreenActivity::class.java))
                //finish()
            }
        })





        return binding.root
    }


}