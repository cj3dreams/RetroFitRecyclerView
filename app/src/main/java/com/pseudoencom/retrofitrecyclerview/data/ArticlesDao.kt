package com.pseudoencom.retrofitrecyclerview.data

import androidx.room.*

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getArticles(): List<ArticlesEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: ArticlesEntity?)

    @Delete
    fun delete(article: ArticlesEntity?)

    @Update
    fun update(article: ArticlesEntity?)

}
