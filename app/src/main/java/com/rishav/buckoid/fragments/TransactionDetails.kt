package com.rishav.buckoid.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.rishav.buckoid.R
import com.rishav.buckoid.ViewModel.TransactionViewModel
import com.rishav.buckoid.databinding.FragmentTransactionDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class TransactionDetails : Fragment() {

    val transaction by navArgs<TransactionDetailsArgs>()
    private val viewModel: TransactionViewModel by viewModels()
    lateinit var binding: FragmentTransactionDetailsBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        activity?.window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.background)
        binding = FragmentTransactionDetailsBinding.inflate(inflater, container, false)

        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNav.visibility = View.GONE

        binding.title.text = transaction.data.title
        binding.amount.text= "₹${transaction.data.amount}"
        binding.category.text = transaction.data.category
        binding.date.text = transaction.data.date
        binding.note.text = transaction.data.note

        binding.back.setOnClickListener {
            if(transaction.fragment == "Dashboard"){
                Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_dashboard2)
            }else if(transaction.fragment == "AllTransactions"){
                Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_transactions)
            }else{
                Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_dashboard2)
            }
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if(transaction.fragment == "Dashboard"){
                        Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_dashboard2)
                    }else if(transaction.fragment == "AllTransactions"){
                        Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_transactions)
                    }else{
                        Navigation.findNavController(binding.root).navigate(R.id.action_transactionDetails_to_dashboard2)
                    }
                }
            }
            )

        binding.edit.setOnClickListener {
            val argument = TransactionDetailsDirections.actionTransactionDetailsToAddTransaction(transaction.data,true)
            Navigation.findNavController(binding.root).navigate(argument)
        }
        binding.delete.setOnClickListener { deleteTransaction() }
        return binding.root
    }

    private fun deleteTransaction() {
        val bottomDialog: BottomSheetDialog = BottomSheetDialog(requireContext(),R.style.bottom_dialog)
        bottomDialog.setContentView(R.layout.dialog_delete)

        val delete=bottomDialog.findViewById<Button>(R.id.delete)
        val cancel=bottomDialog.findViewById<Button>(R.id.cancel)

        delete?.setOnClickListener{
            viewModel.deleteTransaction(transaction.data!!.id!!)
            bottomDialog.dismiss()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_transactionDetails_to_dashboard2)
        }
        cancel?.setOnClickListener{
            bottomDialog.dismiss()
        }

        bottomDialog.show()
    }


}