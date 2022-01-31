package com.pseudoencom.newsapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pseudoencom.newsapp.model.NewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(app: Application): AndroidViewModel(app) {
    var apiTokenLive: MutableLiveData<List<ApiTokenEntity>> = MutableLiveData()

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

    var allArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var appleArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var amazonArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var facebookArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var googleArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var jetbrainsArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var microsoftArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var teslaArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var readLaterArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)
    var favoritesArticlesB: MutableLiveData<Boolean> = MutableLiveData(false)


    fun setToDB(gotFromApi: MutableList<ArticlesEntity>, newsModel: NewsModel): MutableLiveData<Boolean>{
           var result = MutableLiveData<Boolean>(false)
        viewModelScope.launch(Dispatchers.IO) {
               val articlesDao = RoomAppDb.getAppDatabase((getApplication()))?.articlesDao()
               val list = articlesDao?.getArticles()
               for (i in 0 until gotFromApi.size) {
                   insertNews(gotFromApi[i], newsModel)

               }
            result.postValue(true)
           }
        return result
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
                    allArticlesB.postValue(true)
                    allArticles.postValue(mutableList)
                }
                "Apple" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Apple" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    appleArticlesB.postValue(true)
                    appleArticles.postValue(mutableList)
                }
                "Amazon" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Amazon" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    amazonArticlesB.postValue(true)
                    amazonArticles.postValue(mutableList)
                }
                "Facebook" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Facebook" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    facebookArticlesB.postValue(true)
                    facebookArticles.postValue(mutableList)
                }
                "Google" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Google" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    googleArticlesB.postValue(true)
                    googleArticles.postValue(mutableList)
                }
                "Jetbrains" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Jetbrains" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    jetbrainsArticlesB.postValue(true)
                    jetbrainsArticles.postValue(mutableList)
                }
                "Microsoft" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Microsoft" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    microsoftArticlesB.postValue(true)
                    microsoftArticles.postValue(mutableList)
                }
                "Tesla" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].tabName == "Tesla" && list[i].isFavorite == 0 && list[i].isReadLater == 0) {
                            mutableList.add(list[i])
                        }
                    }
                    teslaArticlesB.postValue(true)
                    teslaArticles.postValue(mutableList)
                }
                "isReadLater" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].isReadLater == 1) {
                            mutableList.add(list[i])
                        }
                    }
                    readLaterArticlesB.postValue(true)
                    readLaterArticles.postValue(mutableList)
                }
                "isFavorite" -> {
                    val mutableList: MutableList<ArticlesEntity> = mutableListOf()
                    for (i in 0 until list!!.size) {
                        if (list[i].isFavorite == 1) {
                            mutableList.add(list[i])
                        }
                    }
                    favoritesArticlesB.postValue(true)
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
    fun getAllNewsObserversBoolean(newsModel: NewsModel): MutableLiveData<Boolean>{
        return when(newsModel.code) {
            "Technology" -> allArticlesB
            "Apple" -> appleArticlesB
            "Amazon" -> amazonArticlesB
            "Facebook" -> facebookArticlesB
            "Google" -> googleArticlesB
            "Jetbrains" -> jetbrainsArticlesB
            "Microsoft" -> microsoftArticlesB
            "Tesla" -> teslaArticlesB
            "isReadLater" -> readLaterArticlesB
            "isFavorite" -> favoritesArticlesB
            else -> MutableLiveData<Boolean>(false)
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
    fun getObserversApiToken(): MutableLiveData<List<ApiTokenEntity>>{
        getAllApiToken()
        return apiTokenLive
    }
    fun getAllApiToken(){
        val apiTokenDao = RoomAppDb.getAppDatabase((getApplication()))?.apiTokenDao()
        val list = apiTokenDao?.getToken()
        apiTokenLive.postValue(list!!)
    }
    fun insertApiToken(apiTokenEntity: ApiTokenEntity){
        val apiTokenDao = RoomAppDb.getAppDatabase((getApplication()))?.apiTokenDao()
        apiTokenDao?.insert(apiTokenEntity)
        getAllApiToken()
    }
    fun isEmptyToken(): Boolean{
        val apiTokenDao = RoomAppDb.getAppDatabase((getApplication()))?.apiTokenDao()
        val list = apiTokenDao?.getToken()
        return list!!.size == 0
    }
}