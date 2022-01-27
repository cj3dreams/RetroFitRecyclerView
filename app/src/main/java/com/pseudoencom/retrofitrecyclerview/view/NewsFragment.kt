package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.pseudoencom.retrofitrecyclerview.data.ArticlesEntity
import com.pseudoencom.retrofitrecyclerview.data.RoomViewModel
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class NewsFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, OnSearchListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var viewModel2: SharedViewModel
    private lateinit var roomViewModel: RoomViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var receiveNewsModel: NewsModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var oops:ImageView

    private val retrofitService = ApiInterface.create()
    var gotFromApi: MutableList<ArticlesEntity> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
        viewModel2 = ViewModelProvider(requireActivity(), MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_news,container,false)
        roomViewModel.alwaysKnowNewsModel(receiveNewsModel)
        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oops)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener{
            viewModel.getDataFromApi(receiveNewsModel)
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
        var isOnline = isOnline(requireContext())
        shimmerFrameLayout.visibility = View.VISIBLE
        oops.visibility = View.INVISIBLE
        if(isOnline) {
            viewModel.getDataFromApi(receiveNewsModel)
            viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
                gotFromApi = it
                roomViewModel.setNewsToDb(gotFromApi, receiveNewsModel)
                roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, Observer {
                        adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        viewModel.giveList(it.toMutableList())
                })
            })
            recyclerView.visibility = View.VISIBLE
            shimmerFrameLayout.stopShimmer()
            swipeRefreshLayout.isRefreshing = false
        }else if(!isOnline){
            val isEmpty = roomViewModel.isEmpty()
            Snackbar.make(view, "$isEmpty", 1500).show()
            if(!isEmpty){
                roomViewModel.getAllNewsObservers().observe(viewLifecycleOwner, Observer {
                    adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    viewModel.giveList(it.toMutableList())
                })
                recyclerView.visibility = View.VISIBLE
                shimmerFrameLayout.stopShimmer()
                swipeRefreshLayout.isRefreshing = false
            }else if (isEmpty){
                swipeRefreshLayout.isRefreshing = false
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.INVISIBLE
                oops.visibility = View.VISIBLE
            }
        }
        else {
            oops.visibility = View.VISIBLE
        }

        viewModel.nowSearch().observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
            gotFromApi = it
        })
    }

    override fun onClick(v: View?) {
        val itemView = v?.tag as Int
        val DetailFragment = DetailFragment.newInstance(gotFromApi[itemView])
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
            setCustomAnimations(R.anim.slide_up,R.anim.slide_out_right)
                .commit()
        }
    }

    override fun onSearch(text: String) {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        viewModel.fetchSearch(text)
        viewModel.giveList(gotFromApi)
    }

    override fun onLongClick(v: View?): Boolean {
        basicAlert(v!!)
        return true
    }
    fun basicAlert(view: View){
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            viewModel2.addReadLaterList(gotFromApi[itemView])
            Toast.makeText(requireContext(), "Added to Read Later", Toast.LENGTH_SHORT).show()
        }
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            viewModel2.addFavoritesList(gotFromApi[itemView])
            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
        }
        val neutralButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
        }
        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle("Add to Favorites/Read Later")
            setMessage("Choose which one, add to:")
            setPositiveButton("Favorites", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("Read Later", negativeButtonClick)
            setNeutralButton("Cancel", neutralButtonClick)
            show()
        }
    }
    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }

    companion object {
        fun newInstance(newsModel: NewsModel): NewsFragment {
            val fragment = NewsFragment()
            fragment.receiveNewsModel = newsModel
            return fragment
        }
    }
}