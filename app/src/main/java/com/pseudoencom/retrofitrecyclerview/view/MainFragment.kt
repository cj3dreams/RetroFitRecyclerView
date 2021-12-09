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

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private val retrofitService = ApiInterface.create()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
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
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_main,container,false)
        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)

        val fm:FragmentManager = requireActivity().supportFragmentManager
        val fragmentAdapter = ViewPagerAdapter(fm)
        viewPager = view.findViewById(R.id.vp2)
        viewPager.adapter = fragmentAdapter

        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this)
            recyclerView.adapter = adapter
        })
        viewModel.sayHello(shimmerFrameLayout, recyclerView, view)
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(),"Clicked", Toast.LENGTH_SHORT).show()
    }
}