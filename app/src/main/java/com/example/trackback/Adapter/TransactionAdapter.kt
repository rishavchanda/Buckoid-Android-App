package com.example.trackback.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.databinding.TransactionItemBinding
import com.example.trackback.fragments.*

class TransactionAdapter(val context: Context,val fragment:String, private val transList: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.transactionViewHolder>() {

    class transactionViewHolder(val binding:TransactionItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): transactionViewHolder {
        return transactionViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: transactionViewHolder, position: Int) {
        val data = transList[position]
        holder.binding.title.text = data.title
        holder.binding.money.text = "₹"+data.amount.toInt().toString()
        holder.binding.date.text = data.date
        holder.binding.category.text = data.category

        when(data.category){
            "Food" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_fastfood_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.yellow))
            }
            "Shopping" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBlue))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBlue))
            }
            "Transport" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_directions_transit_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.violet))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.violet))
            }
            "Health" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            "Other" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_category_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBrown))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBrown))
            }
            "Education" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

        }


        holder.binding.root.setOnClickListener {
             if(fragment == "Dashboard"){
                 val argument = DashboardDirections.goToTransactionDetails(data,fragment)
                    Navigation.findNavController(it).navigate(argument)
             }else if(fragment == "AllTransactions"){
                 val argument = AllTransactionsDirections.allTransactionToTransactionDetails(data,fragment)
                 Navigation.findNavController(it).navigate(argument)
             }

        }

    }

    override fun getItemCount() = transList.size

}


