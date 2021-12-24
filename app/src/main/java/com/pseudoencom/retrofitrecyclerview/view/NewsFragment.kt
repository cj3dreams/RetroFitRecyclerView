package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.OnSearchListener
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class NewsFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, OnSearchListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var viewModel2: SharedViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var receiveNewsModel: NewsModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var oops:ImageView
    var searchStringFromAct = ""


    private val retrofitService = ApiInterface.create()
    var forSearch: MutableList<Article> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
        viewModel2 = ViewModelProvider(requireActivity(), MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_news,container,false)
        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oops)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
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
        if (searchStringFromAct != ""){
        viewModel.nowSearch().observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
            forSearch = it
        })
        viewModel.fetchSearch(searchStringFromAct)
        viewModel.giveList(forSearch)
        }

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
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
            setCustomAnimations(R.anim.slide_up,R.anim.slide_out_right)
                .commit()
        }
    }

    override fun onSearch(text: String) {
        searchStringFromAct = text
    }

    companion object {
        fun newInstance(newsModel: NewsModel): NewsFragment {
            val fragment = NewsFragment()
            fragment.receiveNewsModel = newsModel
            return fragment
        }
    }

    override fun onLongClick(v: View?): Boolean {
        basicAlert(v!!)
        return true
    }
    fun basicAlert(view: View){
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            viewModel2.addReadLaterList(forSearch[itemView])
            Toast.makeText(requireContext(),
                "Added to Read Later", Toast.LENGTH_SHORT).show()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            viewModel2.addFavoritesList(forSearch[itemView])
            Toast.makeText(
                requireContext(),
                "Added to Favorites", Toast.LENGTH_SHORT
            ).show()
        }
        val neutralButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                requireContext(),
                "Cancelled", Toast.LENGTH_SHORT
            ).show()
        }

        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle("Add to Favorites/Read Later")
            setMessage("Choose which one, add to:")
            setPositiveButton("Read Later", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("Favorites", negativeButtonClick)
            setNeutralButton("Cancel", neutralButtonClick)
            show()
        }
    }
}