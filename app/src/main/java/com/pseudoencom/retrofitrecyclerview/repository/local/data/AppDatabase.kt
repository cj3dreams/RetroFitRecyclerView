package com.pseudoencom.retrofitrecyclerview.repository.local.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getArticlesDao(): ArticlesDao
}