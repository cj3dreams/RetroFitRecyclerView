package com.pseudoencom.retrofitrecyclerview.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.pseudoencom.retrofitrecyclerview.model.NewsModel

class RoomViewModel(app: Application): AndroidViewModel(app) {
    var alwaysKnowNewsModelVariable: NewsModel = NewsModel("","","")
    var allArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var specialArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()

    fun setNewsToDb(mutableList: MutableList<ArticlesEntity>, newsModel: NewsModel){
        if (mutableList.size != 0) {
            for (i in 0 until mutableList.size){
                if (mutableList[i].tabName == newsModel.code)
                insertNews(mutableList[i])
            }
        }
    }

    fun getAllSpecialArticles(): MutableLiveData<List<ArticlesEntity>>{
        return specialArticles
    }

    fun getAllSpecialNews(){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()
        val newList = mutableListOf<ArticlesEntity>()
        if (list != null) {
            for (i in 0 until list.size){
                if (list[i].tabName == alwaysKnowNewsModelVariable.code){
                    newList.add(list[i])
                }
            }
        }
        allArticles.postValue(newList)
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
        return list!!.size == 0
    }

    fun alwaysKnowNewsModel(newsModel: NewsModel){
        alwaysKnowNewsModelVariable = newsModel
    }
}