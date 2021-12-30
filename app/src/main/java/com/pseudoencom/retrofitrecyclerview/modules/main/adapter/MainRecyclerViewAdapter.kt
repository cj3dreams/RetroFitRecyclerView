package com.pseudoencom.retrofitrecyclerview.modules.main.adapter

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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article
import com.pseudoencom.retrofitrecyclerview.modules.main.view.NewsFragment

class MainRecyclerViewAdapter(val context: Context, var myDataSet:List<Article>, val onClickListener: View.OnClickListener, val onLongClickListener: View.OnLongClickListener)
    : RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent, false)
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

        val itemData = myDataSet[position]
        holder.nameOfNews.text = itemData.title
        holder.descrip.text = itemData.description.trim().replace("\n","")
        holder.brandName.text = " " + itemData.source.name + " "
        holder.timeRelease.text = " " +   itemData.publishedAt.substring(0,10) + " "
        loadImage(context,holder.imageViewOfNews, itemData.urlToImage)
        try {
            holder.itemView.tag = position
            holder.itemView.setOnClickListener(onClickListener)
            holder.itemView.setOnLongClickListener(onLongClickListener)
        } catch (ex: Exception) {
            ex.message?.let {
                Log.e(NewsFragment::class.java.simpleName, it)
            }
        }
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    fun loadImage(
        activity: Context?, ivUser: ImageView,
        originalImage: String?
    ) {

        activity?.let {
            Glide.with(it).load(originalImage).diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(ivUser)
        }

    }
}