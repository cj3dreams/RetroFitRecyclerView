package com.pseudoencom.newsapp

import com.pseudoencom.newsapp.model.DataNewsModelClass
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiInterface {

    @GET("everything")
    fun getNews(@Query("q") code: String, @Query("from") date:String,
                @Query("sortBy") sortBy: String,@Query("apiKey") apiKey: String) : Call<DataNewsModelClass>


    companion object {

        var BASE_URL = "https://newsapi.org/v2/"

        fun create() : ApiInterface {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()


            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}