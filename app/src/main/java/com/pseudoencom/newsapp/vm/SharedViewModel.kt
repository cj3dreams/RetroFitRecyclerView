package com.pseudoencom.newsapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pseudoencom.newsapp.MainRepository
import com.pseudoencom.newsapp.R
import com.pseudoencom.newsapp.data.ApiTokenEntity
import com.pseudoencom.newsapp.data.ArticlesEntity
import com.pseudoencom.newsapp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel constructor(private val repository: MainRepository) : ViewModel() {

    var mutableLiveData: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var search: MutableLiveData<MutableList<ArticlesEntity>> = MutableLiveData()
    var mProfile: MutableLiveData<ArrayList<ProfileModel>> = MutableLiveData()
    var listForSeacrh: MutableList<ArticlesEntity> = mutableListOf()
    var listOfProfile: ArrayList<ProfileModel> = arrayListOf()


    fun giveList(e: MutableList<ArticlesEntity>) {
        this.listForSeacrh = e
    }

    fun nowSearch() = search
    fun fetchSearch(text: String) {
        search.value = searchNews(text)
    }

    fun mPFragment() = mProfile
    fun fetchMPF() {
        mProfile.value = listOfProfile
    }

    fun getDataFromApi(receiveNewsModel: NewsModel, apiTokenEntity: List<ApiTokenEntity>): MutableLiveData<Boolean> {
        var isApiDownloadedAll:  MutableLiveData<Boolean> = MutableLiveData(false)
        viewModelScope.launch(Dispatchers.IO) {
        val response = repository.getAllData(receiveNewsModel, apiTokenEntity)
        response.enqueue(object : Callback<DataNewsModelClass> {
            override fun onResponse(call: Call<DataNewsModelClass?>, response: Response<DataNewsModelClass>?) {
                if (response != null) {
                        val responseData = response.body()?.articles
                        val mutableList = mutableListOf<ArticlesEntity>()
                        if (responseData != null) {

                                for (i in 0 until responseData.size) {
                                    if (responseData[i].title != "") {
                                        mutableList.add(
                                            (ArticlesEntity(
                                                0, 0,
                                                responseData[i].content,
                                                responseData[i].description,
                                                responseData[i].publishedAt,
                                                responseData[i].source.name,
                                                responseData[i].title,
                                                responseData[i].url, responseData[i].urlToImage,
                                                receiveNewsModel.code, 0, 0
                                            ))
                                        )
                                    }
                                }

                        } else {
                            mutableList.add(
                                ArticlesEntity(0, 0,
                                    "Api Token Time Ended or Incorrect cj3dreams",
                                    " Add New Token from newpsapi.org or Just Remove App Please and Type me telegram @cj3dreams",
                                    "Api Token Time Ended",
                                    "Api Token Time Ended",
                                    "Api Token Time Ended",
                                    "Api Token Time Ended",
                                    "Api Token Time Ended",
                                    "Api Token Time Ended",
                                    0, 0))
                        }
                    mutableLiveData.postValue(mutableList)
                    isApiDownloadedAll.postValue(true)
                }
            }
            override fun onFailure(call: Call<DataNewsModelClass>?, t: Throwable?) {

            }
        })
        }
        return isApiDownloadedAll
    }

    fun searchNews(search: String): MutableList<ArticlesEntity> {
        val listForSeacrh: MutableList<ArticlesEntity> = mutableListOf()
        for (item in this.listForSeacrh) {
            if (item.title?.toLowerCase()!!
                    .contains(search.toLowerCase()) || item.title?.toLowerCase()
                    .startsWith(search.toLowerCase()) ||
                item.description?.toLowerCase()!!.contains(search.toLowerCase())
            )
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
