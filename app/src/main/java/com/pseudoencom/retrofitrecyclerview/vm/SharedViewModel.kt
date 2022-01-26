package com.pseudoencom.retrofitrecyclerview.vm

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.MainRepository
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.data.ArticlesEntity
import com.pseudoencom.retrofitrecyclerview.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel constructor(private val repository: MainRepository)  : ViewModel() {

    var mutableLiveData: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var favoritesData: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var readLaterData: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var search: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var mProfile: MutableLiveData<ArrayList<ProfileModel>> = MutableLiveData()

    var listForSeacrh: MutableList<ArticlesEntity> = mutableListOf()
    var listForFavorites: MutableList<ArticlesEntity> = mutableListOf()
    var listForReadLater: MutableList<ArticlesEntity> = mutableListOf()
    var listOfProfile: ArrayList<ProfileModel> = arrayListOf()


    fun removeFromReadLaterList(article: ArticlesEntity){
        listForReadLater.remove(article)
    }
    fun addReadLaterList(article: ArticlesEntity){
        listForReadLater.add(article)

    }
    fun getReadLater() = readLaterData
    fun fetchReadLater() = getReadLater().postValue(listForReadLater)


    fun removeFromFavoritesList(article: ArticlesEntity){
        listForFavorites.remove(article)
    }
    fun addFavoritesList(article: ArticlesEntity){
        listForFavorites.add(article)
    }
    fun getFavorites() = favoritesData
    fun fetchFavorites() = getFavorites().postValue(listForFavorites)

    fun giveList(e:MutableList<ArticlesEntity>) {
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

    fun getDataFromApi(receiveNewsModel: NewsModel){

        val response = repository.getAllData(receiveNewsModel)
        response.enqueue(object : Callback<DataNewsModelClass> {
            override fun onResponse(call: Call<DataNewsModelClass?>, response: Response<DataNewsModelClass>?) {
                if (response != null) {
                    val responseData = response.body()!!.articles
                    val mutableList = mutableListOf<ArticlesEntity>()
                    for (i in 0 until responseData.size){
                        mutableList.add((ArticlesEntity(0,
                            0,
                            responseData[i].content,
                            responseData[i].description,
                            responseData[i].publishedAt,
                            responseData[i].source.name,
                            responseData[i].title,
                            responseData[i].url,
                            "https://www.reuters.com/resizer/fJFkekJW137EJppxumHfEwAXJac=/1200x628/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/X2HY7W5EBRMPVKEDFTONJBJ7YA.jpg",
                            receiveNewsModel.code, 0, 0)))
                        }
                    mutableLiveData.postValue(mutableList)
                }
            }
            override fun onFailure(call: Call<DataNewsModelClass>?, t: Throwable?) {
//                    val snackBar: Snackbar = Snackbar.make(view, "Network Error: $t", 2500)
//                    snackBar.show()
//                    s.isRefreshing = false
//                    shimmerFrameLayout.stopShimmer()
//                    shimmerFrameLayout.visibility = View.INVISIBLE
//                    oops.visibility = View.VISIBLE
            }
        })
    }
    fun searchNews(search: String): MutableList<ArticlesEntity> {
        val listForSeacrh: MutableList<ArticlesEntity> = mutableListOf()
        for (item in this.listForSeacrh) {
            if (item.title.toLowerCase().contains(search.toLowerCase()) || item.title.toLowerCase().startsWith(search.toLowerCase()) ||
                item.description.toLowerCase().contains(search.toLowerCase()))
                listForSeacrh.add(item)
        }
        this.listForSeacrh = listForSeacrh
        return listForSeacrh
    }

    fun getProfile(): ArrayList<ProfileModel> {
        val listOfProfile: ArrayList<ProfileModel> = ArrayList()
        listOfProfile.add(ProfileModel(1, R.drawable.ic_api, "API Token"))
        listOfProfile.add(ProfileModel(2, R.drawable.ic_sorting, "Sorting"))
        listOfProfile.add(ProfileModel(3, R.drawable.ic_date, "Date from"))
        listOfProfile.add(ProfileModel(4, R.drawable.ic_cache, "Clean cache"))
        listOfProfile.add(ProfileModel(5, R.drawable.ic_about, "About me"))
        this.listOfProfile = listOfProfile
        return listOfProfile
    }
}
