package com.pseudoencom.newsapp.model

data class DataNewsModelClass(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)