package com.pseudoencom.retrofitrecyclerview.modules.main.model

class LocalArticleModel(
    val author: Any,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    var isReadLater: Boolean = false,
    var isFavorite: Boolean = false,
)