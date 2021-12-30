package com.pseudoencom.retrofitrecyclerview.modules.main.model

data class DataNewsModelClass(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)