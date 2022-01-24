package com.pseudoencom.retrofitrecyclerview.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class RoomViewModel(app: Application): AndroidViewModel(app) {
    lateinit var allArticles: MutableLiveData<List<ArticlesEntity>>

    init {
        allArticles = MutableLiveData()
    }

    fun getAllNewsObservers(): MutableLiveData<List<ArticlesEntity>>{
        return allArticles
    }

    fun getAllNews(){
        val articlesDao = RoomAppDatabase.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()

        allArticles.postValue(list!!)
    }

    fun insertNews(entity: ArticlesEntity){
        val articlesDao =  RoomAppDatabase.getAppDatabase(getApplication())?.articlesDao()
        articlesDao?.insert(entity)
        getAllNews()
    }
    fun deleteNews(entity: ArticlesEntity){
        val articlesDao =  RoomAppDatabase.getAppDatabase(getApplication())?.articlesDao()
        articlesDao?.delete(entity.id)
        getAllNews()
    }
}