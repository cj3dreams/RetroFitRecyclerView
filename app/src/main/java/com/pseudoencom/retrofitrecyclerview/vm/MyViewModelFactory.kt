package com.pseudoencom.retrofitrecyclerview.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pseudoencom.retrofitrecyclerview.MainRepository

class MyViewModelFactory constructor(private val repository: MainRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            SharedViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}