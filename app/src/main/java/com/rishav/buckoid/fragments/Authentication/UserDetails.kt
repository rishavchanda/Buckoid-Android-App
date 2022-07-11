package com.rishav.buckoid.fragments.Authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rishav.buckoid.MainActivity
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentUserDetailsBinding

class UserDetails : Fragment() {

    lateinit var binding:FragmentUserDetailsBinding
    lateinit var userDetails: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        getActivity()?.getWindow()?.setStatusBarColor(ContextCompat.getColor(requireActivity(), R.color.background))
        binding=  FragmentUserDetailsBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
        val name=googleSignInAccount?.displayName.toString().split(" ")
        binding.text1.text = "Good to go \n${name[0]} !!"
        currencyPicker()
        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        binding.next.setOnClickListener {
            saveUserData()
        }
    }

    private fun currencyPicker() {
        val currencyList:List<String> = listOf("₹ Indian Rupee","$ Dollar","£ United Kingdom Pound","$ Argentina Peso","ƒ Aruba Guilder","₼ Azerbaijan Manat","Br Belarus Ruble","лв Bulgaria Lev","R$ Brazil Real","៛ Cambodia Riel","¥ China Yuan Renminbi","৳ Bangladeshi taka")
        val currencyAdapter = ArrayAdapter(requireContext(),R.layout.currency_item,currencyList)
        binding.currencyPicker.setAdapter(currencyAdapter)
    }

    private fun goToNextScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveUserData() {
        val monthly_budget = binding.editMoney.text.toString()
        val yearly_budget = binding.editYearMoney
        val currency = binding.currencyPicker.selectedItem.toString()
        if(monthly_budget.equals("") || yearly_budget.text.toString().equals("")) {
            Toast.makeText(requireActivity(), "Enter all details to continue...", Toast.LENGTH_SHORT).show()
        }else{
            val editor: SharedPreferences.Editor = userDetails.edit()
            editor.putBoolean("isFirstTime", false)
            editor.putString("MonthlyBudget", monthly_budget)
            editor.putString("YearlyBudget", yearly_budget.text.toString())
            editor.putString("currency_name", currency.trim())
            editor.putString("currency", currency.split(" ")[0].trim())
            editor.apply()
            goToNextScreen()
        }
    }



}