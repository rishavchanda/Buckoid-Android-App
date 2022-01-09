package com.rishav.buckoid.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rishav.buckoid.Model.Transaction
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.TransactionItemBinding
import com.rishav.buckoid.fragments.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal

class TransactionAdapter(val context: Context, val activity:Activity,val fragment:String, private val transList: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.transactionViewHolder>(){

    class transactionViewHolder(val binding:TransactionItemBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var userDetails: SharedPreferences
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
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light))
            }
            "Shopping" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBlue))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBlue))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue_light))
            }
            "Transport" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_directions_transit_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.violet))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.violet))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.violet_light))
            }
            "Health" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
            }
            "Other" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_category_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBrown))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.lightBrown))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBrown_light))
            }
            "Education" -> {
                holder.binding.cardIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                holder.binding.cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
                holder.binding.category.setTextColor(ContextCompat.getColor(context, R.color.green))
                holder.binding.cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
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


