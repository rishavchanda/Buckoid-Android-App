package com.example.trackback.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackback.Adapter.TransactionAdapter
import com.example.trackback.Model.Transaction
import com.example.trackback.R
import com.example.trackback.ViewModel.TransactionViewModel
import com.example.trackback.databinding.FragmentDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal


class Dashboard : Fragment() {

    lateinit var binding:FragmentDashboardBinding
    private val viewModel: TransactionViewModel by viewModels()
    private var totalExpense = 0.0
    private var totalGoal = 5000.0f
    private var totalFood = 0.0f
    private var totalShopping = 0.0f
    private var totalTransport=0.0f
    private var totalHealth = 0.0f
    private var totalOthers = 0.0f
    private var totalAcademics = 0.0f
    lateinit var drawerLayout:DrawerLayout
    lateinit var navigationView:NavigationView
    lateinit var userDetails: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        navigationView = requireActivity().findViewById(R.id.navigationView)
        bottomNav.visibility = View.VISIBLE
        navigationDrawer()
        getData()
        val arg = DashboardDirections.actionDashboard2ToAddTransaction(Transaction(null,"","",0.0,"",0,0,0,""),false)
        binding.addNew.setOnClickListener{Navigation.findNavController(binding.root).navigate(arg)}
        return binding.root
    }


    //calling data from room database using livedata view model
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun getData() {
        var format =  SimpleDateFormat("MM")
        val currentMonth = format.format(Calendar.getInstance().getTime())
        format =  SimpleDateFormat("yyyy")
        val currentYear = format.format(Calendar.getInstance().getTime())
        format =  SimpleDateFormat("MMMM")
        binding.date.text = "${format.format(Calendar.getInstance().getTime())} ${currentYear}"

        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)
        val name = userDetails.getString("Name", "")?.split(" ")
        binding.name.text = "Hi ${name?.get(0)} !!"

        if(!userDetails.getBoolean("ShowedOnboardingDashboard",false)){
            showOnBoarding()
        }

        totalExpense = 0.0
        totalGoal = userDetails.getString("MonthlyBudget","0")?.toFloat()!!
        totalFood = 0.0f
        totalShopping = 0.0f
        totalTransport=0.0f
        totalHealth = 0.0f
        totalOthers = 0.0f
        totalAcademics = 0.0f
        viewModel.getMonthlyTransaction(currentMonth.toInt(),currentYear.toInt()).observe(viewLifecycleOwner,{ transactionList ->
            if(transactionList.isEmpty()){
                binding.noTransactionsDoneText.text = "Add Your First Transaction of ${format.format(Calendar.getInstance().getTime())} $currentYear \n Click On + to add Transactions"
                binding.noTransactionsDoneText.visibility = View.VISIBLE
                binding.transactionRecyclerView.visibility = View.GONE
                binding.text1.visibility = View.GONE
            }else if(transactionList.size == 1 && !userDetails.getBoolean("ShowedOnboardingTransactionCard",false)){
                    //showOnBoardingTransactionCard()
            }
            else {
                binding.text1.visibility = View.VISIBLE
                binding.noTransactionsDoneText.visibility = View.GONE
                binding.transactionRecyclerView.visibility = View.VISIBLE
            }
                binding.transactionRecyclerView.layoutManager =
                    LinearLayoutManager(requireContext())
                binding.transactionRecyclerView.adapter =
                    TransactionAdapter(requireContext(), "Dashboard", transactionList.reversed())

                for (i in transactionList) {
                    totalExpense += i.amount
                    when (i.category) {
                        "Food" -> {
                            totalFood += (i.amount.toFloat())
                        }
                        "Shopping" -> {
                            totalShopping += (i.amount.toFloat())
                        }
                        "Transport" -> {
                            totalTransport += (i.amount.toFloat())
                        }

                        "Health" -> {
                            totalHealth += (i.amount.toFloat())
                        }
                        "Other" -> {
                            totalOthers += (i.amount.toFloat())
                        }
                        "Education" -> {
                            totalAcademics += (i.amount.toFloat())
                        }
                    }
                }
                binding.expense.text = "₹${totalExpense.toInt()}"
                binding.budget.text = "₹${totalGoal.toInt()}"
                if (totalExpense > totalGoal) {
                    binding.indicator.setImageResource(R.drawable.ic_negative_transaction)
                    binding.expense.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                } else {
                    binding.indicator.setImageResource(R.drawable.ic_positive_amount)
                }
                showPiChart()

        })

    }

    //To show PiChart to main card to users
    private fun showPiChart() {
        val mPieChart = binding.piechart

        mPieChart.addPieSlice(PieModel("Food", totalFood, ContextCompat.getColor(requireContext(), R.color.yellow)))
        mPieChart.addPieSlice(PieModel("Shopping", totalShopping, ContextCompat.getColor(requireContext(), R.color.lightBlue)))
        mPieChart.addPieSlice(PieModel("Health", totalHealth, ContextCompat.getColor(requireContext(), R.color.red)))
        mPieChart.addPieSlice(PieModel("Others", totalOthers, ContextCompat.getColor(requireContext(), R.color.lightBrown)))
        mPieChart.addPieSlice(PieModel("Transport", totalTransport, ContextCompat.getColor(requireContext(), R.color.violet)))
        mPieChart.addPieSlice(PieModel("Academics", totalAcademics, ContextCompat.getColor(requireContext(), R.color.green)))

        if (totalGoal>totalExpense){
            mPieChart.addPieSlice(PieModel("Left",totalGoal-(totalExpense.toFloat()) , ContextCompat.getColor(requireContext(), R.color.background_deep)))
        }

        mPieChart.startAnimation()

    }


    //navigationDrawer
    private fun navigationDrawer() {
        navigationView.bringToFront()
        binding.drawerMenu.setOnClickListener{
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }else {
                        requireActivity().finish()
                    }
                }
            }
            )

    }

    fun showOnBoarding(){
        MaterialTapTargetPrompt.Builder(requireActivity())
        .setTarget(binding.mainCard)
        .setPromptFocal(RectanglePromptFocal())
        .setPromptBackground(RectanglePromptBackground())
        .setPrimaryText("Your Monthly Details")
        .setBackgroundColour(ContextCompat.getColor(requireContext(), R.color.button))
        .setPrimaryTextColour(ContextCompat.getColor(requireContext(), R.color.textPrimary))
        .setSecondaryTextColour(ContextCompat.getColor(requireContext(), R.color.textSecondary))
        .setSecondaryText("Your Transactions visual representation and data on Monthly Basis will be shown here!!")
        .setBackButtonDismissEnabled(true)
        .setPromptStateChangeListener{prompt, state ->
            if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                showButtonPrompt()
            }
        }
        .show()

    }

    private fun showButtonPrompt() {
        MaterialTapTargetPrompt.Builder(requireActivity())
            .setTarget(binding.addNew)
            .setPrimaryText("Hey Click Me!!")
            .setFocalRadius(100.0f)
            .setBackgroundColour(ContextCompat.getColor(requireContext(), R.color.button))
            .setPrimaryTextColour(ContextCompat.getColor(requireContext(), R.color.textPrimary))
            .setSecondaryTextColour(ContextCompat.getColor(requireContext(), R.color.textSecondary))
            .setSecondaryText("Good to go Add your first Transaction, Click on this Add Button")
            .setBackButtonDismissEnabled(true)
            .setPromptStateChangeListener{prompt, state ->
                if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED ){
                    val editor: SharedPreferences.Editor = userDetails.edit()
                    editor.putBoolean("ShowedOnboardingDashboard", true)
                    editor.apply()
                }else if(state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                    showButtonPrompt()
                }
            }
            .show()
    }

    fun showOnBoardingTransactionCard(){
        MaterialTapTargetPrompt.Builder(requireActivity())
            .setTarget(binding.addNew)
            .setPrimaryText("Hey Click Me!!")
            .setFocalRadius(100.0f)
            .setBackgroundColour(ContextCompat.getColor(requireContext(), R.color.button))
            .setPrimaryTextColour(ContextCompat.getColor(requireContext(), R.color.textPrimary))
            .setSecondaryTextColour(ContextCompat.getColor(requireContext(), R.color.textSecondary))
            .setSecondaryText("Good to go Add your first Transaction, Click on this Add Button")
            .setBackButtonDismissEnabled(true)
            .setPromptStateChangeListener{prompt, state ->
                if(state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED ){
                    val editor: SharedPreferences.Editor = userDetails.edit()
                    editor.putBoolean("ShowedOnboardingTransactionCard", true)
                    editor.apply()
                }else if(state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED){
                    showButtonPrompt()
                }
            }
            .show()
    }


}


