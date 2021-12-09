package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.adapter.ViewPagerAdapter
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import com.pseudoencom.retrofitrecyclerview.model.Source
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: SharedViewModel
    private val retrofitService = ApiInterface.create()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home,container,false)
        var list = listOf("hello", "hello", "hello", "hello")
        val fm:FragmentManager = requireActivity().supportFragmentManager
        val fragmentAdapter = ViewPagerAdapter(fm,list)
        viewPager = view.findViewById(R.id.vp2)
        viewPager.adapter = fragmentAdapter
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(),"Clicked", Toast.LENGTH_SHORT).show()
    }
}