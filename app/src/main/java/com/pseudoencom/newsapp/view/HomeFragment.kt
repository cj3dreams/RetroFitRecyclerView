package com.pseudoencom.newsapp.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val list: ArrayList<NewsModel> = arrayListOf(NewsModel("All", "Technology", "2022-01-27"))
        list.add(NewsModel("Apple", "Apple", "2022-01-27"))
        list.add(NewsModel("Amazon", "Amazon", "2022-01-27"))
        list.add(NewsModel("Facebook", "Facebook", "2022-01-27"))
        list.add(NewsModel("Google", "Google", "2022-01-27"))
        list.add(NewsModel("Jetbrains", "Jetbrains", "2022-01-27"))
        list.add(NewsModel("Microsoft", "Microsoft", "2022-01-27"))
        list.add(NewsModel("Tesla", "Tesla", "2022-01-27"))
        adapter = ViewPagerAdapter(childFragmentManager, list)
        listener = adapter
        viewPager = view.findViewById(R.id.vp2)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(adapter)
        tabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
        val isEmptyToken = roomViewModel.isEmptyToken()
        if (isEmptyToken){
            roomViewModel.insertApiToken(ApiTokenEntity(0, "5fcc2e558df247c795f3cd61d74824f9"))
            roomViewModel.insertApiToken(ApiTokenEntity(0, "5fcc2e558df247c795f3cd61d74824f9"))
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
}