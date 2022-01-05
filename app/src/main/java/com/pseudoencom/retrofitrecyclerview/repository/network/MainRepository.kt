package com.pseudoencom.retrofitrecyclerview.repository.network

import com.pseudoencom.retrofitrecyclerview.modules.main.model.Article
import com.pseudoencom.retrofitrecyclerview.modules.main.model.NewsModel
import com.pseudoencom.retrofitrecyclerview.modules.main.model.Source
import com.pseudoencom.retrofitrecyclerview.repository.local.data.AppDatabase
import com.pseudoencom.retrofitrecyclerview.repository.local.data.ArticleModel
import java.lang.Exception

class MainRepository constructor(
    private val retrofitService: ApiInterface,
    private var database: AppDatabase
) {

    fun getAllData(send: NewsModel) = retrofitService.getNews(
        send.code,
        send.date,
        "publishedAt",
        "83144c42e9134eab8f8813bd927b20a0"
    )

    fun insertArticle(article: Article) {
        try {
            val model = ArticleModel(
                article.author,
                article.content,
                article.description,
                article.publishedAt,
                article.source.name,
                article.title,
                article.url,
                article.urlToImage
            )
            database.getArticlesDao().insert(model)
        }catch (ex:Exception){ }
    }

    fun getArticles(): ArrayList<Article> {
        val list: ArrayList<Article> = ArrayList()
        val favorites = database.getArticlesDao().getArticles()
        for (item in favorites) {
            list.add(
                Article(
                    item.author, item.content, item.description, item.publishedAt,
                    Source("", item.source_name), item.title, item.url, item.urlToImage
                )
            )
        }
        return list
    }

    fun removeFromFavorites(url: String) {
        database.getArticlesDao().delete(url)
    }
//    a084e3b0ef354daf9d4faf00c8ee662e
}