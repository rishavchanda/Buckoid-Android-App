package com.rishav.buckoid.fragments.Authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rishav.buckoid.MainActivity
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentUserDetailsBinding

class UserDetails : Fragment() {

    lateinit var binding:FragmentUserDetailsBinding
    lateinit var userDetails: SharedPreferences
    var isFirstTime:Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=  FragmentUserDetailsBinding.inflate(inflater, container, false)
        setData()
        return binding.root
    }

    private fun setData() {
        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        isFirstTime = userDetails.getBoolean("isFirstTime",true)
        if (!isFirstTime){
            goToNextScreen()
        }else {
            binding.next.setOnClickListener {
                saveUserData()
            }
        }
    }

    private fun goToNextScreen() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveUserData() {
        val name = binding.editName.text.toString()
        val monthly_budget = binding.editMoney.text.toString()
        val yearly_budget = binding.editYearMoney
        if(name.equals("") || monthly_budget.equals("") || yearly_budget.text.toString().equals("")) {
            Toast.makeText(requireActivity(), "Enter all details to continue...", Toast.LENGTH_SHORT).show()
        }else{
            val editor: SharedPreferences.Editor = userDetails.edit()
            editor.putBoolean("isFirstTime", false)
            editor.putString("Name", name)
            editor.putString("MonthlyBudget", monthly_budget)
            editor.putString("YearlyBudget", yearly_budget.text.toString())
            editor.apply()
            goToNextScreen()
        }
    }



}