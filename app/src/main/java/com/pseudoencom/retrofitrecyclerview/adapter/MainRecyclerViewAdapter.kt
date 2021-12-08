package com.pseudoencom.retrofitrecyclerview.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.facebook.shimmer.ShimmerFrameLayout
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import com.pseudoencom.retrofitrecyclerview.view.MainFragment

class MainRecyclerViewAdapter(val context: Context, val myDataSet:List<Article>, val onClickListener: View.OnClickListener)
    : RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var id: Int = 0
        var imageViewOfNews: ImageView
        var nameOfNews: TextView
        var descrip: TextView
        var timeRelease: TextView
        var brandName: TextView

        init {
            brandName = view.findViewById(R.id.brandName)
            timeRelease = view.findViewById(R.id.itemTimeTime)
            imageViewOfNews = view.findViewById(R.id.itemImg)
            nameOfNews = view.findViewById(R.id.itemNewsTitle)
            descrip = view.findViewById(R.id.itemNewsDes)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shimmer = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.parseColor("#CFCFCF"))
            .setBaseAlpha(1F)
            .setHighlightColor(Color.parseColor("#f3f3f3"))
            .setHighlightAlpha(1f)
            .setDropoff(50f)
            .build()

        val shimmerDrawable = ShimmerDrawable()
        shimmerDrawable.setShimmer(shimmer)


        val itemData = myDataSet.get(position)
        holder.nameOfNews.text = itemData.title
        holder.descrip.text = itemData.description.trim().replace("\n","")
        holder.brandName.text = itemData.source.name
        holder.timeRelease.text =  itemData.publishedAt.substring(0,10)

        loadImage(context,holder.imageViewOfNews, itemData.urlToImage, itemData.urlToImage)
        try {
            holder.itemView.tag = itemData
            holder.itemView.setOnClickListener(onClickListener)
        } catch (ex: Exception) {
            ex.message?.let {
                Log.e(MainFragment::class.java.simpleName, it)
            }
        }
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    fun loadImage(
        activity: Context?, ivUser: ImageView, thumbnailImage: String?,
        originalImage: String?
    ) {

        activity?.let {
            Glide.with(it).load(originalImage)
                .thumbnail(Glide.with(activity).load(thumbnailImage))
                .into(ivUser)
        }
    }
}