package com.pseudoencom.retrofitrecyclerview.vm

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Delayed
import java.util.logging.Handler

class SharedViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    var mutableLiveData: MutableLiveData<List<Article>> = MutableLiveData()

    fun sayHello(shimmerFrameLayout: ShimmerFrameLayout, recyclerView: RecyclerView, view: View){
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
                val snackbar: Snackbar = Snackbar.make(view, "Network Error \n $t", 3000)
                snackbar.show()
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))

            }
        })
    }
}