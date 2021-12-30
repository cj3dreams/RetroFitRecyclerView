package com.pseudoencom.retrofitrecyclerview.modules.main.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article


class DetailFragment : Fragment() {
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var imageView: ImageView
    private lateinit var idFrom: Article

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view: View = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_detail,container, false)
        textView = view.findViewById(R.id.detailNewsTitle)
        textView2 = view.findViewById(R.id.detailNewsDes)
        imageView = view.findViewById(R.id.detailImg)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.text = idFrom.title
        textView2.text = idFrom.description + "\n" +
                idFrom.publishedAt + "\n" + "\n" + idFrom.url
                loadImage(context,imageView, idFrom.urlToImage)
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

    fun loadImage(activity: Context?, ivUser: ImageView, originalImage: String?) {
        activity?.let {
            Glide.with(it).load(originalImage)
                .thumbnail(Glide.with(activity).load(originalImage))
                .into(ivUser)
        }
    }


    companion object {
        fun newInstance(sayMeIdMotherFucker: Article): DetailFragment {
            val fragment = DetailFragment()
            fragment.idFrom = sayMeIdMotherFucker
            return fragment
        }
    }
}