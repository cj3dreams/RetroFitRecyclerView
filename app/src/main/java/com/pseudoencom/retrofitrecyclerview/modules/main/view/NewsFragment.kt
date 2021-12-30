package com.pseudoencom.retrofitrecyclerview.modules.main.view

import android.accounts.NetworkErrorException
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
import com.pseudoencom.retrofitrecyclerview.utils.OnSearchListener
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.modules.main.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article
import com.pseudoencom.retrofitrecyclerview.modules.main.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.modules.main.vm.NewsViewModel
import com.pseudoencom.retrofitrecyclerview.modules.main.vm.SharedViewModel

class NewsFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, OnSearchListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewModel: NewsViewModel

    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var receiveNewsModel: NewsModel
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var oops: ImageView
    var forSearch: MutableList<Article> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_news, container, false)
        recyclerView = view.findViewById(R.id.rrView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oops)
        shimmerFrameLayout = view.findViewById(R.id.shimmer)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            fetchNews(receiveNewsModel)
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.purple_200,
            R.color.purple_500,
            R.color.purple_700,
            R.color.teal_700
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getErrorHandler().observe(viewLifecycleOwner, Observer {
            if (it is NetworkErrorException && it.message != null) {
                val snackBar: Snackbar = Snackbar.make(view, it.message!!, 2500)
                snackBar.show()
                oops.visibility = View.VISIBLE
            }
        })

        viewModel.getLoading().observe(viewLifecycleOwner, Observer {
            if (it) {
                recyclerView.visibility = View.GONE
                shimmerFrameLayout.startShimmer()
                shimmerFrameLayout.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                shimmerFrameLayout.visibility = View.INVISIBLE
                shimmerFrameLayout.stopShimmer()
            }
            swipeRefreshLayout.isRefreshing = it
            shimmerFrameLayout.visibility = View.INVISIBLE
        })

        fetchNews(receiveNewsModel)
    }

    override fun onClick(v: View?) {
        val itemView = v?.tag as Int
        val DetailFragment = DetailFragment.newInstance(forSearch[itemView])
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
            setCustomAnimations(R.anim.slide_up, R.anim.slide_out_right)
                .commit()
        }
    }

    override fun onLongClick(v: View?): Boolean {
        basicAlert(v!!)
        return true
    }

    override fun onSearch(text: String) {
        viewModel.search(text).observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
        })
    }

    fun fetchNews(data: NewsModel) {
        oops.visibility = View.INVISIBLE
        viewModel.fetchNews(data).observe(viewLifecycleOwner, Observer {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
        })
    }

    fun basicAlert(view: View) {
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            sharedViewModel.addReadLaterList(forSearch[itemView])
            Toast.makeText(requireContext(), "Added to Read Later", Toast.LENGTH_SHORT).show()
        }
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view?.tag as Int
            sharedViewModel.addFavoritesList(forSearch[itemView])
            Toast.makeText(requireContext(), "Added to Favorites", Toast.LENGTH_SHORT).show()
        }
        val neutralButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                requireContext(), "Cancelled", Toast.LENGTH_SHORT
            ).show()
        }
        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle("Add to Favorites/Read Later")
            setMessage("Choose which one, add to:")
            setPositiveButton(
                "Favorites",
                DialogInterface.OnClickListener(function = positiveButtonClick)
            )
            setNegativeButton("Read Later", negativeButtonClick)
            setNeutralButton("Cancel", neutralButtonClick)
            show()
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