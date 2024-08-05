package com.worksy.hr.art.utils

sealed class UIResult<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : UIResult<T>(data, null)
    class Error<T>(message: String?) : UIResult<T>(null, message)
    class Loading<T> : UIResult<T>()
}
