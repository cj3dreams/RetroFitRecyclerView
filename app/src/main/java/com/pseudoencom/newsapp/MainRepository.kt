package com.pseudoencom.newsapp

import com.pseudoencom.newsapp.model.NewsModel

class MainRepository constructor(private val retrofitService: ApiInterface) {

    fun getAllData(send: NewsModel) = retrofitService.getNews(send.code, send.date, "publishedAt","a084e3b0ef354daf9d4faf00c8ee662e")

//    a084e3b0ef354daf9d4faf00c8ee662e
}