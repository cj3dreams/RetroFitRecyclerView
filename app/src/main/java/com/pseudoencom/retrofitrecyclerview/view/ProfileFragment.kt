package com.pseudoencom.retrofitrecyclerview.view

import ProfileAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class ProfileFragment : Fragment(), View.OnClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProfileAdapter
    lateinit var viewModel: SharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById(R.id.rRViewP)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mPFragment().observe(viewLifecycleOwner, Observer {
            adapter = ProfileAdapter(it, this, requireContext())
            recyclerView.adapter = adapter
        })
        viewModel.getProfile()
        viewModel.fetchMPF()
    }

    override fun onClick(v: View?) {

    }
    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        act.backButton.visibility = View.GONE
        act.toolbar.elevation = 7F
    }
}