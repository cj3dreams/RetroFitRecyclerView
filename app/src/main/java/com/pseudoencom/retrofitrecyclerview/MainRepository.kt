package com.pseudoencom.retrofitrecyclerview

class MainRepository constructor(private val retrofitService: ApiInterface) {

    fun getAllData() = retrofitService.getData()
}