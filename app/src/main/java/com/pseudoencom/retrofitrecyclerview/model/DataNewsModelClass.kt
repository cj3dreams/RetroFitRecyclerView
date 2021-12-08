package com.pseudoencom.retrofitrecyclerview.model

data class DataNewsModelClass(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)