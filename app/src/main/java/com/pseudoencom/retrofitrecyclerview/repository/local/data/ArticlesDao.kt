package com.pseudoencom.retrofitrecyclerview.repository.local.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles")
    fun getArticles(): List<ArticleModel>

    @Query("DELETE from articles WHERE url = :url")
    fun delete(url: String)

    @Insert
    fun insert(article: ArticleModel)

}