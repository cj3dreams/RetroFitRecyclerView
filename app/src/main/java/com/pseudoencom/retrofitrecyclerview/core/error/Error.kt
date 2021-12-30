package com.pseudoencom.retrofitrecyclerview.core.error

import java.io.IOException

open class Error(code: Int, msg: String?=null) : IOException(msg)