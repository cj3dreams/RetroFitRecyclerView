package com.pseudoencom.retrofitrecyclerview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticlesEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    var id: Int = 0,
    @ColumnInfo(name = "author")
    val author: Int,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "publishedAt")
    val publishedAt: String?,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "url")
    val url: String?,
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String?,
    @ColumnInfo(name = "tabName")
    var tabName: String,
    @ColumnInfo(name  = "isFavorite")
    var isFavorite: Int,
    @ColumnInfo(name = "isReadLater")
    var isReadLater: Int
        )