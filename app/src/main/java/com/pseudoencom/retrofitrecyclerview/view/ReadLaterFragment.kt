package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class ReadLaterFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var receiveNewsModel: NewsModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var oops:ImageView
    var sayPositionFrag: Long = 0


    private val retrofitService = ApiInterface.create()
    var forSearch: List<Article> = listOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_favorities,container,false)
        recyclerView = view.findViewById(R.id.rrViewF)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oopsF)
        shimmerFrameLayout = view.findViewById(R.id.shimmerF)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layoutF)

        receiveNewsModel = NewsModel("Apple","Apple","2021-12-18")

        swipeRefreshLayout.setOnRefreshListener{
            viewModel.sayHello(shimmerFrameLayout, recyclerView, view, receiveNewsModel, swipeRefreshLayout, oops)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.purple_200,
            R.color.purple_500,
            R.color.purple_700,
            R.color.teal_700)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
            forSearch = it
        })
        viewModel.sayHello(shimmerFrameLayout, recyclerView, view, receiveNewsModel,swipeRefreshLayout, oops)
        viewModel.giveList(forSearch)
    }

    override fun onClick(v: View?) {
        val itemView = v?.tag as Int
        val DetailFragment = DetailFragment.newInstance(forSearch[itemView])
        activity?.supportFragmentManager?.beginTransaction()?.apply {
//            setCustomAnimations(R.anim.slide_up,R.anim.slide_out_right)
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
                .commit()
        }
    }
    override fun onLongClick(v: View?): Boolean {
        Toast.makeText(requireContext(),"LongClick",Toast.LENGTH_SHORT).show()
        return true
    }
}