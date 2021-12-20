package com.pseudoencom.retrofitrecyclerview

import com.pseudoencom.retrofitrecyclerview.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment

class MainRepository constructor(private val retrofitService: ApiInterface) {

    fun getAllData(send: NewsModel) = retrofitService.getNews(send.code, send.date, "publishedAt","d874a248928649a298b089c831b08706")

//    a084e3b0ef354daf9d4faf00c8ee662e
}