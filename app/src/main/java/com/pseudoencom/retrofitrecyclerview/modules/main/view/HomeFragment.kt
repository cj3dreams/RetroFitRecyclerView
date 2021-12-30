package com.pseudoencom.retrofitrecyclerview.modules.main.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pseudoencom.retrofitrecyclerview.*
import com.pseudoencom.retrofitrecyclerview.modules.main.adapter.ViewPagerAdapter
import com.pseudoencom.retrofitrecyclerview.modules.main.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.modules.main.vm.SharedViewModel
import com.pseudoencom.retrofitrecyclerview.repository.network.ApiInterface
import com.pseudoencom.retrofitrecyclerview.utils.OnSearchListener

class HomeFragment : Fragment(), View.OnClickListener, OnSearchListener {

    private lateinit var viewModel: SharedViewModel
    private val retrofitService = ApiInterface.create()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var listener: OnSearchListener? = null
    lateinit var adapter: ViewPagerAdapter
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProvider(this).get(
                SharedViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, container, false)
        val list: ArrayList<NewsModel> = arrayListOf(NewsModel("All", "Technology", "2021-12-18"))
        list.add(NewsModel("Apple", "Apple", "2021-12-24"))
        list.add(NewsModel("Amazon", "Amazon", "2021-12-24"))
        list.add(NewsModel("Facebook", "Facebook", "2021-12-24"))
        list.add(NewsModel("Google", "Google", "2021-12-24"))
        list.add(NewsModel("Jetbrains", "Jetbrains", "2021-12-24"))
        list.add(NewsModel("Microsoft", "Microsoft", "2021-12-24"))
        adapter = ViewPagerAdapter(childFragmentManager, list)
        listener = adapter
        viewPager = view.findViewById(R.id.vp2)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(adapter)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onSearch(text: String) {
        listener?.onSearch(text)
    }

    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        act.backButton.visibility = View.GONE
        act.toolbar.elevation = 0F
    }
}