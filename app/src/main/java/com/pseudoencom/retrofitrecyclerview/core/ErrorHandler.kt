package com.pseudoencom.retrofitrecyclerview.core

import android.accounts.NetworkErrorException
import android.app.Activity

class ErrorHandler {
    companion object {
        fun handle(activity: Activity, errorException: Any?) {
            if (errorException is NetworkErrorException) {

            }
        }
    }
}