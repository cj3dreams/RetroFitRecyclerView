package com.pseudoencom.newsapp.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pseudoencom.newsapp.*
import com.pseudoencom.newsapp.adapter.ViewPagerAdapter
import com.pseudoencom.newsapp.data.ApiTokenEntity
import com.pseudoencom.newsapp.data.RoomViewModel
import com.pseudoencom.newsapp.model.NewsModel
import com.pseudoencom.newsapp.vm.MyViewModelFactory
import com.pseudoencom.newsapp.vm.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener, OnSearchListener {

    private lateinit var viewModel: SharedViewModel
    private val retrofitService = ApiInterface.create()
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private var listener: OnSearchListener? = null
    lateinit var adapter: ViewPagerAdapter
    lateinit var roomViewModel: RoomViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
                SharedViewModel::class.java
            )
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_home, container, false)
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().minusDays(1).toString().substring(0,10)
        } else {
            getCurrentDate().substring(0,10)
        }
        val list: ArrayList<NewsModel> = arrayListOf(NewsModel("All", "Technology", date))
        list.add(NewsModel("Apple", "Apple", date))
        list.add(NewsModel("Amazon", "Amazon", date))
        list.add(NewsModel("Facebook", "Facebook", date))
        list.add(NewsModel("Google", "Google", date))
        list.add(NewsModel("Jetbrains", "Jetbrains", date))
        list.add(NewsModel("Microsoft", "Microsoft", date))
        list.add(NewsModel("Tesla", "Tesla", date))
        adapter = ViewPagerAdapter(childFragmentManager, list)
        listener = adapter
        viewPager = view.findViewById(R.id.vp2)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(adapter)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        val isEmptyToken = roomViewModel.isEmptyToken()
        if (isEmptyToken){
            roomViewModel.insertApiToken(ApiTokenEntity(0, "8e9a5d60b6704002a2a35b80b835bfb8"))
            roomViewModel.insertApiToken(ApiTokenEntity(0, "8e9a5d60b6704002a2a35b80b835bfb8"))
        }
        return view
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

    override fun onClick(v: View?) {

    }
    fun getCurrentDate():String{
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1);
        return calendar.toString()
    }
}