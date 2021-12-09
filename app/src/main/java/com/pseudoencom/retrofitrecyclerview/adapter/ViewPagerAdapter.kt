package com.pseudoencom.retrofitrecyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pseudoencom.retrofitrecyclerview.view.MainFragment
import com.pseudoencom.retrofitrecyclerview.view.ReadLaterFragment

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            else -> ReadLaterFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "MainFragment()"
            else -> "ReadLaterFragment()"
        }
    }
}