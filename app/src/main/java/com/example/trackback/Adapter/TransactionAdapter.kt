package com.example.trackback.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.databinding.TransactionItemBinding

class TransactionAdapter(val context: Context,val transList: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.transactionViewHolder>() {

    class transactionViewHolder(val binding:TransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionAdapter.transactionViewHolder {
        return transactionViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: TransactionAdapter.transactionViewHolder, position: Int) {
        val data = transList[position]
        holder.binding.title.text = data.title
        holder.binding.money.text = "₹"+data.amount.toString()
        holder.binding.date.text = data.date
        holder.binding.category.text = data.category

        when(data.category){
            "Food" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_fastfood_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow))
                //holder.binding.
            }
            "Shopping" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBlue))

            }
            "Transport" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_directions_transit_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.violet))

            }
            "Health" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))

            }
            "Others" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_category_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBrown))

            }
            "Academics" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))

            }

        }
    }

    override fun getItemCount() = transList.size

}