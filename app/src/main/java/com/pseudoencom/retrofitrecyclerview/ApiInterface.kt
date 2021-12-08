package com.pseudoencom.retrofitrecyclerview

import com.pseudoencom.retrofitrecyclerview.model.DataNewsModelClass
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    @GET("v2/everything?q=Apple&from=2021-12-07&sortBy=popularity&apiKey=a084e3b0ef354daf9d4faf00c8ee662e")
    fun getData() : Call<DataNewsModelClass>

    companion object {

        var BASE_URL = "https://newsapi.org/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}