package com.pseudoencom.retrofitrecyclerview.modules.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pseudoencom.retrofitrecyclerview.core.BaseViewModel
import com.pseudoencom.retrofitrecyclerview.core.error.NoInternetConnection
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article
import com.pseudoencom.retrofitrecyclerview.modules.main.model.DataNewsModelClass
import com.pseudoencom.retrofitrecyclerview.modules.main.model.NewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class NewsViewModel : BaseViewModel() {
    private var newsList: List<Article> = ArrayList()

    fun getNews(): List<Article> = newsList

    fun fetchNews(data: NewsModel): LiveData<List<Article>> {
        val result: MutableLiveData<List<Article>> = MutableLiveData()
        setLoading(true)
        repository.getAllData(data).enqueue(object : Callback<DataNewsModelClass> {
            override fun onResponse(
                call: Call<DataNewsModelClass?>,
                response: Response<DataNewsModelClass>?
            ) {
                response?.body()?.let {
                    newsList = it.articles
                    result.postValue(it.articles)
                }
                setLoading(false)
            }

            override fun onFailure(call: Call<DataNewsModelClass>?, t: Throwable?) {
                setLoading(false)
                setError(NoInternetConnection("Network Error: $t"))
                /*if (view != null) {
                    val snackBar: Snackbar = Snackbar.make(view, "Network Error: $t", 2500)
                    snackBar.show()
                    s.isRefreshing = false
                    shimmerFrameLayout.stopShimmer()
                    shimmerFrameLayout.visibility = View.INVISIBLE
                    oops.visibility = View.VISIBLE
                }*/
            }
        })
        return result
    }

    fun search(text: String): LiveData<List<Article>> {
        val result: MutableLiveData<List<Article>> = MutableLiveData()
        viewModelScope.launch(Dispatchers.IO) {
            if (newsList.size > 0) {
                val search = text.lowercase(Locale.getDefault())
                val list: ArrayList<Article> = ArrayList()
                for (item in newsList) {
                    if (item.title.toLowerCase().contains(search) || item.description.toLowerCase()
                            .contains(search.toLowerCase())
                    ) {
                        list.add(item)
                    }
                }
                if (list.size > 0) result.postValue(list)
                else result.postValue(newsList)
            }
        }
        return result
    }
}