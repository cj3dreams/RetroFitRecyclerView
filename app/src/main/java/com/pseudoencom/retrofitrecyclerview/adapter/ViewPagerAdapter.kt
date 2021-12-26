package com.pseudoencom.retrofitrecyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.pseudoencom.retrofitrecyclerview.OnSearchListener
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment


class ViewPagerAdapter(fm: FragmentManager, val list: ArrayList<NewsModel>) :
    FragmentPagerAdapter(fm), OnSearchListener, ViewPager.OnPageChangeListener {
    var listener: OnSearchListener? = null
    var fragments = arrayOfNulls<NewsFragment>(10)


    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        if (fragments[position] != null) {
            return fragments[position]!!
        } else {
            val fragment = NewsFragment.newInstance(list[position])
             if (position==0)listener = fragment
             fragments[position] = fragment
            return fragment
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].title
    }

    override fun onSearch(text: String) {
        listener?.onSearch(text)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        listener = fragments[position]
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}