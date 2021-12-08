package com.pseudoencom.retrofitrecyclerview.vm

import android.provider.ContactsContract
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.pseudoencom.retrofitrecyclerview.ApiInterface
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import com.pseudoencom.retrofitrecyclerview.model.Source
import com.pseudoencom.retrofitrecyclerview.view.MainFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    var mutableLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    fun sayHello(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView){
        val response = repository.getAllData()
        response.enqueue(object : Callback<DataNewsModelClass> {
            override fun onResponse(call: Call<DataNewsModelClass?>, response: Response<DataNewsModelClass>?) {
                if (response != null) {
                    mutableLiveData.postValue(response.body()!!.articles)
                    recyclerView.visibility = View.VISIBLE
                    shimmerFrameLayout.stopShimmer()

                }
            }
            override fun onFailure(call: Call<DataNewsModelClass>?, t: Throwable?) {

                }
        })
    }
}