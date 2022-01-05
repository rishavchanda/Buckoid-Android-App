package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentAddTransactionBinding

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.SharedPreferences
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal
import java.text.SimpleDateFormat
import java.util.*


class AddTransaction : Fragment(), View.OnClickListener {
   val data by navArgs<AddTransactionArgs>()
   private lateinit var binding: FragmentAddTransactionBinding
    lateinit var userDetails: SharedPreferences
   private var category = ""
    var day=0
    var month=0
    var year=0

   private val viewModel: TransactionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentAddTransactionBinding.inflate(inflater, container, false)
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNav.visibility = View.GONE
        setListner(binding)
        datePicker(binding)
        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        if(!userDetails.getBoolean("ShowedOnboardingAddTransaction",false)){
            showOnBoarding()
        }
        if(data.from){
            setDatas()
            binding.addTransaction.setText("Save Transaction")
            binding.titleAddTransacttion.setText("Edit Transaction")
            binding.back.setOnClickListener {
                val arg = AddTransactionDirections.actionAddTransactionToTransactionDetails(data.data,"AddTransaction")
                Navigation.findNavController(binding.root)
                    .navigate(arg)
            }
        }else{
            binding.back.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.action_addTransaction_to_dashboard2) }
        }

        binding.addTransaction.setOnClickListener{ addNewTransaction() }
        return binding.root
    }

    private fun setListner(binding: FragmentAddTransactionBinding) {
        binding.food.setOnClickListener(this)
        binding.shopping.setOnClickListener(this)
        binding.transport.setOnClickListener(this)
        binding.health.setOnClickListener(this)
        binding.others.setOnClickListener(this)
        binding.academics.setOnClickListener(this)

    }


    private fun setDatas(){
        binding.editTitle.setText(data.data.title)
        binding.editDate.setText(data.data.date)
        binding.editMoney.setText(data.data.amount.toString())
        binding.editNote.setText(data.data.note)
        category=data.data.category
        when (category) {
            "Food" -> {
                setCategory(binding.food, binding.food)
            }
            "Shopping" -> {
                setCategory(binding.shopping, binding.shopping)
            }
            "Transport" -> {
                setCategory(binding.transport, binding.transport)
            }
            "Health" -> {
                setCategory(binding.health, binding.health)
            }
            "Other" -> {
                setCategory(binding.others, binding.others)
            }
            "Education" -> {
                setCategory(binding.academics, binding.academics)
            }
        }
    }
    private fun addNewTransaction() {
       val title = binding.editTitle.text.toString()
       val amount = binding.editMoney.text.toString()
       val note = binding.editNote.text.toString()
       val date = binding.editDate.text.toString()

       if (title == "" || amount == "" || note == "" || date == "" || category == ""){
           Toast.makeText(context, "Enter all required details", Toast.LENGTH_SHORT).show()
       }else {

           if ( data.from){
               val transaction = Transaction(
                   data.data.id,
                   title = title,
                   amount = amount.toDouble(),
                   note = note,
                   date = date,
                   day = day,
                   month = month,
                   year = year,
                   category = category

               )
               viewModel.updateTransaction(transaction)
               Toast.makeText(context, "Transaction Updated Successfully", Toast.LENGTH_SHORT).show()
               val arg = AddTransactionDirections.actionAddTransactionToTransactionDetails(transaction,"AddTransaction")
               Navigation.findNavController(binding.root)
                   .navigate(arg)
           }else {
               val transaction = Transaction(
                   null,
                   title = title,
                   amount = amount.toDouble(),
                   note = note,
                   date = date,
                   day = day,
                   month = month,
                   year = year,
                   category = category

               )
               viewModel.addTransaction(transaction)
               Toast.makeText(context, "Transaction Added Successfully", Toast.LENGTH_SHORT).show()
               Navigation.findNavController(binding.root)
                   .navigate(R.id.action_addTransaction_to_dashboard2)
           }

       }


    }


    @SuppressLint("SimpleDateFormat")
    fun datePicker(binding:FragmentAddTransactionBinding){
        val cal = Calendar.getInstance()
        binding.editDate.setText(SimpleDateFormat("dd MMMM  yyyy").format(System.currentTimeMillis()))
        day = SimpleDateFormat("dd").format(System.currentTimeMillis()).toInt()
        month = SimpleDateFormat("MM").format(System.currentTimeMillis()).toInt()
        year = SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toInt()
        val dateSetListener = OnDateSetListener { _, Year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, Year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            var myFormat = "dd MMMM  yyyy" // mention the format you need
            var sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.editDate.setText(sdf.format(cal.time))
            myFormat="dd"
            sdf = SimpleDateFormat(myFormat, Locale.US)
            day =sdf.format(cal.time).toInt()
            myFormat="MM"
            sdf = SimpleDateFormat(myFormat, Locale.US)
            month = sdf.format(cal.time).toInt()
            myFormat="yyyy"
            sdf = SimpleDateFormat(myFormat, Locale.US)
            year = sdf.format(cal.time).toInt()

        }

        binding.editDate.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.food -> {
                setCategory(v,binding.food)
            }
            binding.shopping -> {
                setCategory(v,binding.shopping)
            }
            binding.transport -> {
                setCategory(v,binding.transport)
            }
            binding.health -> {
                setCategory(v,binding.health)
            }
            binding.others -> {
                setCategory(v,binding.others)
            }
            binding.academics -> {
                setCategory(v,binding.academics)
            }
        }
    }

    private fun setCategory(v: View, button: MaterialButton) {
        category = button.text.toString()
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mtrl_btn_text_btn_bg_color_selector))
        button.setIconTintResource(R.color.purple_200)
        button.setStrokeColorResource(R.color.purple_200)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        when (v) {
            binding.food -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.shopping -> {
                removeBackground(binding.food)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.transport -> {
                removeBackground(binding.shopping)
                removeBackground(binding.food)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.health -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.food)
                removeBackground(binding.others)
                removeBackground(binding.academics)
            }
            binding.others -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.food)
                removeBackground(binding.academics)
            }
            binding.academics -> {
                removeBackground(binding.shopping)
                removeBackground(binding.transport)
                removeBackground(binding.health)
                removeBackground(binding.others)
                removeBackground(binding.food)
            }
        }
    }

   private fun removeBackground(button: MaterialButton) {
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.transparent))
        button.setIconTintResource(R.color.textSecondary)
        button.setStrokeColorResource(R.color.textSecondary)
        button.setTextColor(ContextCompat.getColor(requireContext(), R.color.textSecondary))
    }

    fun showOnBoarding(){
        MaterialTapTargetPrompt.Builder(requireActivity())
            .setTarget(binding.selector1)
            .setPromptFocal(RectanglePromptFocal())
            .setPromptBackground(RectanglePromptBackground())
            .setPrimaryText("Select Any Category of Transaction")
            .setSecondaryText("Nice Select the category you spend on...")
            .setBackButtonDismissEnabled(true)
            .setPromptStateChangeListener{prompt, state ->
                if(state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                    val editor: SharedPreferences.Editor = userDetails.edit()
                    editor.putBoolean("ShowedOnboardingAddTransaction", true)
                    editor.apply()
                }
            }
            .show()
    }


}

