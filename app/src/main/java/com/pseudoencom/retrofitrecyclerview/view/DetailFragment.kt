package com.pseudoencom.retrofitrecyclerview.view

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.data.ArticlesEntity
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel
import kotlin.math.absoluteValue


class DetailFragment : Fragment(), View.OnClickListener {
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var imageView: ImageView
    private lateinit var viewModel: SharedViewModel
    private lateinit var getUrl: String
    private val retrofitService = ApiInterface.create()
    private lateinit var idFrom: ArticlesEntity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(MainRepository(retrofitService))).get(SharedViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view: View = LayoutInflater.from(requireContext())
           .inflate(R.layout.fragment_detail,container, false)

        textView = view.findViewById(R.id.detailNewsTitle)
        textView2 = view.findViewById(R.id.detailNewsDes)
        textView3 = view.findViewById(R.id.urlClicked)
        textView3.setOnClickListener(this)
        imageView = view.findViewById(R.id.detailImg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.text = idFrom.title
        textView2.text = idFrom.description + "\n" +
                idFrom.publishedAt
                loadImage(context,imageView, idFrom.urlToImage)
        getUrl = idFrom?.url.toString()
        textView3.text = "...read more"

    }

    fun loadImage(
        activity: Context?, ivUser: ImageView,
        originalImage: String?
    ) {

        activity?.let {
            Glide.with(it).load(originalImage)
                .thumbnail(Glide.with(activity).load(originalImage))
                .into(ivUser)
        }
    }

    companion object {
        fun newInstance(sayMeIdMotherFucker: ArticlesEntity): DetailFragment {
            val fragment = DetailFragment()
            fragment.idFrom = sayMeIdMotherFucker
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        act.backButton.visibility = View.VISIBLE
        act.toolbar.elevation = 7F
        if (act.backButton.isPressed){
            act.backButton.visibility = View.GONE
            act.toolbar.elevation = 0F
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val act = activity as MainActivity
        act.backButton.visibility = View.GONE
        act.toolbar.elevation = 0F
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.urlClicked -> {
                val fragment = WebViewFragment.getUrl(getUrl)
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.frgChanger, fragment)
                    addToBackStack("returnFromWeb")
                        .commit()
                }
            }
        }
    }

}