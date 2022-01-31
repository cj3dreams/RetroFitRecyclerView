package com.pseudoencom.newsapp

import com.pseudoencom.newsapp.data.ApiTokenEntity
import com.pseudoencom.newsapp.model.NewsModel

class MainRepository constructor(private val retrofitService: ApiInterface) {

    fun getAllData(send: NewsModel, apiTokenEntity: List<ApiTokenEntity>) = retrofitService.getNews(send.code, send.date, "publishedAt",apiTokenEntity[apiTokenEntity.size-1].apiToken)

//    60ceca39a1a245269dc2919a50232577
}