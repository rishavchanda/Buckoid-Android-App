package com.example.trackback.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trackback.R
import com.example.trackback.databinding.FragmentAddExpenseBinding

class AddExpense : Fragment() {
   lateinit var bindingAddExpense: FragmentAddExpenseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding =  FragmentAddExpenseBinding.inflate(inflater, container, false)
        //binding.selector1.
        return binding.root
    }


}