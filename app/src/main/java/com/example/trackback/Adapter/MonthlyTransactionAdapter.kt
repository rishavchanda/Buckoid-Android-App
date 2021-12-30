package com.example.trackback.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackback.Model.MonthlyTransactions
import com.example.trackback.R
import com.example.trackback.databinding.MonthlyTransactionItemBinding
import org.eazegraph.lib.models.PieModel

class MonthlyTransactionAdapter(val context: Context, private val transList: List<MonthlyTransactions>) : RecyclerView.Adapter<MonthlyTransactionAdapter.transactionViewHolder>(){

    class transactionViewHolder(val binding: MonthlyTransactionItemBinding) : RecyclerView.ViewHolder(binding.root)

    private var totalExpense = 0.0
    private var totalGoal = 5000.0f
    private var totalFood = 0.0f
    private var totalShopping = 0.0f
    private var totalTransport=0.0f
    private var totalHealth = 0.0f
    private var totalOthers = 0.0f
    private var totalAcademics = 0.0f
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthlyTransactionAdapter.transactionViewHolder {
        return MonthlyTransactionAdapter.transactionViewHolder(
            MonthlyTransactionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: MonthlyTransactionAdapter.transactionViewHolder, position: Int) {
        val data = transList[position]
        holder.binding.date.text = data.date
        holder.binding.budget.text = data.budget.toInt().toString()
        holder.binding.expense.text = data.expense.toInt().toString()
        totalFood = data.food.toFloat()
        totalExpense = data.expense
        totalShopping = data.shopping.toFloat()
        totalTransport = data.shopping.toFloat()
        totalHealth = data.health.toFloat()
        totalOthers = data.others.toFloat()
        totalAcademics = data.education.toFloat()
        totalGoal = data.budget.toFloat()
        showPiChart(holder.binding)


    }

    override fun getItemCount() = transList.size


    private fun showPiChart(binding: MonthlyTransactionItemBinding) {
        val mPieChart = binding.piechart

        mPieChart.addPieSlice(PieModel("Food", totalFood, ContextCompat.getColor(context, R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Shopping", totalShopping, ContextCompat.getColor(context, R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Health", totalHealth, ContextCompat.getColor(context, R.color.red)))
        mPieChart.addPieSlice(PieModel("Others", totalOthers, ContextCompat.getColor(context, R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Transport", totalTransport, ContextCompat.getColor(context, R.color.violet)))
        mPieChart.addPieSlice(PieModel("Academics", totalAcademics, ContextCompat.getColor(context, R.color.green)))

        if (totalGoal>totalExpense){
            mPieChart.addPieSlice(PieModel("Left",totalGoal-(totalExpense.toFloat()) , ContextCompat.getColor(context, R.color.background)))
        }

        mPieChart.startAnimation()

    }

}