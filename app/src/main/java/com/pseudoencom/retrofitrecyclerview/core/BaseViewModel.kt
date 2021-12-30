package com.pseudoencom.retrofitrecyclerview.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pseudoencom.retrofitrecyclerview.repository.network.ApiInterface
import com.pseudoencom.retrofitrecyclerview.repository.network.MainRepository


open class BaseViewModel() : ViewModel() {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    protected val repository: MainRepository = MainRepository(ApiInterface.create())
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