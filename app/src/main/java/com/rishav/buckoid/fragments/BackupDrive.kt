package com.rishav.buckoid.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentBackupDriveBinding

class BackupDrive : Fragment() {

    lateinit var binding:FragmentBackupDriveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding =  FragmentBackupDriveBinding.inflate(inflater, container, false)
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNav.visibility = View.GONE

        setData()
        return binding.root
    }

    private fun setData() {
        binding.back.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_backupDrive_to_profile)
        }
    }


}