package com.pseudoencom.retrofitrecyclerview.vm

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.model.*
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Delayed
import java.util.logging.Handler

class SharedViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    var mutableLiveData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var favoritesData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var readLaterData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var search: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var mProfile: MutableLiveData<ArrayList<ProfileModel>> = MutableLiveData()

    var listForSeacrh: MutableList<Article> = mutableListOf()
    var listForFavorites: MutableList<Article> = mutableListOf()
    var listForReadLater: MutableList<Article> = mutableListOf()
    var listOfProfile: ArrayList<ProfileModel> = arrayListOf()


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

    fun giveList(e:MutableList<Article>) {
        this.listForSeacrh = e
    }
    fun nowSearch() = search
    fun fetchSearch(text: String){
        search.value = searchNews(text)
    }

    fun mPFragment() = mProfile
    fun fetchMPF() {
        mProfile.value = listOfProfile
    }

    fun sayHello(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView, view: View?, receiveNewsModel: NewsModel, s: SwipeRefreshLayout, oops:ImageView){
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
                if (view!=null) {
                    val snackBar: Snackbar = Snackbar.make(view, "Network Error: $t", 2500)
                    snackBar.show()
                    s.isRefreshing = false
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.INVISIBLE
                    oops.visibility = View.VISIBLE
                }
            }
        })
    }
    fun searchNews(search: String): MutableList<Article> {
        val listForSeacrh: MutableList<Article> = mutableListOf()
        for (item in this.listForSeacrh) {
            if (item.title.toLowerCase().contains(search.toLowerCase()) || item.title.toLowerCase().startsWith(search.toLowerCase()) ||
                item.description.toLowerCase().contains(search.toLowerCase()) ||item.source.name.toLowerCase().contains(search.toLowerCase()))
                listForSeacrh.add(item)
        }
        this.listForSeacrh = listForSeacrh
        return listForSeacrh
    }

    fun getProfile(): ArrayList<ProfileModel> {
        val listOfProfile: ArrayList<ProfileModel> = ArrayList()
        listOfProfile.add(ProfileModel(1, R.drawable.ic_favorities, "API Settings"))
        listOfProfile.add(ProfileModel(2, R.drawable.ic_favorities, "Категория"))
        listOfProfile.add(ProfileModel(3, R.drawable.ic_favorities, "Мои кошелки"))
        listOfProfile.add(ProfileModel(4, R.drawable.ic_favorities, "Долги"))
        listOfProfile.add(ProfileModel(5, R.drawable.ic_favorities, "Помощь и Поддержка"))
        this.listOfProfile = listOfProfile
        return listOfProfile
    }
}
