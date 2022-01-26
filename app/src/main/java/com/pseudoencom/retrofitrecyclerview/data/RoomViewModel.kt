package com.pseudoencom.retrofitrecyclerview.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class RoomViewModel(app: Application): AndroidViewModel(app) {

    var allArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()

    fun setNewsToDb(mutableList: MutableList<ArticlesEntity>){
        if (mutableList.size != 0) {
            for (i in 0 until mutableList.size){
                insertNews(mutableList[i])
            }
        }
    }

    fun getAllNewsObservers(): MutableLiveData<List<ArticlesEntity>>{
        return allArticles
    }

    fun getAllNews(){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()
        allArticles.postValue(list!!)
    }

    fun insertNews(entity: ArticlesEntity){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        articlesDao?.insert(entity)
        getAllNews()
    }
    fun deleteNews(entity: ArticlesEntity){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        articlesDao?.delete(entity)
        getAllNews()
    }
    fun isEmpty(): Boolean{
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()
        return list!!.isEmpty()
    }
}