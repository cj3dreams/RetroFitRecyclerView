package com.pseudoencom.retrofitrecyclerview.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.pseudoencom.retrofitrecyclerview.repository.local.data.AppDatabase
import com.pseudoencom.retrofitrecyclerview.repository.network.ApiInterface
import com.pseudoencom.retrofitrecyclerview.repository.network.MainRepository


open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    protected val repository: MainRepository = MainRepository(
        ApiInterface.create(),
        Room.databaseBuilder(application, AppDatabase::class.java, "DB_NAME")
            .allowMainThreadQueries().build()
    )
    private val errorHandler: MutableLiveData<Any> = MutableLiveData()

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    protected fun setLoading(isLoading: Boolean) {
        this.isLoading.postValue(isLoading)
    }

    fun getErrorHandler(): LiveData<Any> {
        return errorHandler
    }

    protected fun setError(error: Any) {
        this.errorHandler.postValue(error)
    }
}