package com.sprout.api.ext

import com.google.gson.Gson

/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson():String{
    return Gson().toJson(this)
}