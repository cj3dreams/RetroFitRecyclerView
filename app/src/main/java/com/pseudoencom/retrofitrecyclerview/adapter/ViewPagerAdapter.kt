package com.pseudoencom.retrofitrecyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment

class ViewPagerAdapter(fm: FragmentManager, val list: ArrayList<NewsModel>): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return NewsFragment.newInstance(list[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].title
        }
}