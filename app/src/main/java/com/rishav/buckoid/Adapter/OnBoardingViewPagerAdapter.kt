package com.rishav.buckoid.Adapter

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.rishav.buckoid.R

class OnBoardingViewPagerAdapter(context:Context): PagerAdapter() {

    val Title = intArrayOf(
        R.string.app_name,
        R.string.app_name,
        R.string.app_name
    )
    override fun getCount(): Int {
        return Title.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}