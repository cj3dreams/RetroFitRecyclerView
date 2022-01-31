package com.pseudoencom.newsapp.view

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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.newsapp.ApiInterface
import com.pseudoencom.newsapp.MainRepository
import com.pseudoencom.newsapp.OnSearchListener
import com.pseudoencom.newsapp.R
import com.pseudoencom.newsapp.adapter.MainRecyclerViewAdapter
import com.pseudoencom.newsapp.data.ApiTokenEntity
import com.pseudoencom.newsapp.data.ArticlesEntity
import com.pseudoencom.newsapp.data.RoomViewModel
import com.pseudoencom.newsapp.model.NewsModel
import com.pseudoencom.newsapp.vm.MyViewModelFactory
import com.pseudoencom.newsapp.vm.SharedViewModel
import kotlin.properties.Delegates

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
    lateinit var isAllDownloaded: String
    lateinit var isAllUploaded: String

    private val retrofitService = ApiInterface.create()
    var gotFromApi: MutableList<ArticlesEntity> = mutableListOf()
    var gotFromDB: MutableList<ArticlesEntity> = mutableListOf()
    var apiTokenList: List<ApiTokenEntity> = listOf()

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

        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oops)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            recyclerView.visibility = View.INVISIBLE
            val isOnline = isOnline(requireContext())
            if(isOnline) {
                val isEmptyToken = roomViewModel.isEmptyToken()
                if (!isEmptyToken){
                    roomViewModel.getObserversApiToken().observe(viewLifecycleOwner, Observer {
                        apiTokenList = it
                        viewModel.getDataFromApi(receiveNewsModel, apiTokenList).observe(viewLifecycleOwner, Observer {
                            if (it) {
                                isAllDownloaded = it.toString()
                            }
                        })
                    })
                }
                viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
                    gotFromApi = it
                    roomViewModel.setToDB(it, receiveNewsModel).observe(viewLifecycleOwner, Observer {
                        isAllUploaded = it.toString()
                        if(isAllUploaded == "true") {
                            roomViewModel.getAllNewsObserversBoolean(receiveNewsModel).observe(viewLifecycleOwner, Observer {
                                if (it) {
                                    isAllUploaded = it.toString()
                                    if (isAllUploaded == "true" && isAllDownloaded == "true"){
                                        recyclerView.visibility = View.VISIBLE
                                        shimmerFrameLayout.stopShimmer()
                                        swipeRefreshLayout.isRefreshing = false
                                    }
                                }
                            })
                        }
                    })
                    roomViewModel.getAllNewsObservers(receiveNewsModel)?.observe(viewLifecycleOwner, Observer {
                        adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        gotFromDB = it.toMutableList()
                        viewModel.giveList(it.toMutableList())
                    })
                })
            }else if(!isOnline){
                val isEmpty = roomViewModel.isEmpty()
                if(!isEmpty){
                    roomViewModel.getAllNewsObservers(receiveNewsModel)?.observe(viewLifecycleOwner, Observer {
                        adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                        recyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        gotFromDB = it.toMutableList()
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
            viewModel.nowSearch().observe(viewLifecycleOwner, Observer {
                adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                recyclerView.adapter = adapter
            })
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.purple_300,
            R.color.purple_500,
            R.color.purple_700,
            R.color.purple_700
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isOnline = isOnline(requireContext())
        shimmerFrameLayout.visibility = View.VISIBLE
        oops.visibility = View.INVISIBLE
        if(isOnline) {
            val isEmptyToken = roomViewModel.isEmptyToken()
            if (!isEmptyToken){
                roomViewModel.getObserversApiToken().observe(viewLifecycleOwner, Observer {
                    apiTokenList = it
                    viewModel.getDataFromApi(receiveNewsModel, apiTokenList).observe(viewLifecycleOwner, Observer {
                        if (it) {
                            isAllDownloaded = it.toString()
                        }
                    })
                })
            }
            viewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
                gotFromApi = it
                roomViewModel.setToDB(it, receiveNewsModel).observe(viewLifecycleOwner, Observer {
                    isAllUploaded = it.toString()
                    if(isAllUploaded == "true") {
                        roomViewModel.getAllNewsObserversBoolean(receiveNewsModel).observe(viewLifecycleOwner, Observer {
                                if (it) {
                                    isAllUploaded = it.toString()
                                    if (isAllUploaded == "true" && isAllDownloaded == "true"){
                                        recyclerView.visibility = View.VISIBLE
                                        shimmerFrameLayout.stopShimmer()
                                        swipeRefreshLayout.isRefreshing = false
                                    }
                                }
                            })
                    }
                })
                roomViewModel.getAllNewsObservers(receiveNewsModel)?.observe(viewLifecycleOwner, Observer {
                    adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    gotFromDB = it.toMutableList()
                    viewModel.giveList(it.toMutableList())
                })
            })
        }else if(!isOnline){
            val isEmpty = roomViewModel.isEmpty()
            if(!isEmpty){
                roomViewModel.getAllNewsObservers(receiveNewsModel)?.observe(viewLifecycleOwner, Observer {
                    adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    gotFromDB = it.toMutableList()
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
        viewModel.nowSearch().observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
        })
    }

    override fun onClick(v: View?) {
        val itemView = v?.tag as Int
        val DetailFragment = DetailFragment.newInstance(gotFromDB[itemView])

        activity?.supportFragmentManager?.beginTransaction()?.apply {
            setCustomAnimations(R.anim.blink,0)
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
                .commit()
        }
    }

    override fun onSearch(text: String) {
        viewModel.fetchSearch(text)
        viewModel.giveList(gotFromDB)
    }

    override fun onLongClick(v: View?): Boolean {
        basicAlert(v!!)
        return true
    }
    fun basicAlert(view: View){
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            roomViewModel.insertNews(
                ArticlesEntity(0,
                    gotFromDB[itemView].author,
                    gotFromDB[itemView].content,
                    gotFromDB[itemView].description,
                    gotFromDB[itemView].publishedAt,
                    gotFromDB[itemView].source,
                    gotFromDB[itemView].title,
                    gotFromDB[itemView].url,
                    gotFromDB[itemView].urlToImage,
                    gotFromDB[itemView].tabName,0,1),
                NewsModel("isReadLater","isReadLater","isReadLater"))
            Toast.makeText(requireContext(), "Added to Read Later", Toast.LENGTH_SHORT).show()
        }
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            roomViewModel.insertNews(
                ArticlesEntity(0,
                    gotFromDB[itemView].author,
                    gotFromDB[itemView].content,
                    gotFromDB[itemView].description,
                    gotFromDB[itemView].publishedAt,
                    gotFromDB[itemView].source,
                    gotFromDB[itemView].title,
                    gotFromDB[itemView].url,
                    gotFromDB[itemView].urlToImage,
                    gotFromDB[itemView].tabName,1,0),
                NewsModel("isFavorite","isFavorite","isFavorite"))
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