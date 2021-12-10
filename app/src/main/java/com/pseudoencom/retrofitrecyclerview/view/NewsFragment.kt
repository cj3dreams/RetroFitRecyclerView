package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class NewsFragment : Fragment(), View.OnClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private val retrofitService = ApiInterface.create()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    var receiveNewsModel: NewsModel = NewsModel("0","0","0")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_news,container,false)
        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        shimmerFrameLayout = view.findViewById(R.id.shimmer)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this)
            recyclerView.adapter = adapter
        })
        viewModel.sayHello(shimmerFrameLayout, recyclerView, view, receiveNewsModel)
    }

    override fun onClick(v: View?) {
        Toast.makeText(requireContext(),"Clicked", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance(newsModel: NewsModel): NewsFragment {
            val fragment = NewsFragment()
            fragment.receiveNewsModel = newsModel
            return fragment
        }
    }
}