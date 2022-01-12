package com.rishav.buckoid.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.rishav.buckoid.R

class OnBoardingViewPagerAdapter(val context:Context): PagerAdapter() {

    val animations = intArrayOf(
        R.raw.person_thinking,
        R.raw.chart,
        R.raw.monthly_chart
    )
    val Title = intArrayOf(
        R.string.app_name,
        R.string.app_name,
        R.string.app_name
    )
    val Desc = intArrayOf(
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

    override fun instantiateItem(container: ViewGroup, position: Int): View {

        val layoutInflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.view_pager_onboarding_item,container,false)

        val animationView:LottieAnimationView = view.findViewById(R.id.animationView)
        val title = view.findViewById<TextView>(R.id.title)
        val desc = view.findViewById<TextView>(R.id.dec)

        animationView.setAnimation(animations[position])
        title.setText(Title[position])
        desc.setText(Desc[position])

        container.addView(view)

        return view



    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(ConstraintLayout(context) as View?)
    }
}