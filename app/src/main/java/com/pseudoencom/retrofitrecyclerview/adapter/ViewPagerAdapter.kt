package com.pseudoencom.retrofitrecyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment

class ViewPagerAdapter(fm: FragmentManager, val list: List<String>): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return NewsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position]
        }
}