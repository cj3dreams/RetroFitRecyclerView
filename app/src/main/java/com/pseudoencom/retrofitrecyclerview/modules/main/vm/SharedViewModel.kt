package com.pseudoencom.retrofitrecyclerview.modules.main.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.modules.main.model.*

class SharedViewModel : ViewModel() {

    var favoritesData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var readLaterData: MutableLiveData<MutableList<Article>> = MutableLiveData()
    var mProfile: MutableLiveData<ArrayList<ProfileModel>> = MutableLiveData()
    var listForSeacrh: MutableList<Article> = mutableListOf()
    var listForFavorites: MutableList<Article> = mutableListOf()
    var listForReadLater: MutableList<Article> = mutableListOf()
    var listOfProfile: ArrayList<ProfileModel> = arrayListOf()


    fun removeFromReadLaterList(article: Article) {
        listForReadLater.remove(article)
    }

    fun addReadLaterList(article: Article) {
        listForReadLater.add(article)
    }

    fun getReadLater() = readLaterData

    fun fetchReadLater() = getReadLater().postValue(listForReadLater)


    fun removeFromFavoritesList(article: Article) {
        listForFavorites.remove(article)
    }

    fun addFavoritesList(article: Article) {
        listForFavorites.add(article)
    }

    fun getFavorites() = favoritesData

    fun fetchFavorites() = getFavorites().postValue(listForFavorites)

    fun giveList(e: MutableList<Article>) {
        this.listForSeacrh = e
    }

    fun mPFragment() = mProfile

    fun fetchMPF() {
        mProfile.value = listOfProfile
    }


    fun getProfile(): ArrayList<ProfileModel> {
        val listOfProfile: ArrayList<ProfileModel> = ArrayList()
        listOfProfile.add(ProfileModel(1, R.drawable.ic_api, "API Token"))
        listOfProfile.add(ProfileModel(2, R.drawable.ic_sorting, "Sorting"))
        listOfProfile.add(ProfileModel(3, R.drawable.ic_date, "Date from"))
        listOfProfile.add(ProfileModel(4, R.drawable.ic_cache, "Clean cache"))
        listOfProfile.add(ProfileModel(5, R.drawable.ic_about, "About me"))
        this.listOfProfile = listOfProfile
        return listOfProfile
    }
}
