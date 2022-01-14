package com.rishav.buckoid.fragments.Authentication

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.rishav.buckoid.Adapter.OnBoardingViewPagerAdapter
import com.rishav.buckoid.R
import com.rishav.buckoid.databinding.FragmentOnBoardingScreenBinding

class OnBoardingScreen : Fragment() {

    lateinit var layout:ConstraintLayout
    lateinit var binding: FragmentOnBoardingScreenBinding
    lateinit var userDetails: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=  FragmentOnBoardingScreenBinding.inflate(inflater, container, false)
        userDetails = requireActivity().getSharedPreferences("UserDetails", AppCompatActivity.MODE_PRIVATE)

        if(!userDetails.getBoolean("onBoardingShown",false)){
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView2)
                .navigate(R.id.action_onBoardingScreen_to_userSignUp,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.onBoardingScreen,
                            true).build()
                )
        }
        binding.nextBtn.setOnClickListener {

            if(getItem(0)<2) {
                binding.viewPager.setCurrentItem(getItem(1), true)
            }
            else{
                val editor: SharedPreferences.Editor = userDetails.edit()
                editor.putBoolean("onBoardingShown", true)
                editor.apply()
                Navigation.findNavController(binding.root).navigate(R.id.action_onBoardingScreen_to_userSignUp)
            }
        }

        binding.skipBtn.setOnClickListener{
            val editor: SharedPreferences.Editor = userDetails.edit()
            editor.putBoolean("onBoardingShown", true)
            editor.apply()
            Navigation.findNavController(binding.root).navigate(R.id.action_onBoardingScreen_to_userSignUp)
        }

        val viewPageAdapter = OnBoardingViewPagerAdapter(requireContext())
        binding.viewPager.setAdapter(viewPageAdapter)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                setIndicator(position)
            }

        })

        setIndicator(0)

        return binding.root
    }

    fun getItem(i:Int):Int{
        return binding.viewPager.currentItem+i
    }

    fun setIndicator(position:Int){
        val textViewArray = arrayOfNulls<TextView>(3)
        binding.indicatorLayout.removeAllViews()

        for (i in textViewArray.indices ){
            textViewArray[i] = TextView(requireContext())
            textViewArray[i]?.setText(Html.fromHtml("&#8226"))
            textViewArray[i]?.textSize = 38F
            textViewArray[i]?.setTextColor(resources.getColor(R.color.textPrimary))
            binding.indicatorLayout.addView(textViewArray[i])
        }

        textViewArray[position]?.setTextColor(resources.getColor(R.color.purple_200))

    }



}

