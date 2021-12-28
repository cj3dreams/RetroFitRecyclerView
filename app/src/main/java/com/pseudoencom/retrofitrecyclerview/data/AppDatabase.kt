package com.pseudoencom.retrofitrecyclerview.data

import ArticlesDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.pseudoencom.retrofitrecyclerview.model.ArticleModel

@Database(entities = [ArticleModel::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getArticlesDao(): ArticlesDao
}