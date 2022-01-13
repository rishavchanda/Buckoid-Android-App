package com.rishav.buckoid.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rishav.buckoid.MainActivity
import com.rishav.buckoid.Model.Transaction
import com.rishav.buckoid.R
import com.rishav.buckoid.ViewModel.TransactionViewModel
import com.rishav.buckoid.databinding.TransactionItemBinding
import com.rishav.buckoid.fragments.*


class TransactionAdapter(val context: Context, val viewModel: TransactionViewModel,val activity: Activity, val fragment:String, private val transList: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.transactionViewHolder>(){

    class transactionViewHolder(val binding:TransactionItemBinding) : RecyclerView.ViewHolder(binding.root)

    lateinit var userDetails: SharedPreferences
    lateinit var currency:String
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): transactionViewHolder {
        return transactionViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: transactionViewHolder, position: Int) {
        val data = transList[position]
        userDetails = activity.getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        currency = userDetails.getString("currency","₹").toString()
        holder.binding.title.text = data.title
        holder.binding.money.text = currency+" "+data.amount.toInt().toString()
        holder.binding.date.text = data.date
        holder.binding.category.text = data.category

        setIcons(data.category,holder.binding.cardIcon,holder.binding.cardImage,holder.binding.category)


        holder.binding.root.setOnClickListener {
            openTransaction(data,fragment)
        }

    }

    private fun setIcons(category: String, cardIcon: ImageView, cardImage: CardView, category1: TextView) {
        when(category){
            "Food" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_fastfood_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.yellow))
                category1.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow_light))
            }
            "Shopping" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBlue))
                category1.setTextColor(ContextCompat.getColor(context, R.color.lightBlue))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue_light))
            }
            "Transport" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_directions_transit_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.violet))
                category1.setTextColor(ContextCompat.getColor(context, R.color.violet))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.violet_light))
            }
            "Health" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_favorite_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.red))
                category1.setTextColor(ContextCompat.getColor(context, R.color.red))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_light))
            }
            "Other" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_category_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightBrown))
                category1.setTextColor(ContextCompat.getColor(context, R.color.lightBrown))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.lightBrown_light))
            }
            "Education" -> {
                cardIcon.setImageResource(R.drawable.ic_baseline_auto_stories_24)
                cardIcon.setColorFilter(ContextCompat.getColor(context, R.color.green))
                category1.setTextColor(ContextCompat.getColor(context, R.color.green))
                cardImage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green_light))
            }

        }
    }

    private fun openTransaction(data: Transaction, fragment: String) {
        val bottomDialog: BottomSheetDialog = BottomSheetDialog(context,R.style.bottom_dialog)
        bottomDialog.setContentView(R.layout.transaction_details_dialog)

        //textViews
        val amount = bottomDialog.findViewById<TextView>(R.id.amount)
        val title = bottomDialog.findViewById<TextView>(R.id.title)
        val date = bottomDialog.findViewById<TextView>(R.id.date)
        val note = bottomDialog.findViewById<TextView>(R.id.note)
        val nullText = bottomDialog.findViewById<TextView>(R.id.nullText)

        //icon
        val cardIcon = bottomDialog.findViewById<ImageView>(R.id.card_icon)
        val cardImageView = bottomDialog.findViewById<CardView>(R.id.card_image)

        if (cardImageView != null && cardIcon != null && nullText!=null) {
                setIcons(data.category,cardIcon,cardImageView,nullText)
        }

        //buttons
        val edit=bottomDialog.findViewById<Button>(R.id.edit)
        val delete=bottomDialog.findViewById<Button>(R.id.delete)

        amount?.text = currency+" "+data.amount.toInt().toString()
        title?.text = data.title
        date?.text = data.date
        note?.text = data.note

        edit?.setOnClickListener{
            if(fragment == "Dashboard"){
                val argument = DashboardDirections.actionDashboard2ToAddTransaction(data,true)
                Navigation.findNavController(activity,R.id.fragmentContainerView).navigate(argument)
            }else if(fragment == "AllTransactions"){
                val argument = AllTransactionsDirections.actionTransactionsToAddTransaction(data,true)
                Navigation.findNavController(activity,R.id.fragmentContainerView).navigate(argument)
            }
            bottomDialog.dismiss()
        }
        delete?.setOnClickListener{
            deleteTransaction(data,bottomDialog)
        }

        bottomDialog.show()
    }

    private fun deleteTransaction(data: Transaction,detailDialog: BottomSheetDialog) {
        val bottomDialog: BottomSheetDialog = BottomSheetDialog(context,R.style.bottom_dialog)
        bottomDialog.setContentView(R.layout.dialog_delete)

        val delete=bottomDialog.findViewById<Button>(R.id.delete)
        val cancel=bottomDialog.findViewById<Button>(R.id.cancel)

        delete?.setOnClickListener{
            viewModel.deleteTransaction(data.id!!)
            bottomDialog.dismiss()
            detailDialog.dismiss()
        }
        cancel?.setOnClickListener{
            bottomDialog.dismiss()
        }

        bottomDialog.show()
    }

    override fun getItemCount() = transList.size

}


