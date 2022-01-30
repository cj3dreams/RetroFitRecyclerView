package com.pseudoencom.newsapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pseudoencom.newsapp.model.NewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(app: Application): AndroidViewModel(app) {
    var allArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var appleArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var amazonArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var facebookArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var googleArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var jetbrainsArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var microsoftArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var teslaArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var readLaterArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()
    var favoritesArticles: MutableLiveData<List<ArticlesEntity>> = MutableLiveData()


    fun setToDB(gotFromApi: MutableList<ArticlesEntity>, newsModel: NewsModel) {
           viewModelScope.launch(Dispatchers.IO) {
               val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
               val list = articlesDao?.getArticles()
               for (i in 0 until gotFromApi.size) {
                   if (!list!!.contains(gotFromApi[i])) {
                       insertNews(gotFromApi[i], newsModel)
                   }
               }
           }
    }

    fun getAllSpecialNews(newsModel: NewsModel){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()
        viewModelScope.launch(Dispatchers.IO) {
            when (newsModel.code) {
                "Technology" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Technology" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    allArticles.postValue(mutableList)
                }
                "Apple" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Apple" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    appleArticles.postValue(mutableList)
                }
                "Amazon" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Amazon" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    amazonArticles.postValue(mutableList)
                }
                "Facebook" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Facebook" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    facebookArticles.postValue(mutableList)
                }
                "Google" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Google" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    googleArticles.postValue(mutableList)
                }
                "Jetbrains" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Jetbrains" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    jetbrainsArticles.postValue(mutableList)
                }
                "Microsoft" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Microsoft" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    microsoftArticles.postValue(mutableList)
                }
                "Tesla" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Tesla" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    teslaArticles.postValue(mutableList)
                }
                "isReadLater" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].isReadLater == 1) {
                            mutableList.add(list[i])
                        }
                    }
                    readLaterArticles.postValue(mutableList)
                }
                "isFavorite" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].isFavorite == 1) {
                            mutableList.add(list[i])
                        }
                    }
                    favoritesArticles.postValue(mutableList)
                }
            }
        }
    }

    fun getAllNewsObservers(newsModel: NewsModel): MutableLiveData<List<ArticlesEntity>>?{
        getAllSpecialNews(newsModel)
         return when(newsModel.code) {
            "Technology" -> allArticles
            "Apple" -> appleArticles
            "Amazon" -> amazonArticles
            "Facebook" -> facebookArticles
            "Google" -> googleArticles
            "Jetbrains" -> jetbrainsArticles
            "Microsoft" -> microsoftArticles
            "Tesla" -> teslaArticles
             "isReadLater" -> readLaterArticles
             "isFavorite" -> favoritesArticles
            else -> null
        }
    }

    fun insertNews(entity: ArticlesEntity, newsModel: NewsModel){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        articlesDao?.insert(entity)
        getAllSpecialNews(newsModel)
    }
    fun deleteNews(entity: ArticlesEntity, newsModel: NewsModel){
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        articlesDao?.delete(entity)
        getAllSpecialNews(newsModel)
    }
    fun isEmpty(): Boolean{
        val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
        val list = articlesDao?.getArticles()
        return list!!.size == 0
    }
}