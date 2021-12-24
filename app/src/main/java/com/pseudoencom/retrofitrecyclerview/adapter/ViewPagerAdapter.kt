package com.pseudoencom.retrofitrecyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pseudoencom.retrofitrecyclerview.OnSearchListener
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment

class ViewPagerAdapter(fm: FragmentManager, val list: ArrayList<NewsModel>) :
    FragmentPagerAdapter(fm), OnSearchListener {
    var listener: OnSearchListener? = null
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        val fragment = NewsFragment.newInstance(list[position])
        listener = fragment
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].title
    }

    override fun onSearch(text: String) {
        listener?.onSearch(text)
    }
}