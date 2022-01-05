package com.pseudoencom.retrofitrecyclerview.modules.main.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
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
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.modules.main.adapter.MainRecyclerViewAdapter
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article
import com.pseudoencom.retrofitrecyclerview.modules.main.vm.SharedViewModel

class FavoritiesFragment : Fragment(), View.OnClickListener, View.OnLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: MainRecyclerViewAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var oops:ImageView
    var forSearch: MutableList<Article> = mutableListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_favorities,container,false)
        recyclerView = view.findViewById(R.id.rrViewF)
        recyclerView.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        oops = view.findViewById(R.id.oopsF)
        shimmerFrameLayout = view.findViewById(R.id.shimmerF)
        shimmerFrameLayout.visibility = View.GONE
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layoutF)
        swipeRefreshLayout.setOnRefreshListener (this)
        oops = view.findViewById(R.id.oopsF)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites().also {
            adapter = MainRecyclerViewAdapter(requireContext(), it, this, this)
            recyclerView.adapter = adapter
            forSearch = it
            if (adapter.itemCount == 0){
                recyclerView.visibility = View.GONE
                oops.visibility = View.VISIBLE

            }else{
                recyclerView.visibility = View.VISIBLE
                oops.visibility = View.GONE
            }
        }
        viewModel.giveList(forSearch)
    }

    override fun onClick(v: View?) {
        val itemView = v?.tag as Int
        val DetailFragment = DetailFragment.newInstance(forSearch[itemView])
        activity?.supportFragmentManager?.beginTransaction()?.apply {
     setCustomAnimations(R.anim.slide_up,R.anim.slide_out_right)
            replace(R.id.frgChanger, DetailFragment)
            addToBackStack("Back")
                .commit()
        }
    }
    override fun onLongClick(v: View?): Boolean {
        basicAlert(v!!)
        return true
    }
    fun basicAlert(view: View){
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            val itemView = view.tag as Int
            viewModel.removeFromFavoritesList(forSearch[itemView])
            Handler().postDelayed({
                swipeRefreshLayout.post {
                    onRefresh()
                    swipeRefreshLayout.isRefreshing = true }
            },500)
            Toast.makeText(requireContext(),
                "Deleted", Toast.LENGTH_SHORT).show()
        }
        val negativeButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(
                requireContext(),
                "Cancelled", Toast.LENGTH_SHORT
            ).show()
        }

        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle("Delete Favorite")
            setMessage("Are you sure?")
            setPositiveButton("Delete", DialogInterface.OnClickListener(function = positiveButtonClick))
            setNegativeButton("Cancel", negativeButtonClick)
            show()
        }


    }

    override fun onRefresh() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frgChanger, FavoritiesFragment())
            commit()
        }
    }
    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        act.backButton.visibility = View.GONE
        act.toolbar.elevation = 7F
    }
}