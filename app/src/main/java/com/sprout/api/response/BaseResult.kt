package com.sprout.api.response

open class BaseResult<T> {
    val errorMsg: String? = null
    val errorCode: Int = 0
    val data: T? = null
}