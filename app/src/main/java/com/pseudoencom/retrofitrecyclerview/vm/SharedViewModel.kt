package com.pseudoencom.retrofitrecyclerview.vm

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.model.Source
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Delayed
import java.util.logging.Handler

class SharedViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    var mutableLiveData: MutableLiveData<List<Article>> = MutableLiveData()
    var favoritesData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var readLaterData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var listForSeacrh: List<Article> = listOf()
    var listForFavorites: MutableList<Article> = mutableListOf()
    var listForReadLater: MutableList<Article> = mutableListOf()
    var search: MutableLiveData<List<Article>> = MutableLiveData()

    fun removeFromReadLaterList(article: Article){
        listForReadLater.remove(article)
    }
    fun addReadLaterList(article: Article){
        listForReadLater.add(article)
    }
    fun getReadLater() = readLaterData
    fun fetchReadLater() = getReadLater().postValue(listForReadLater)


    fun removeFromFavoritesList(article: Article){
        listForFavorites.remove(article)
    }
    fun addFavoritesList(article: Article){
        listForFavorites.add(article)
    }
    fun getFavorites() = favoritesData
    fun fetchFavorites() = getFavorites().postValue(listForFavorites)

    fun detailF() = mutableLiveData


    fun giveList(e:List<Article>) {
        listForSeacrh = e
    }
    fun nowSearch() = search
    fun fetchSearch(text: String){
        search.value = searchNews(text)
    }

    fun sayHello(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView, view: View, receiveNewsModel: NewsModel, s: SwipeRefreshLayout, oops:ImageView){
        shimmerFrameLayout.visibility = View.VISIBLE
        oops.visibility = View.INVISIBLE

        val response = repository.getAllData(receiveNewsModel)
        response.enqueue(object : Callback<DataNewsModelClass> {
            override fun onResponse(call: Call<DataNewsModelClass?>, response: Response<DataNewsModelClass>?) {
                if (response != null) {
                    mutableLiveData.postValue(response.body()!!.articles)
                    recyclerView.visibility = View.VISIBLE
                    shimmerFrameLayout.stopShimmer()
                    s.isRefreshing = false

                }
            }
            override fun onFailure(call: Call<DataNewsModelClass>?, t: Throwable?) {
                val snackBar: Snackbar = Snackbar.make(view, "Network Error: $t", 2500)
                snackBar.show()
                s.isRefreshing = false
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.INVISIBLE
                oops.visibility = View.VISIBLE

            }
        })
    }
    fun searchNews(search: String): List<Article> {
        val news: ArrayList<Article> = ArrayList()
        for (item in this.listForSeacrh) {
            if ((item.title.contains(search.capitalize()) || item.title.contains(search) || item.title.contains(
                    search.toLowerCase()
                ) || item.title.contains(search.toUpperCase()) || item.title.startsWith(search)) || item.description.contains(search) || item.source.name.contains(
                    search
                )
            )
                news.add(
                    Article(
                        item.author,
                        item.content,
                        item.description,
                        item.publishedAt,
                        item.source,
                        item.title,
                        item.url,
                        item.urlToImage
                    )
                )
        }
        return news
    }
}