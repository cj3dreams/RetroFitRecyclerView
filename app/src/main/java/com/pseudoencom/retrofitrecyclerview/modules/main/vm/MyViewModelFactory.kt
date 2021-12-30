package com.pseudoencom.retrofitrecyclerview.modules.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pseudoencom.retrofitrecyclerview.repository.network.MainRepository

class MyViewModelFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            SharedViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}