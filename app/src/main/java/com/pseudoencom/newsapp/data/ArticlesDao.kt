package com.pseudoencom.newsapp.data

import androidx.room.*

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getArticles(): List<ArticlesEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(article: ArticlesEntity?)

    @Delete
    fun delete(article: ArticlesEntity?)

    @Update
    fun update(article: ArticlesEntity?)

}
